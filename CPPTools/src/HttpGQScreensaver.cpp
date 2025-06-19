//
// Created by catog on 2025/6/17.
//

#include "../include/HttpGQScreensaver.h"
#include <httplib.h>

#include "nlohmann/json.hpp"
#include "GLog.h"

std::unique_ptr<HttpGQScreensaver> HttpGQScreensaver::newInstance()
{
    return {};
}

std::string HttpGQScreensaver::queryScreensaverImageList(const std::string &hostName,
                                                                      const int32_t hostPort) const
{
    const auto TAG = "JNI-HttpGQScreensaver#queryScreensaverImageList";
    std::ostringstream oss;
    oss << "HostName: " << hostName << "; HostPort: " << hostPort;
    GLog::logD(TAG, oss.str().c_str());
    httplib::Client cli{hostName, hostPort};
    const auto resp = cli.Get("/screensaverIL");
    if (resp.error() == httplib::Error::Success)
    {
        try
        {
            const auto jsonStr = resp->body;
            GLog::logE(TAG, jsonStr.c_str());
            return jsonStr;
        } catch (const std::exception &e)
        {
            GLog::logE(TAG, e.what());
        }
    } else
    {
        GLog::logE(TAG, to_string(resp.error()).c_str());
    }
    return {};
}
