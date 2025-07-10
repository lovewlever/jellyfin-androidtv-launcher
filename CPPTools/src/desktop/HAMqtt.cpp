//
// Created by catog on 2025/7/1.
//

#include "desktop/HAMqtt.h"
#include "mqtt/async_client.h"
#include <iostream>

#include "desktop/HAMqttCallback.h"
#include "mqtt/message.h"

using namespace mqtt;

namespace mqtt {
    const std::string message::EMPTY_STR;
}

void HAMqtt::listen()
{
    cli.set_callback(callback);
    std::chrono::duration<std::chrono::milliseconds::rep> interval{10};
    auto connOpts = mqtt::connect_options_builder()
            .keep_alive_interval(interval)
            .clean_session()
            .automatic_reconnect(true)
            .user_name("addons")
            .password("123456")
            .finalize();

    try
    {
        const auto tokenPtr = cli.connect(connOpts);
        tokenPtr->wait();
        std::cout << "Connected==============" << std::endl;
    } catch (const mqtt::persistence_exception &exc)
    {
        std::cerr << "Persistence Error: " << exc.what() << " ["
                << exc.get_reason_code() << "]" << std::endl;
    }
    catch (const mqtt::exception &e)
    {
        std::cerr << "Exception Error: " << e.what() << " ["
                << e.get_reason_code() << "]" << std::endl;
    }
}

void HAMqtt::publish(const std::string &&payload)
{
    if (cli.is_connected())
    {
        mqtt::message_ptr pubMsg = mqtt::make_message(TOPIC_NAME, payload);
        cli.publish(pubMsg);
        std::cout << "publish msg: " << payload << std::endl;
    } else
    {
        std::cerr << "Client is not connected" << std::endl;
    }

}

void HAMqtt::disconnect()
{
    if (cli.is_connected())
    {
        cli.disconnect();
    } else
    {
        std::cerr << "Client is not connected" << std::endl;
    }
}

void HAMqtt::subscribe(const std::string &&topic)
{
    if (cli.is_connected())
    {
        cli.subscribe(topic, 1);
    }
}

void HAMqtt::message_arrived(const_message_ptr msg)
{
    std::cout << "HAMqtt::message_arrived: " << msg->get_payload_str()  << std::endl;
}
