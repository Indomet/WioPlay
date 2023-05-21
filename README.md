![Android CI](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/badges/main/pipeline.svg?job=androidBuild&key_text=Android+CI&key_width=80)
![Arduino CI](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/badges/main/pipeline.svg?job=build-sketch&key_text=Arduino+CI&key_width=80)
![NoteParser CI](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/badges/main/pipeline.svg?job=parserBuild&key_text=NoteParser+CI&key_width=100)

<div align="center">

<img src="https://egeniq.com/wp-content/uploads/2022/10/1_RimJezQmCVqfqxy4qxXfPA.gif" width="425" />

# Wio Play

</div>

**Table of Contents:**

[[_TOC_]]

## Purpose

The purpose of Wio Play is motivating people to lose calories by effectively utilising the terminal's components and the user experience of the android app

## Benefits

- Large Song Library 🎵

  - **50+** **songs** currently available

- Maintainbility 🔄

  - Songs are automatically **maintained on the server**

- Media Storage 💾

  - Downloaded songs are **cached on the app**

- Motion Detection 👟
  - Utilizes the **small form factor** of the wio terminal

<!-- [**Wiki**](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/home) -->

## How It Works

The idea is to have a compact device that can stream music from a source, or load it from its existing memory, that combined with a capable player and a motion detection mechanism make the **Wio Play** a handy piece of technology.

### Steps

The full process of streaming a track on our service can be broken down into 3 key steps interconnected together using MQTT, the full process eventually concludes when the chosen song has been played fully on the terminal

1. Pinging Terminal

   - Happens once when the user requests to play a specific song, this will put the terminal in `Note Requesting` state

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

- [Software Architecture](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/Software-Architecture)
- [Diagrams](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/Requirements/Diagrams)


## Setup

The system cosists of 3 main components, the **Android application**, the **wio terminal**, and the **Note-Parser** server that is responsible for providing and parsing the song library. All three components must be set up correctly in order for the system to function properly.

In order to get all the project files, you must clone the project repo:

`git clone https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development.git`

### Android App 

#### - Using Gradle ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

Assuming you already have a device or emulator attached, you can build the application using gradle.
 1. `gradlew assembleDebug` to build a debug apk which can then be installed 
 2. `gradlew installDebug` to build and immediately install the apk onto the attached device

#### - Using Android Studio ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)

To install the Android app on your personal device, please make sure to have [_Android Studio Flamingo or newer_](https://developer.android.com/studio) installed on your machine.

- Navigate to the File menu, then open the project from the AndroidApp folder.
- Connect your device to the computer, alternatively you can install an emulator with the help of the android studio device manager.
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

Compiling and uploading the arduino code to your Wio Terminal.
#### - Using Arduino-CLI ![Arduino](https://img.shields.io/badge/-Arduino-00979D?style=for-the-badge&logo=Arduino&logoColor=white)

1. `arduino-cli compile -b <fqbn> WioPlay/` to compile the workspace
2. `arduino-cli upload -p <port> -b <fqbn> WioPlay/` to upload the compiled workspace onto the device

*Note: **&lt;fqbn&gt;** and **&lt;port&gt;** correspond to the device fqbn number and usb connection port respectively*

#### - Using IDE
- In the Arduino IDE, navigate to: **group-10-dit113-v23-mini-project-systems-development/seeed-wio-terminal/WioPlay** and open the folder.
- Connect the Wio Seeed Terminal to your computer.
- Plug in the grove speaker to **port 1** on the Wio Seeed Terminal.
- Create a header file named "**WifiInformation.h**", copy paste and fill in each field in the following format:

`#define SSID "Wifi-name-here"`

`#define PASSWORD "Password-here"`

`#define my_IPv4 "Broker-adress-here"`


- Go to line 9 in MqttConnection.h and uncomment `"#include "WifiInformation.h"`
- Compile and upload the sketch to the Wio Seeed Terminal.
- If you have the battery pack attached, now it is safe to unplug, otherwise please attach the battery pack.

### Using the system

After completing the installation process, you can start using the system by first launching the note parser and the android app. The app should automatically send a message to the server and receive all the available songs, as well as storing them to the phone's storage at /Documents. the three files created should be songList.json, user.json, and workoutManager.json.

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
| :-----: | :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------: |
|   Ali   |Establsihed MQTT conncetion for andorid phone<br>Created modern UI for settings and workout tab<br>Helped setup CI for the android phone<br>Created the JSON MQTT message sending format<br>Created fucntioanlity behind the workouttab and the updating of user information tab<br>Created many diagrams in the wiki<br>Contributed to requirement tracing and writing<br>Wrote workout backend logic tests  <br> Created observer pattern <br> create reusable util methods | [Click Here](https://git.chalmers.se/almuslim) |
|  Asim   |Set up Arduino CLI.<br>Implemented detection of movement in all three axes on the wio terminal.<br>Implemented data receiving/sending functionality via MQTT on WIO terminal<br>Fixed the bug that was causing the app to crash on Android devices with API 31 or higher.<br>Implemented the display of current song titlecon the WIO terminal.<br>Added basic playback controls such as play, resume, skip, and previous button clicking functionality and visualisation to the UI.<br>Fixed the bug where the Wio Terminal displays a flat line after a certain amount of time on the burndown chart scene.<br>Fixed the bug causing the music to slow down during the transition to the burndown chart scene.<br>Implemented the feature to display an image from the SD card when playing a song and ensured that the image is automatically updated when a new song is played.<br>Created diagrams in the wiki                                                                                                                                                                                                                                                                                                                                                                                                                       | [Click Here](https://git.chalmers.se/mehmetas) |
| Jackson |     Helped refactor broker connection class to be singleton <br> Added popup to edit the username and save it  <br> added viewing the username in fragments                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |  [Click Here](https://git.chalmers.se/jacniy)  |
|  Joel   |                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | [Click Here](https://git.chalmers.se/joelmat)  |
| Mohamad | Create the algorithm for the music parser<br/>Create the MQTT connection between parser and android app<br/>Get a list of 50+ songs to parse<br/>Get metadata for each song (tempo, album art, etc…)<br/>Write tests for the music parser<br/>Add CI for the note-parser building + testing<br/>Contributed to the android CI<br/>Add music-player class to the arduino<br/>Come up with the request/receive architecture for streaming music<br/>Add sequence diagram for the architecture<br/>Format and add content to the README<br/>Contribute to bug fixes and refactors | [Click Here](https://git.chalmers.se/mohamadk) |
|  Zepei  |  Implemented backend for music library tab<br>Implemented UI for music library tab<br>Created User, Song, and songlist logic<br>Implemented reading and writing of User data<br>Implemented reading and writing of Song list<br>Reworked bottom navigation and music library UI<br>Implemented calorie credits earning and spending logic<br>Implemented unlocking songs<br>Worked on refactoring the code of the whole Android app<br>Refactored classes into singleton pattern<br>Implemented receiving and loading of song data through MQTT<br>Worked on requirements traceability<br>Worked on cycling through unlocked song list with left and right buttons on the terminal<br>Fixed many bugs<br>Added android instrumented tests <br>Helped with setting up CI.|[Click Here](https://git.chalmers.se/zepei)   |
 

 ## Demo Video link
[Group 10 WioPlay Demo](https://youtu.be/E4AoKy7i2mA)

## Support
[User manual](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/User-Manual)

