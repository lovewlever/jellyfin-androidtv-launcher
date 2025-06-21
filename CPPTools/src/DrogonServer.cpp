//
// Created by catog on 2025/6/17.
//

#include "../include/DrogonServer.h"

#include "DrogonConfig.h"
#include "GLog.h"
#include <thread>

#include "drogon/HttpAppFramework.h"

int32_t DrogonServer::startServer(const bool threadDetach) const
{
    GLog::generateLogFile();
    // 读取配置文件
    if (const auto i = DrogonConfig::getInstance().loadDrogonConfig(); i < 0)
    {
        return i;
    }

    // 启动DrogonServer
    std::thread t{
        []
        {
            const auto listenPort = DrogonConfig::getInstance().getListenPort();
            const auto listenHost = DrogonConfig::getInstance().getListenHost();
            GLog::log() << "start drogon server. listen: " << listenHost << ":" << listenPort << std::endl;
            std::cout << "start drogon server. listen: " << listenHost << ":" << listenPort << std::endl;
            drogon::app().setLogPath("./")
                    .setLogLevel(trantor::Logger::kInfo)
                    .addListener(listenHost, listenPort)
                    .setThreadNum(4)
                    //.enableRunAsDaemon()
                    .run();
        }
    };
    if (!threadDetach)
    {
        t.join();
    } else
    {
        t.detach();
    }
    return 0;
}
