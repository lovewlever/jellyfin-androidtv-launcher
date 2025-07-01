//
// Created by catog on 2025/7/1.
//

#ifndef WATHERAMAPAPI_H
#define WATHERAMAPAPI_H
#include <memory>

#include "IWeatherApi.h"


// 临沂市	371300	0539
// 兰山区	371302	0539
// 罗庄区	371311	0539
// 河东区	371312	0539
// 蒙阴县	371328	0539

class WeatherAMapApi final : public IWeatherApi
{

public:
    WeatherAMapApi() = default;
    ~WeatherAMapApi() override = default;

    std::unique_ptr<WeatherData> queryWeather() const override;

};


#endif //WATHERAMAPAPI_H
