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

    set(SRC_FILES src/library.cpp
            src/HttpGQScreensaver.cpp
            include/HttpGQScreensaver.h
            main.cpp
            jni.cpp
            src/DrogonServer.cpp
            include/DrogonServer.h
            src/DrogonConfig.cpp
            include/DrogonConfig.h
            src/GLog.cpp
            include/GLog.h
            src/DrogonScreensaverController.cpp
            include/DrogonScreensaverController.h)
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
        target_link_libraries(${PROJECT_NAME} PUBLIC drogon jsoncpp trantor brotlicommon brotlidec brotlienc cares zlibd)
    else ()
        file(GLOB DATA_FILES ${CMAKE_CURRENT_SOURCE_DIR}/dll/release/*.dll)
        file(COPY ${DATA_FILES} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/)
        target_link_libraries(${PROJECT_NAME} PUBLIC drogon jsoncpp trantor brotlicommon brotlidec brotlienc cares zlib)
    endif ()

elseif (ANDROID)
    message("Platform: " "ANDROID")
    include_directories(include)
    find_library(log-lib log)
    add_library(${PROJECT_NAME} SHARED include/GLog.h src/GLog.cpp
            include/httplib.h
            include/HttpGQScreensaver.h
            src/HttpGQScreensaver.cpp
            jni.cpp
            )

    target_link_libraries(${PROJECT_NAME} PUBLIC ${log-lib})
endif ()

target_link_libraries(${PROJECT_NAME} PUBLIC yaml-cpp::yaml-cpp)

