//
// Created by catog on 2025/6/17.
//

#include "../include/HttpGQScreensaver.h"
#include <httplib.h>
#include <ClientConfig.h>

std::unique_ptr<HttpGQScreensaver> HttpGQScreensaver::newInstance()
{
    return {};
}

std::vector<std::string> HttpGQScreensaver::queryScreensaverImageList() const
{

    const auto &ins = ClientConfig::getInstance();
    httplib::Client cli{ins.getServerHostName(), ins.getServerPort()};
    const auto resp = cli.Get("/screensaverIL");
    if (resp->status == 200)
    {
        std::cout << resp->body << "\n";
    }
    return {};
}
