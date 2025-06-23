<h1 align="center">Jellyfin Android TV</h1>
<h3 align="center">Forked from<a href="https://github.com/jellyfin/jellyfin-androidtv">jellyfin/jellyfin-androidtv</a></h3>

## Differentiation

 - Added the function of setting as Launcher
 - Added jump to system settings icon
 - Add APP list page
 - Added Screen saver to display the images in the directory specified by the server, and JellyfinCPPTools.exe to implement the server
   - When ```ScreensaverFolderPath``` is empty or the ```JellyfinCPPTools``` service is not started, the default Screen saver will be displayed.
   - ```config/drogon-config.yaml ```
   - The port number is jellyfin server + 1. The port number of Jellyfin Server is 8096, so please fill in 8097 for ListenPort of ```JellyfinCPPTools``` config.

## Build Process

### Dependencies

- Android Studio

### Build

1. Clone or download this repository

   ```sh
   git clone https://github.com/jellyfin/jellyfin-androidtv.git
   cd jellyfin-androidtv
   ```

2. Open the project in Android Studio and run it from there or build an APK directly through Gradle:

   ```sh
   ./gradlew assembleDebug
   ```
   
   Add the Android SDK to your PATH environment variable or create the ANDROID_SDK_ROOT variable for
   this to work.

### Deploy to device/emulator

   ```sh
   ./gradlew installDebug
   ```

*You can also replace the "Debug" with "Release" to get an optimized release binary.*
