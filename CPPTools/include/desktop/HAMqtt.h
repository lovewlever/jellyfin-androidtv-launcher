//
// Created by catog on 2025/7/1.
//

#ifndef HAMQTT_H
#define HAMQTT_H

#include "HAMqttCallback.h"
#include "mqtt/async_client.h"
using namespace mqtt;

/**
 * 测试订阅：.\mosquitto_sub.exe -h 127.0.0.1 -t zigbee2mqtt -u addons -P 123456
 * 测试发布：.\mosquitto_pub.exe -h 127.0.0.1 -t zigbee2mqtt -u addons -P 123456 -m "Hello"
 */
class HAMqtt
{
public:
    HAMqtt(): callback{this} {}
    ~HAMqtt() = default;
    HAMqttCallback callback;
    const std::string ADDRESS{"mqtt://127.0.0.1:1883"};
    const std::string CLIENT_ID{""};
    const std::string TOPIC_NAME{"zigbee2mqtt"};
    mqtt::async_client cli{ADDRESS, CLIENT_ID};

    void listen();

    void publish(const std::string &&payload);

    void disconnect();

    void subscribe(const std::string &&topic);

    void message_arrived(const_message_ptr msg);
};



#endif //HAMQTT_H
