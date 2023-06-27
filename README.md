[![Arduino CI](https://github.com/Indomet/WioPlay/actions/workflows/github-Arduino-CI.yml/badge.svg)](https://github.com/Indomet/WioPlay/actions/workflows/github-Arduino-CI.yml)
[![Android CI](https://github.com/Indomet/WioPlay/actions/workflows/github-Android-CI.yml/badge.svg)](https://github.com/Indomet/WioPlay/actions/workflows/github-Android-CI.yml)
[![NoteParser CI](https://github.com/Indomet/WioPlay/actions/workflows/main.yml/badge.svg)](https://github.com/Indomet/WioPlay/actions/workflows/main.yml)

<div align="center">

<img src="https://egeniq.com/wp-content/uploads/2022/10/1_RimJezQmCVqfqxy4qxXfPA.gif" width="425" />

# Wio Play

</div>

## Purpose

The purpose of Wio Play is motivating people to lose calories by effectively utilising the terminal's components and the user experience of the android app

## Benefits

- Large Song Library 🎵

  - **50+** **songs** currently available

- Maintainability 🔄

  - Songs are automatically **maintained on the server**

- Media Storage 💾

  - Downloaded songs are **cached on the app**

- Motion Detection 👟
  - Utilizes the **small form factor** of the WIO terminal

<!-- [**Wiki**](https://github.com/Indomet/WioPlay/wiki) -->

## How It Works

The idea is to have a compact device that can stream music from a source, or load it from its existing memory, that combined with a capable player and a motion detection mechanism make the **Wio Play** a handy piece of technology.

### Steps

The process of streaming a track on our service can be broken down into 3 key steps interconnected together using MQTT, the full process eventually concludes when the chosen song has been played fully on the terminal

1. Pinging Terminal

   - Happens once when the user requests to play a specific song, this will put the terminal in the `Note Requesting` state

2. Requesting Notes

   - During the `Note Requesting` state, the terminal will ask the app for more musical notes whenever the current chunk of notes has been played fully and exhausted

3. Sending Notes
   - Once the app receives a request from the terminal asking for more notes, it sends the next notes chunk in the queue back to the terminal

### Diagram

The steps described above can be seen in the sequence diagram below:

<div align="center">
<br />
<img src="https://lh3.googleusercontent.com/drive-viewer/AFGJ81p9zU6DBnY59_8D82sz5SMo3yR0VnWb_HecgOrcSK-aTqkYeohW5WBslyrLSCfYT-ftaoWi8t12STy1MtWIQBnjGfiJeg=s2560" />
</div>

For more detailed diagrams about the system please refer to the following wiki pages

- [Software Architecture](https://github.com/Indomet/WioPlay/wiki/Software-Architecture)
- [Diagrams](https://github.com/Indomet/WioPlay/wiki/Diagrams)


## Setup

The system consists of 3 main components, the **Android application**, the **WIO terminal**, and the **Note-Parser** server that is responsible for providing and parsing the song library. All three components must be set up correctly for the system to function properly.

In order to get all the project files, you must clone the project repo:

`git clone git@github.com:Indomet/WioPlay.git`

### Android App 

#### - Using Gradle ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

Assuming you already have a device or emulator attached, you can build the application using gradle.
 1. `gradlew assembleDebug` to build a debug apk which can then be installed 
 2. `gradlew installDebug` to build and immediately install the apk onto the attached device

#### - Using Android Studio ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)

To install the Android app on your personal device, please make sure to have [_Android Studio Flamingo or newer_](https://developer.android.com/studio) installed on your machine.

- Navigate to the File menu, then open the project from the AndroidApp folder.
- Connect your device to the computer, alternatively you can install an emulator with the help of the Android Studio device manager.
- At the top of the screen, make sure to select your device
- Click the run icon, while making sure the phone is open. A popup should appear asking for permission to install the app, press yes. Now the phone can be unplugged from the computer and the app will function as a normal Android application.

<img src="https://i.imgur.com/OiqdqAr.png" width="425" />

### Note Parser
The NoteParser was developed with maven, which means you can use the pom.xml file thats present in the repo to build, verify, and execute the project.

#### - Using Maven ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

Assuming you have maven installed and present in your path
1. `mvn install` to build a copy of the project (includes JAR)
2. `mvn exec:java` to directly execute the MainClass and run the application

### Wio Terminal

Compiling and uploading the Arduino code to your Wio Terminal.
#### - Using Arduino-CLI ![Arduino](https://img.shields.io/badge/-Arduino-00979D?style=for-the-badge&logo=Arduino&logoColor=white)

1. `arduino-cli compile -b <fqbn> WioPlay/` to compile the workspace
2. `arduino-cli upload -p <port> -b <fqbn> WioPlay/` to upload the compiled workspace onto the device

*Note: **&lt;fqbn&gt;** and **&lt;port&gt;** correspond to the device fqbn number and USB connection port respectively*

#### - Using IDE
- In the Arduino IDE, navigate to: **group-10-dit113-v23-mini-project-systems-development/seeed-wio-terminal/WioPlay** and open the folder.
- Connect the Wio Seeed Terminal to your computer.
- Plug in the grove speaker to **port 1** on the Wio Seeed Terminal.
![IMAGE_DESCRIPTION](https://i.imgur.com/IELVBrE.png)
- Create a header file named "**WifiInformation.h**", copy paste and fill in each field in the following format:

`#define SSID "Wifi-name-here"`

`#define PASSWORD "Password-here"`

`#define my_IPv4 "Broker-adress-here"`


- Go to line 9 in MqttConnection.h and uncomment `"#include "WifiInformation.h"`
- Compile and upload the sketch to the Wio Seeed Terminal.
- If you have the battery pack attached, now it is safe to unplug, otherwise please attach the battery pack.

### Using the system

After completing the installation process, you can start using the system by first launching the note parser and the Android app. The app should automatically send a message to the server and receive all the available songs, as well as storing them to the phone's storage at /Documents. the three files created should be songList.json, user.json, and workoutManager.json.

After that, turn on the Wio terminal, if you are a first time user, you must navigate to the settings page in the app and enter your information, which is used to provide you with your personal workout data. Then navigate to the workout page and pick a workout you would like to do, choose the calorie goal and duration, and press start.

As you exercise while holding the Wio terminal, it will detect your movements and calculate the amount of calories you are burning, in the bottom of the workout page, you can see real time statistics of the calories burnt, which will also be converted to a 1:1 ratio into calorie credits.

In the music library page, you can spend the calorie credits you earned from burning calories to unlock new songs. After unlocking a song, a play button will appear for that specific song, by pressing it, the Wio terminal will begin to play that song.

While exercising, you can press and hold the joystick to view the real-time burn-down chart, which shows your current calorie burning rate as well as the estimated line which you should try and keep up with in order to reach your calorie goal within the workout duration.

While playing music, you can use the left and right button to cycle through all the songs that you currently have unlocked, and the middle button to pause/resume the song.

All progress and user data is kept until the app is deleted from the phone.

Have fun!

## Contributions

The Wio Play is currently receiving contributions from the following members

| Member  |                                                                                                                                                                                                                               Contributions                                                                                                                                                                                                                               |                    Profile                     |
| :-----: | :-- | :--------------------------------------------: |
|   Ali   |Establsihed MQTT connection for Android phone<br>Created modern UI for settings and workout tab<br>Helped setup CI for the Android phone<br>Created the JSON MQTT message sending format<br>Created functionality behind the workout tab and the updating of user information tab<br>Created many diagrams in the wiki<br>Contributed to requirement tracing and writing<br>Wrote workout backend logic tests  <br> Created observer pattern <br> create reusable util methods | [Click Here](https://github.com/BigMan360) |
|  Asim   |Set up Arduino CLI.<br>Implemented detection of movement in all three axes on the WIO terminal.<br>Implemented data receiving/sending functionality via MQTT on WIO terminal<br>Fixed the bug that was causing the app to crash on Android devices with API 31 or higher.<br>Implemented the display of current song title on the WIO terminal.<br>Added button clicking functionalities such as switch scene, play, resume, next, and previous button.<br>Added music UI with visualisation of buttons and image.<br>Fixed the bug where the burndown chart displays a flat line after a certain amount of time.<br>Fixed the bug causing the music to slow down during the transition to the burndown chart scene.<br>Implemented the display of an image from the SD card when playing a song.<br>Implemented automatic image update when a new song is played.<br>Refactored menu panel with 2 cases (accomplish/fail goal) to make it look prettier on the WIO terminal<br>Created component diagram in the wiki.<br>Divided the received topic values from json and passed them to the relevant function for calculating burnt calories.                                                                                                                                                                                                                                                                                                                                                                                                                      | [Click Here](https://github.com/Indomet) |
| Jackson |     Helped refactor broker connection class to be singleton <br> Added popup to edit the username and save it  <br> Added viewing the username in fragments<br> Added functionality to restrict to a specific range while user saves required personal information <br>Added search bar and underlying functionality to quickly access desired song.                                                                                                                                                                                                                                                                                                                                                                                                                                                               |  [Click Here](https://git.chalmers.se/jacniy)  |
|  Joel   | Created the composite BurndownChart.h and its components BurndownChartBackEnd.h and BurndownChartFrontEnd.h.<br> Created the logic behind the burndown chart (the X-axis or time elapsed, the Y-axis or current calories burnt and the blue line's slope that ends in the coordinates (exerciseDuration, calorieGoal) are subscribed to the values sent from the application) <br> Created UserInformation.h whose single responsibility is to store the physical attributes having an impact on the burnt calories <br> Created the menu panel with 2 cases (accomplish/fail goal) with that displays a comparison between the actual- vs expected calories burnt per second throughout the workout <br> Created the unlocked songs list that uses binary search insertion by comparing the title of the song to be inserted (the unlocked songs are alphabetically sorted) <br> Created the feature of switching between unlocked songs by clicking on two blue terminal buttons (previous, next)                                                                                                                                                                                                                                                                                                                                                                                                                                                             | [Click Here](https://github.com/mrjex)  |
| Mohamad | Create the algorithm for the music parser<br/>Create the MQTT connection between parser and Android app<br/>Get a list of 50+ songs to parse<br/>Get metadata for each song (tempo, album art, etc…)<br/>Write [wiki](https://github.com/Indomet/WioPlay/wiki/Note-Parser) section about NoteParser<br/>Write tests for the music parser<br/>Add CI for the note-parser building + testing<br/>Contributed to the Android CI<br/>Add music-player class to the Arduino<br/>Come up with the request/receive architecture for streaming music<br/>Add sequence diagram for the architecture<br/>Format and add content to the README<br/>Contribute to bug fixes and refactors | [Click Here](https://github.com/Chef03) |
|  Zepei  |  Implemented backend for music library tab<br>Implemented UI for music library tab<br>Created User, Song, and songlist logic<br>Implemented reading and writing of User data<br>Implemented reading and writing of Song list<br>Reworked bottom navigation and music library UI<br>Implemented calorie credits earning and spending logic<br>Implemented unlocking songs<br>Worked on refactoring the code of the whole Android app<br>Refactored classes into singleton pattern<br>Implemented receiving and loading of song data through MQTT<br>Worked on requirements traceability<br>Worked on cycling through unlocked song list with left and right buttons on the terminal<br>Fixed many bugs<br>Added Android instrumented tests <br>Helped with setting up CI.|[Click Here](https://github.com/Zepeiz)   |
 

 ## Demo Video link
[Group 10 WioPlay Demo](https://youtu.be/E4AoKy7i2mA)

## Support
[User manual](https://github.com/Indomet/WioPlay/wiki/User-Manual)

