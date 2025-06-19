//
// Created by catog on 2025/6/17.
//

#include "../include/DrogonScreensaverController.h"
#include <DrogonConfig.h>
#include <filesystem>
#include <nlohmann/json.hpp>

#include "GLog.h"


void DrogonScreensaverController::queryScreensaverImageList(const drogon::HttpRequestPtr &req,
                                                            std::function<void(const drogon::HttpResponsePtr &)> &&
                                                            callback)
{
    const auto peer = req->peerAddr();
    GLog::log() << "queryScreensaverImageList: 接收到请求IP: " << peer.toIp() << "\n";
    const auto &dConfig = DrogonConfig::getInstance();
    const auto &folder = dConfig.getScreensaverFolderPath();
    std::filesystem::directory_iterator iter{folder};
    std::vector<std::string> imagePaths{};
    for (const auto &entry : iter)
    {
        if (entry.is_regular_file())
        {
            imagePaths.push_back(std::filesystem::path(entry.path()).filename().string());
        }
    }
    nlohmann::json json = imagePaths;
    GLog::log() << "queryScreensaverImageList: 查询到的图片列表: " << json.dump() << "\n";
    const auto response = drogon::HttpResponse::newHttpResponse();
    response->setBody(json.dump());
    callback(response);
}

void DrogonScreensaverController::getImage(const drogon::HttpRequestPtr &req,
    std::function<void(const drogon::HttpResponsePtr &)> &&callback, const std::string &imageName)
{
    const auto peer = req->peerAddr();
    GLog::log() << "getImage: 接收到请求IP: " << peer.toIp() << "\n";
    const auto &dConfig = DrogonConfig::getInstance();
    const auto &folder = dConfig.getScreensaverFolderPath();
    const auto &file = folder + "\\" + imageName;
    GLog::log() << "getImage: 返回图片: " << file << "\n";
    const auto resp = drogon::HttpResponse::newFileResponse(file);
    callback(resp);
}
