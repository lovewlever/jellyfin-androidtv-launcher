#pragma once
#define NOMINMAX
#include <drogon/drogon.h>
#include <mobile/HttpGQScreensaver.h>

#include "Constants.h"
#include "GLog.h"
#include "httplib.h"
#include "include/desktop/DrogonServer.h"
#include "mobile/ScreensaverImageList.h"
#include "nlohmann/json.hpp"
#include "desktop/HAOperate.h"
#include "desktop/WeatherAMapApi.h"
#include "openssl/err.h"
#include "openssl/opensslv.h"
#include "openssl/ssl.h"
#include "openssl/types.h"

using namespace drogon;

std::unique_ptr<HAOperate> haOperatePtr{nullptr};

int main()
{
    // GLog::EnableLog = true;
    // Constants::setMobileServerUrl("http://127.0.0.1:8097");
    //Constants::setMobilePackageCacheDirPath(R"(C:\Users\catog\Pictures)");
    //
    // const auto saverPtr = HttpGQScreensaver::newInstance();
    // const auto remoteList = saverPtr->queryScreensaverImageList("127.0.0.1", 8097);
    //
    // ScreensaverImageList::updateScreensaverRemoteImageList(remoteList);
    // const auto list = ScreensaverImageList::getScreensaverRemoteImageList();
    // const auto str = *list;
    // const auto imageList = nlohmann::json::parse(*list);
    // ScreensaverImageList::downloadScreensaverRemoteImageListToLocalPath(imageList);
    //
    //ScreensaverImageList::getScreensaverLocalImageList();
    //return 0;

    //std::cout << "Openssl Version: " << OPENSSL_VERSION_TEXT << std::endl;

    // haOperatePtr = HAOperate::create();
    // haOperatePtr->start();


    const auto ds = std::make_unique<DrogonServer>();
    return ds->startServer(false);
}
