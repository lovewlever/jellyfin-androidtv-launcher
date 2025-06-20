//
// Created by catog on 2025/6/17.
//

#ifndef DROGONCONFIG_H
#define DROGONCONFIG_H
#include <cstdint>
#include <string>
#include <memory>


class DrogonConfig
{
public:
    DrogonConfig(const DrogonConfig &) = delete;
    DrogonConfig &operator=(const DrogonConfig &) = delete;

    static DrogonConfig &getInstance();
    int32_t loadDrogonConfig();
    std::string getScreensaverFolderPath() const;
    [[nodiscard]] int32_t getListenPort() const;
    [[nodiscard]] std::string getListenHost() const;
private:
    std::string gqScreensaverFolderPath;
    int32_t gqListenPort{0};
    std::string gqListenHost{"0.0.0.0"};
    DrogonConfig() = default;
    ~DrogonConfig() = default;
};


#endif //DROGONCONFIG_H
