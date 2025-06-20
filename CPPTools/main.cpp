#include "DrogonConfig.h"
#include <drogon/drogon.h>
#include <HttpGQScreensaver.h>

using namespace drogon;

int main()
{
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

    std::thread cli{
        []()
        {
            while (true)
            {
                std::this_thread::sleep_for(std::chrono::milliseconds(2000));
                HttpGQScreensaver::newInstance()->queryScreensaverImageList("127.0.0.2", 8080);
            }
        }
    };
    cli.join();

    return 0;
}
