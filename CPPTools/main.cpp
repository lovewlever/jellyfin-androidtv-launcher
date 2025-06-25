#include <drogon/drogon.h>
#include <mobile/HttpGQScreensaver.h>

#include "Constants.h"
#include "GLog.h"
#include "httplib.h"
#include "include/desktop/DrogonServer.h"
#include "mobile/ScreensaverImageList.h"
#include "nlohmann/json.hpp"

using namespace drogon;


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

    const auto ds = std::make_unique<DrogonServer>();
    return ds->startServer(false);
}
