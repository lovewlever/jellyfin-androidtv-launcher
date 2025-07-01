//
// Created by catog on 2025/7/1.
//

#include "../../include/desktop/WeatherAMapApi.h"

#include "httplib.h"
#include "desktop/DrogonConfig.h"
#include "nlohmann/adl_serializer.hpp"
#include "nlohmann/json.hpp"


std::unique_ptr<WeatherData> WeatherAMapApi::queryWeather() const
{
    const auto [City, Extensions, Output, Key] = DrogonConfig::getInstance().getDrogonAMapWeatherConfig();
    std::ostringstream oss{};
    oss << "/v3/weather/weatherInfo?key=" << Key;
    oss << "&city=" << City;
    oss << "&extensions=" << Extensions;
    oss << "&output=" << Output;
    httplib::Client sslCli{"restapi.amap.com", 80};
    const auto resp = sslCli.Get(oss.str());
    if (resp.error() == httplib::Error::Success)
    {
        // {"status":"1","count":"1","info":"OK","infocode":"10000","lives":[{"province":"山东","city":"兰山区","adcode":"371302","
        //weather":"阴","temperature":"32","winddirection":"西南","windpower":"≤3","humidity":"68","reporttime":"2025-07-01 11:01:
        //04","temperature_float":"32.0","humidity_float":"68.0"}]}
        const auto body = resp->body;
        try
        {
            const auto json = nlohmann::json::parse(body);
            if (const auto status = json["status"].get<std::string>(); status == "1")
            {
                if (const auto livesObj = json["lives"]; livesObj.is_array() && livesObj.size() > 0)
                {
                    const auto liveObj = livesObj[0];
                    const auto city = liveObj["city"].get<std::string>();
                    const auto weather = liveObj["weather"].get<std::string>();
                    const auto temperature = liveObj["temperature"].get<std::string>();
                    const auto temperatureFloat = liveObj["temperature_float"].get<std::string>();
                    const auto humidity = liveObj["humidity"].get<std::string>();
                    const auto humidityFloat = liveObj["humidity_float"].get<std::string>();
                    const auto winddirection = liveObj["winddirection"].get<std::string>();
                    const auto windpower = liveObj["windpower"].get<std::string>();
                    const auto reporttime = liveObj["reporttime"].get<std::string>();

                    return std::make_unique<WeatherData>(city, weather, temperature, temperatureFloat, humidity,
                                                         humidityFloat, winddirection, windpower, reporttime);
                }
            }
        } catch (const std::exception &e)
        {
            return nullptr;
        }
    }

    return nullptr;
}
