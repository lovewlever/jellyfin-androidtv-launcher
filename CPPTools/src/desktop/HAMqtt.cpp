//
// Created by catog on 2025/7/1.
//

#include "../../include/desktop/HAMqtt.h"
#include "mqtt/async_client.h"


void HAMqtt::listen()
{
    const auto ADDRESS{"mqtt://<host>:<port>"};
    const auto CLIENT_ID{"JellyfinCppToolsHAMqtt"};
    mqtt::async_client cli{ADDRESS, CLIENT_ID};
    mqtt::callback cb;
    cli.set_callback(cb);
    std::chrono::duration<std::chrono::milliseconds::rep> interval{1};
    auto connOpts = mqtt::connect_options_builder()
        .keep_alive_interval(interval)
        .clean_session().finalize();

    try
    {
        cli.connect(connOpts);

        mqtt::message_ptr pubMsg = mqtt::make_message("PAYLOAD1", "Hello");
        cli.publish(pubMsg);
        cli.disconnect();
    }catch (const mqtt::persistence_exception& exc) {
        std::cerr << "Persistence Error: " << exc.what() << " ["
            << exc.get_reason_code() << "]" << std::endl;
    }
    catch (const mqtt::exception& e)
    {
        std::cerr << "Exception Error: " << e.what() << " ["
                    << e.get_reason_code() << "]" << std::endl;
    }
}
