## Transportation Asset System Management (TAMS) Android Application <!---![AppIcon](/app/src/main/res/mipmap-mdpi/ic_launcher.png "AppIcon")--->

This is the Android version of the TAMS application. If you are looking for the iOS version, please click [here](https://github.com/ScribblesProject/iOS).

### Language Requirements
  1. Java
  2. XML

### Prerequisites
  1. Install JDK, Java, and Git (Github) on development machine
  2. Install Android Studio development environment
  3. Android device (for testing purposes)

### Device Setup
If you will be using an Android device for testing the application, you will have to enable *Developer Options* before you can use the device in the Android Studio development environment. Due to various configurations and manufacturers, the process of activating this option may differ. The following are instructions to get you to enable *Developer Options* on your device and get it ready for developing the application. The instructions will be based on the Google Nexus 7 (2013) running Android 7.1.

### Enable Developer Options on Your Android Device
  1. Go to *Settings* on your device
  2. Scroll down until you see *About Tablet* or *About Phone* and tap on it
  3. Scroll down until you locate *Build Number*
  4. Tap Build Number approximately seven (7) times to enable *Developer Options*. You will see a message reading the developer options has been enabled.
  5. Go back to the previous screen (by tapping on the back button of your device). You should now see a new option called *Developer Options* appear. Tap this option.
  6. Make sure the *Developer Options* toggle is set to On
  7. Scroll down until you locate *Android Debugging* or *USB Debugging*. Make sure to turn this feature On.
  8. Congratulations. You have set up your Android device and is ready for developing.

### Enable Your Device for Android Studio
Before continuing, make sure you have completed the steps above.
  1. Open Android Studio on your development machine.
  2. Connect your Android device to your development machine.
  3. You should be greeted with a pop-up message on your device. This message is to verify your device on your development machine. Tap on *Okay*. This will make it so that your Android device can be used to send the application to your device for testing.
  4. Congratulations. You have set up your Android device on your development machine. You are now ready to develop the application.

### How to Setup Android Studio
The following are instructions to setup the Android Studio development environment. Some of these steps may differ due to Operating System or version updates. These sets of instructions are done on a Windows 10 Pro machine with Android Studio version 2.2.2.
  1. Download Android Studio directly from Google using the following link: [https://developer.android.com/studio/index.html](https://developer.android.com/studio/index.html) for your Operating System.
  2. Once it has been downloaded, run the installer file. Follow the on-screen instructions to install Android Studio.
  3. Once Android Studio has been installed, there are several things that can be completed in order to be up and running developing the application.
    * **Optional**: Set up VCS (Version Control System) if you have the application on Git (or Github)
      1. Make sure Git (Github) are installed on your development machine and you should be up and running other than putting your Git credentials to update, commit, or pull the project. This will also make it easier to access the project to import it to Android Studio.

### How to Setup Application on Android Studio
The following are instructions on how to setup the application on your development machine and to become familiar with the Android Studio development environment.
  1. Download the source code of the application (in a zip format) from the following link: [https://github.com/ScribblesProject/Android](https://github.com/ScribblesProject/Android). Extract the source code in a directory that can store the application.
  2. When opening Android Studio, click on *Import Project*. This will greet you to a pop-up where you have to locate the source code. Once you locate it, click on *OK*. This should setup the application on Android Studio to begin developing the application.
  3. Once the project has been loaded, you will have a series of multiple folders and files. The following is a brief description of what certain folders and files are.
    1. Gradle: These files are for the compiler to tell it how to compile the application correctly.
    2. Manifest: This file is like the cover letter of the application. It has basic information such as application name, what permissions the application has (ie. location, microphone, and storage), the dependencies the application has, and the requirements to run the application.
    3. XML: These files are used to create the Android UI. These determine what layout and what components are on the application as well as text.
    4. Java: These files are what makes the application what it is. These files contain the logic on how the application communicates with the backend and the user.
    5. Drawable: This file contains all visual components of the application (images).
  4. Congratulations. You are now ready to develop and maintain the application.



<!---### How Setup Your Computer to Be Ready to Start Developing Project

  - Make sure to follow the repository.
  - Make sure to download the latest version of [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) onto your computer.
  - Download [Android Studio](https://developer.android.com/studio/index.html), Google's IDE for creating applications on Android.
      * Make sure that when you are installing Android Studio to download API 19, the emulator to simulate the application running on your computer, and the other plugins that the project will ask you to download.
  - **Optional**: Make sure that you have the [GitHub Desktop](https://desktop.github.com/) client installed on your computer. This makes it easier for you to visually access GitHub.
      * Make sure to import the project onto your computer either using the GitHub Desktop application or via command-line before continuing.

### How to Import the Project on Android Studio
  *Before continuing, make sure you have done everything above.*

  - Open the Android Studio IDE.
  - Make sure to click on `Import project (Eclipse, Gradle, etc.)`.
  - Look for the project under the name `Android` in your GitHub directory.
  - Click on `OK`.
  - Gradle should start setting up for the project. If it is the first time it is doing it, it should take some time so please be patient.--->
