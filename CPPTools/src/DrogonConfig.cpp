//
// Created by catog on 2025/6/17.
//

#include "../include/DrogonConfig.h"

#include "GLog.h"
#include "yaml-cpp/yaml.h"

DrogonConfig & DrogonConfig::getInstance()
{
    static DrogonConfig instance;
    return instance;
}

int32_t DrogonConfig::loadDrogonConfig()
{
    const auto yaml = YAML::LoadFile("config/drogon-config.yaml");
    if (yaml)
    {
        const auto folder = yaml["ScreensaverFolderPath"];
        if (folder)
        {
            this->gqScreensaverFolderPath = folder.as<std::string>();
            GLog::log() << "drogon-config loaded." << std::endl;
            return 0;
        }
        GLog::log() << "ScreensaverFolderPath loading failed." << std::endl;
        return -1;
    }
    GLog::log() << "YAML loading failed." << std::endl;
    return -1;
}

std::string DrogonConfig::getScreensaverFolderPath() const
{
    return this->gqScreensaverFolderPath;
}


