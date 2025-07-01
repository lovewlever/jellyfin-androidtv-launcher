//
// Created by catog on 2025/7/1.
//

#include "../../include/desktop/DrogonWeatherController.h"

#include "GLog.h"
#include "desktop/WeatherAMapApi.h"

void DrogonWeatherController::queryWeather(const drogon::HttpRequestPtr &req, std::function<void(const drogon::HttpResponsePtr &)> &&callback) const
{
    const auto peer = req->peerAddr();
    GLog::log() << "queryWeather: Receive Request IP: " << peer.toIp() << std::endl;

    if (const auto weatherPtr = IWeatherApi::create<WeatherAMapApi>()->queryWeather(); weatherPtr != nullptr)
    {
        const auto json = weatherPtr->to_json();
        GLog::log() << "queryWeather: " << json.dump() << std::endl;
        const auto resp = drogon::HttpResponse::newHttpResponse();
        resp->setBody(json.dump());
        callback(resp);
    } else
    {
        GLog::log() << "queryWeather: " << "nullptr" << std::endl;
        const auto resp = drogon::HttpResponse::newHttpResponse();
        resp->setBody("{}");
        callback(resp);
    }
}
