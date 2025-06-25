//
// Created by catog on 2025/6/24.
//

#include "../../include/mobile/ScreensaverImageList.h"

#include <filesystem>
#include <thread>
#include <vector>
#include <fstream>

#include "Constants.h"
#include "GLog.h"
#include "httplib.h"


std::shared_ptr<std::string> ScreensaverImageList::getScreensaverRemoteImageList()
{
    GLog::logD("getImageList: ", "====");
    std::lock_guard<std::mutex> lock(screensaverMutex);
    return screensaverRemoteImageList;
}

std::vector<std::string> ScreensaverImageList::getScreensaverLocalImageList()
{
    const auto TAG = "getScreensaverLocalImageList";
    static const std::regex imageRegex(R"(.*\.(webp|png|jpe?g)$)", std::regex::icase);
    std::vector<std::string> files{};
    const auto savePath = Constants::getMobileScreensaverImageListDirPath();
    GLog::logD(TAG, ("从本地目录获取图片文件：" + savePath).c_str());
    for (const std::filesystem::path path(savePath); const auto &entry : std::filesystem::directory_iterator(path))
    {
        if (entry.is_regular_file())
        {
            const auto &filePath = entry.path();
            const auto &fileExt = filePath.extension().string();
            if (std::regex_match(fileExt, imageRegex))
            {
                GLog::logD(TAG, (filePath.string() + " 是图片，添加到列表").c_str());
                files.emplace_back(filePath.string());
            } else
            {
                GLog::logE(TAG, (filePath.string() + " 不是图片").c_str());
            }
        } else
        {
            GLog::logE(TAG, (entry.path().string() + " 不是常规文件").c_str());
        }
    }
    GLog::logD(TAG, ("返回从本地目录获取图片文件：数量：" + std::to_string(files.size())).c_str());
    return files;
}

void ScreensaverImageList::updateScreensaverRemoteImageList(const std::string &imagesStr)
{
    GLog::logD("updateScreensaverRemoteImageList: ", imagesStr.c_str());
    std::lock_guard<std::mutex> lock(screensaverMutex);
    screensaverRemoteImageList = std::make_shared<std::string>(imagesStr);
    GLog::logD("updateScreensaverRemoteImageList: ", "Done");
}

/**
 * 下载远程图片到本地目录
 * @param images
 */
void ScreensaverImageList::downloadScreensaverRemoteImageListToLocalPath(const std::vector<std::string> &images)
{
    std::lock_guard<std::mutex> lock(screensaverMutex);
    const auto savePath = Constants::getMobileScreensaverImageListDirPath();
    if (!std::filesystem::is_directory(savePath))
    {
        GLog::logD("dSRemoteILToLocalPath", (savePath + "不存在，将创建。").c_str());
        std::filesystem::create_directory(savePath);
    }

    // 线程数量
    // const auto threadNum = static_cast<size_t>(std::__math::ceil(images.size() / 5));
    size_t threadNum = std::thread::hardware_concurrency();
    if (images.size() <= 4)
    {
        threadNum = 1;
    }
    std::vector<std::vector<std::string> > threadGroups{threadNum};
    std::ostringstream oss{};
    oss << "图片数量：" << images.size() << "; 线程数量: " << threadNum;
    GLog::logD("dSRemoteILToLocalPath", oss.str().c_str());

    const auto serverUrl = Constants::getMobileServerUrl();
    for (int i = 0; i < images.size(); ++i)
    {
        const auto &imagename = images[i];
        threadGroups.at(i % threadNum).emplace_back(imagename);
    }
    oss = std::ostringstream{};
    oss << "每个线程下载数量：" << std::endl;
    for (int i = 0; i < threadGroups.size(); ++i)
    {
        oss << "Group(" << i << ") ThreadNum: " << threadGroups.at(i).size() << std::endl;
    }
    GLog::logD("dSRemoteILToLocalPath", oss.str().c_str());

    std::vector<std::thread> threads{};
    for (const auto &vecs: threadGroups)
    {
        threads.emplace_back([&vecs, &savePath, &serverUrl]()
        {
            const auto threadId = std::this_thread::get_id();
            std::ostringstream osss{};
            osss << "Start Thread: " << threadId << std::endl;
            GLog::logD("dSRemoteILToLocalPath", osss.str().c_str());
            for (const auto &imageName: vecs)
            {

                std::string fullImageUrl = serverUrl + "/" + imageName;
                std::string fullLocalFilePath{savePath + "/" + imageName};

                GLog::logD("dSRemoteILToLocalPath", ("Start Download: " + fullImageUrl).c_str());
                std::ofstream ofs{fullLocalFilePath, std::ios::out | std::ios::binary};
                if (!ofs.is_open())
                {
                    GLog::logE("dSRemoteILToLocalPath", (fullLocalFilePath + " ofs 打开失败").c_str());
                    continue;
                }

                httplib::Client client(serverUrl);
                auto resp = client.Get("/" + imageName, [&ofs](const char *data, size_t data_length) -> bool
                {
                    ofs.write(data, data_length);
                    return true;
                });
                ofs.close();
                if (resp.error() == httplib::Error::Success)
                {
                    GLog::logE("dSRemoteILToLocalPath", (fullImageUrl + " 下载成功").c_str());
                } else
                {
                    std::stringstream ss{};
                    ss << fullImageUrl << " 下载失败：" << resp.error();
                    GLog::logE("dSRemoteILToLocalPath", ss.str().c_str());
                    if (std::filesystem::exists(fullLocalFilePath))
                    {
                        std::filesystem::remove(fullLocalFilePath);
                    }
                }
            }
        });
    }

    for (auto &thread: threads)
    {
        if (thread.joinable())
        {
            thread.join();
        }
    }


    GLog::logD("dSRemoteILToLocalPath", "全部下载完成。");
}
