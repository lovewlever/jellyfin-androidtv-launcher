#include "DrogonConfig.h"
#include <drogon/drogon.h>
#include <HttpGQScreensaver.h>

#include "GLog.h"

using namespace drogon;

int main()
{
    GLog::generateLogFile();
    // 读取配置文件
    if (const auto i = DrogonConfig::getInstance().loadDrogonConfig(); i < 0)
    {
        return i;
    }
    // 启动DrogonServer
    std::thread t{
        []()
        {
            const auto listenPort = DrogonConfig::getInstance().getListenPort();
            const auto listenHost = DrogonConfig::getInstance().getListenHost();
            GLog::log() << "start drogon server. listen: " << listenHost << ":" << listenPort << std::endl;
            std::cout << "start drogon server. listen: " << listenHost << ":" << listenPort << std::endl;
            app().setLogPath("./")
                    .setLogLevel(trantor::Logger::kInfo)
                    .addListener(listenHost, listenPort)
                    .setThreadNum(4)
                    //.enableRunAsDaemon()
                    .run();
        }
    };
    t.join();
    return 0;
}
