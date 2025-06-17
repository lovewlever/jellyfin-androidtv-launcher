//
// Created by catog on 2025/6/17.
//

#ifndef CLIENTCONFIG_H
#define CLIENTCONFIG_H
#include <cstdint>
#include <string>
#include <memory>


class ClientConfig
{
public:
    ClientConfig(const ClientConfig &) = delete;

    ClientConfig &operator=(const ClientConfig &) = delete;

    static ClientConfig &getInstance();

    int32_t loadClientConfig(const std::string &clientConfigPath);

    [[nodiscard]] std::string getServerHostName() const { return serverHostName; }
    [[nodiscard]] int32_t getServerPort() const { return serverPort; }

private:
    std::string serverHostName{};
    int32_t serverPort{0};

    ClientConfig() = default;

    ~ClientConfig() = default;
};


#endif //CLIENTCONFIG_H
