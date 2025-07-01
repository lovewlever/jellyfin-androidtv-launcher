//
// Created by catog on 2025/7/1.
//

#ifndef WEATHERDATA_H
#define WEATHERDATA_H
#include <string>

#include "nlohmann/json.hpp"

struct WeatherData {
    std::string city{""};
    std::string weather{""};
    std::string temperature{""};
    std::string temperatureFloat{""};
    std::string humidity{""};
    std::string humidityFloat{""};
    std::string winddirection{""};
    std::string windpower{""};
    std::string reporttime{""};

    nlohmann::json to_json() {
        return nlohmann::json{
            {"city", city},
            {"weather", weather},
            {"temperature", temperature},
            {"temperatureFloat", temperatureFloat},
            {"humidity", humidity},
            {"humidityFloat", humidityFloat},
            {"winddirection", winddirection},
            {"windpower", windpower},
            {"reporttime", reporttime}
        };
    }
};



#endif //WEATHERDATA_H
