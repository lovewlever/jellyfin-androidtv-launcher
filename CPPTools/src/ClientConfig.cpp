//
// Created by catog on 2025/6/17.
//

#include "../include/ClientConfig.h"
#include <yaml-cpp/yaml.h>
#include <GLog.h>

ClientConfig & ClientConfig::getInstance()
{
    static ClientConfig instance;
    return instance;
}

int32_t ClientConfig::loadClientConfig(const std::string &clientConfigPath)
{
    const auto yaml = YAML::LoadFile(clientConfigPath);
    if (yaml)
    {
        try
        {
            this->serverHostName = yaml["ServerHostName"].as<std::string>();
            this->serverPort = yaml["ServerPort"].as<int>();
            return 0;
        } catch (const YAML::Exception &e)
        {
            GLog::log() << "YAML loading failed: " << e.what() << std::endl;
            return -1;
        }
    }
    GLog::log() << "YAML loading failed." << std::endl;
    return -1;
}
