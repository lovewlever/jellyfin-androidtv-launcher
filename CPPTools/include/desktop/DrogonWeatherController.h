//
// Created by catog on 2025/7/1.
//

#ifndef DROGONWEATHERCONTROLLER_H
#define DROGONWEATHERCONTROLLER_H
#include "drogon/HttpController.h"


class DrogonWeatherController final : public drogon::HttpController<DrogonWeatherController>
{
public:
    METHOD_LIST_BEGIN
        ADD_METHOD_TO(queryWeather, "/queryWeather", drogon::Get);
    METHOD_LIST_END

    void queryWeather(const drogon::HttpRequestPtr &req,
                      std::function<void(const drogon::HttpResponsePtr &)> &&callback) const;
};

#endif //DROGONWEATHERCONTROLLER_H
