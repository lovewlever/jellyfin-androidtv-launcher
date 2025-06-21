//
// Created by catog on 2025/6/17.
//

#include "../include/DrogonConfig.h"

#include "GLog.h"
#include "yaml-cpp/yaml.h"

DrogonConfig &DrogonConfig::getInstance()
{
    static DrogonConfig instance;
    return instance;
}

int32_t DrogonConfig::loadDrogonConfig()
{
    const auto yaml = YAML::LoadFile("config/drogon-config.yaml");
    if (yaml)
    {
        try
        {
            this->gqScreensaverFolderPath = yaml["ScreensaverFolderPath"].as<std::string>();
            this->gqListenPort = yaml["ListenPort"].as<int32_t>();
            this->gqListenHost = yaml["ListenHost"].as<std::string>();
            GLog::log() << "drogon-config loaded." << std::endl;
            std::cout << "drogon-config loaded." << std::endl;
            return 0;
        } catch (const YAML::Exception &e)
        {
            GLog::log() << "DrogonConfig loading failed." << e.msg << std::endl;
            std::cerr << "DrogonConfig loading failed." << e.msg << std::endl;
            return -1;
        }
    }
    GLog::log() << "YAML loading failed." << std::endl;
    std::cerr << "YAML loading failed." << std::endl;
    return -1;
}

std::string DrogonConfig::getScreensaverFolderPath() const
{
    return this->gqScreensaverFolderPath;
}

int32_t DrogonConfig::getListenPort() const
{
    return this->gqListenPort;
}

std::string DrogonConfig::getListenHost() const
{
    return this->gqListenHost;
}
