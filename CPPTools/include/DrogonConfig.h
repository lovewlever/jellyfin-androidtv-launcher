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
private:
    std::string gqScreensaverFolderPath;
    DrogonConfig() = default;
    ~DrogonConfig() = default;
};


#endif //DROGONCONFIG_H
