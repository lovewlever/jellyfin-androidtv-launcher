//
// Created by catog on 2025/7/1.
//

#ifndef IWEATHERAPI_H
#define IWEATHERAPI_H
#include <memory>
#include "desktop/WeatherData.h"

class IWeatherApi;

template<typename T>
concept ConceptWeatherApi = std::is_base_of_v<IWeatherApi, T>;

class IWeatherApi {

public:

    virtual ~IWeatherApi() = default;
    virtual std::unique_ptr<WeatherData> queryWeather() const = 0;

    template<ConceptWeatherApi T>
    static std::unique_ptr<T> create()
    {
        return std::unique_ptr<T>(new T());
    }

protected:
    IWeatherApi() = default;
};



#endif //IWEATHERAPI_H
