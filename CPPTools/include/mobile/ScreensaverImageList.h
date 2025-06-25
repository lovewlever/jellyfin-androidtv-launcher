//
// Created by catog on 2025/6/24.
//

#ifndef SCREENSAVERIMAGELIST_H
#define SCREENSAVERIMAGELIST_H
#include <memory>
#include <mutex>
#include <vector>


class ScreensaverImageList {
public:
    ScreensaverImageList() = delete;
    ~ScreensaverImageList() = delete;
    ScreensaverImageList(const ScreensaverImageList &) = delete;
    ScreensaverImageList(ScreensaverImageList &&) = delete;
    ScreensaverImageList &operator=(const ScreensaverImageList &) = delete;
    ScreensaverImageList &operator=(ScreensaverImageList &&) = delete;

    static std::shared_ptr<std::string> getScreensaverRemoteImageList();

    static std::vector<std::string> getScreensaverLocalImageList();

    static void updateScreensaverRemoteImageList(const std::string &imagesStr);

    static void downloadScreensaverRemoteImageListToLocalPath(const std::vector<std::string> &images);
private:
    inline static auto screensaverRemoteImageList{std::make_shared<std::string>("")};
    inline static std::mutex screensaverMutex{};
};



#endif //SCREENSAVERIMAGELIST_H
