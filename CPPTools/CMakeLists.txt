cmake_minimum_required(VERSION 3.31)

project(JellyfinCPPTools)

set(CMAKE_CXX_STANDARD 23)

include_directories(include)

include(FetchContent)

FetchContent_Declare(
        yaml-cpp
        GIT_REPOSITORY https://github.com/jbeder/yaml-cpp.git
        GIT_TAG 0.8.0 # Can be a tag (yaml-cpp-x.x.x), a commit hash, or a branch name (master)
)
FetchContent_MakeAvailable(yaml-cpp)

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
        src/ClientConfig.cpp
        include/ClientConfig.h
        src/DrogonScreensaverController.cpp
        include/DrogonScreensaverController.h)

if (WIN32)
    link_directories(${CMAKE_CURRENT_SOURCE_DIR}/libs/win)

    add_executable(${PROJECT_NAME} ${SRC_FILES})

    file(GLOB DATA_FILES ${CMAKE_CURRENT_SOURCE_DIR}/dll/*.dll)
    file(COPY ${DATA_FILES} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/)

    file(GLOB DATA_FILES ${CMAKE_CURRENT_SOURCE_DIR}/config/*.yaml)
    file(COPY ${DATA_FILES} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/config/)

    target_link_libraries(${PROJECT_NAME} PUBLIC drogon jsoncpp trantor brotlicommon brotlidec brotlienc cares zlib)

elseif (ANDROID)
    add_library(${PROJECT_NAME} SHARED ${SRC_FILES})
endif ()


target_link_libraries(${PROJECT_NAME} PUBLIC yaml-cpp::yaml-cpp)


