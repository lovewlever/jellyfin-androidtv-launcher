//
// Created by catog on 2025/6/17.
//

#ifndef DROGONSCREENSAVERCONTROLLER_H
#define DROGONSCREENSAVERCONTROLLER_H

#include <drogon/drogon.h>

class DrogonScreensaverController: public drogon::HttpController<DrogonScreensaverController> {

public:
    METHOD_LIST_BEGIN
    ADD_METHOD_TO(DrogonScreensaverController::getImage, "/{imageName}", drogon::HttpMethod::Get);
    ADD_METHOD_TO(DrogonScreensaverController::queryScreensaverImageList, "/screensaverIL", drogon::HttpMethod::Get);
    METHOD_LIST_END

    DrogonScreensaverController() = default;
    ~DrogonScreensaverController() override = default ;
    void queryScreensaverImageList(const drogon::HttpRequestPtr &req, std::function<void(const drogon::HttpResponsePtr &)> &&callback);
    void getImage(const drogon::HttpRequestPtr &req, std::function<void(const drogon::HttpResponsePtr &)> &&callback, const std::string &imageName);
};



#endif //DROGONSCREENSAVERCONTROLLER_H
