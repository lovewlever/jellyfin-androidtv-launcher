//
// Created by catog on 2025/6/17.
//

#include "../include/DrogonScreensaverController.h"
#include <DrogonConfig.h>
#include <filesystem>
#include <nlohmann/json.hpp>


void DrogonScreensaverController::queryScreensaverImageList(const drogon::HttpRequestPtr &req,
                                                            std::function<void(const drogon::HttpResponsePtr &)> &&
                                                            callback)
{
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
    const auto response = drogon::HttpResponse::newHttpResponse();
    response->setBody(json.dump());
    callback(response);
}

void DrogonScreensaverController::getImage(const drogon::HttpRequestPtr &req,
    std::function<void(const drogon::HttpResponsePtr &)> &&callback, const std::string &imageName)
{
    const auto &dConfig = DrogonConfig::getInstance();
    const auto &folder = dConfig.getScreensaverFolderPath();
    const auto &file = folder + "\\" + imageName;
    const auto resp = drogon::HttpResponse::newFileResponse(file);
    callback(resp);
}
