cmake_minimum_required(VERSION 3.31)

project(JellyfinCPPTools)

set(CMAKE_CXX_STANDARD 23)


message(STATUS "System name: ${CMAKE_SYSTEM_NAME}")

include(FetchContent)
FetchContent_Declare(
        yaml-cpp
        GIT_REPOSITORY https://github.com/jbeder/yaml-cpp.git
        GIT_TAG 0.8.0 # Can be a tag (yaml-cpp-x.x.x), a commit hash, or a branch name (master)
)
FetchContent_MakeAvailable(yaml-cpp)

if (WIN32)
    include_directories(include)

    set(SRC_FILES
            include/mobile/HttpGQScreensaver.h
            src/mobile/HttpGQScreensaver.cpp
            main.cpp
            jni.cpp
            src/desktop/DrogonServer.cpp
            include/desktop/DrogonServer.h
            src/desktop/DrogonConfig.cpp
            include/desktop/DrogonConfig.h
            src/GLog.cpp
            include/GLog.h
            src/desktop/DrogonScreensaverController.cpp
            include/desktop/DrogonScreensaverController.h
            src/Constants.cpp
            include/Constants.h
            src/mobile/ScreensaverImageList.cpp
            include/mobile/ScreensaverImageList.h
            src/desktop/HAOperate.cpp
            include/desktop/HAOperate.h
            src/desktop/HAMqtt.cpp
            include/desktop/HAMqtt.h
            src/desktop/WeatherAMapApi.cpp
            include/desktop/WeatherAMapApi.h
            include/httplib.h
            include/desktop/IWeatherApi.h
            include/desktop/WeatherData.h
            src/desktop/DrogonWeatherController.cpp
            include/desktop/DrogonWeatherController.h
            include/desktop/HAMqttCallback.h
            src/desktop/HAMqttCallback.cpp

    )
    message("Platform: " "WIN32")
    message("IS_DEBUG: " ${CMAKE_BUILD_TYPE})

    if (${CMAKE_BUILD_TYPE} STREQUAL "Debug")
        link_directories(${CMAKE_CURRENT_SOURCE_DIR}/libs/win/debug)
    else ()
        link_directories(${CMAKE_CURRENT_SOURCE_DIR}/libs/win/release)
    endif ()

    add_executable(${PROJECT_NAME} ${SRC_FILES})

    file(GLOB DATA_FILES ${CMAKE_CURRENT_SOURCE_DIR}/config/*.yaml)
    file(COPY ${DATA_FILES} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/config/)

    if (${CMAKE_BUILD_TYPE} STREQUAL "Debug")
        file(GLOB DATA_FILES ${CMAKE_CURRENT_SOURCE_DIR}/dll/debug/*.dll)
        file(COPY ${DATA_FILES} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/)
        target_link_libraries(${PROJECT_NAME} PUBLIC drogon jsoncpp trantor brotlicommon brotlidec brotlienc cares
                zlibd
                paho-mqtt3a
                paho-mqtt3as
                paho-mqtt3c
                paho-mqtt3cs
                paho-mqttpp3
                libssl
                libcrypto
        )
    else ()
        file(GLOB DATA_FILES ${CMAKE_CURRENT_SOURCE_DIR}/dll/release/*.dll)
        file(COPY ${DATA_FILES} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/)
        target_link_libraries(${PROJECT_NAME} PUBLIC drogon jsoncpp trantor brotlicommon brotlidec brotlienc cares
                zlib
                paho-mqtt3a
                paho-mqtt3as
                paho-mqtt3c
                paho-mqtt3cs
                paho-mqttpp3
                libssl
                libcrypto

        )
    endif ()

elseif (ANDROID)
    message("Platform: " "ANDROID")
    include_directories(include)
    find_library(log-lib log)
    add_library(${PROJECT_NAME} SHARED include/GLog.h src/GLog.cpp
            include/httplib.h
            include/mobile/HttpGQScreensaver.h
            src/mobile/HttpGQScreensaver.cpp
            src/Constants.cpp
            include/Constants.h
            src/mobile/ScreensaverImageList.cpp
            include/mobile/ScreensaverImageList.h
            jni.cpp
            )

    target_link_libraries(${PROJECT_NAME} PUBLIC ${log-lib})

    add_custom_command(TARGET ${CMAKE_PROJECT_NAME} POST_BUILD
		        COMMAND "${CMAKE_STRIP}" --strip-unneeded "$<TARGET_FILE:${CMAKE_PROJECT_NAME}>"
    )
endif ()

target_link_libraries(${PROJECT_NAME} PUBLIC yaml-cpp::yaml-cpp)

