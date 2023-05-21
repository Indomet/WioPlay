<div align="center">


<img src="https://egeniq.com/wp-content/uploads/2022/10/1_RimJezQmCVqfqxy4qxXfPA.gif" width="425" />

# Wio Play
</div>

**Table of Contents:**

[[_TOC_]]

## Description

Wio Play is a media device that can seamlessly track workout progress and play music to get you motivated.

## Purpose

The purpose of Wio Play is motivating people to lose calories by effectively utilising the terminal's components and the user experience of the android app


## Benefits

* Large Song Library 🎵
   - **50+** **songs** currently available

* Maintainbility 🔄
   - Songs are automatically **maintained on the server**

* Media Storage 💾
  - Downloaded songs are **cached on the app**

* Motion Detection 👟
  - Utilizes the **small form factor** of the wio terminal

<!-- [**Wiki**](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/home) -->

## Streaming Music
The idea is to have a compact device that can stream music from a source, or load it from its existing memory, that combined with a capable player and a motion detection mechanism make the **Wio Play** a handy piece of technology.

### Proccess
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

* [Software Architecture](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/Software-Architecture)
* [Diagrams](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/Requirements/Diagrams)


## Contributions

The Wio Play is currently receiving contributions from the following members

|   Member        | Contributions                |                       Profile                       |
| :-------------: | :--------------------------: | :-------------------------------------------------: |
|     Ali      |<ul><li>Establsihed MQTT conncetion for andorid phone</li><li>Created modern UI for settings and workout tab</li><li>Helped setup CI for the android phone</li></ul><ul><li>Created the JSON MQTT message sending format</li><li>Created fucntioanlity behind the workouttab and the updating of user information tab</li><li>Created many diagrams in the wiki</li></ul><ul><li>Contributed to requirement tracing and writing</li><li>Wrote workout backend logic tests         |      [Click Here](https://git.chalmers.se/almuslim) |
|     Asim        |                              | [Click Here](https://git.chalmers.se/mehmetas)      |
|     Jackson     |                              |     [Click Here](https://git.chalmers.se/jacniy)    |
|     Joel        |                              |     [Click Here](https://git.chalmers.se/joelmat)   |
|     Mohamad     |                              |     [Click Here](https://git.chalmers.se/mohamadk)  |
|     Zepei		    |                              |    [Click Here](https://git.chalmers.se/zepei)      |

## User Manual
This manual aims to guide the user step by step in the installation stage, setup, and how to use the system.
### Installation
The system cosists of 3 main components, the Android application, the wio terminal, and the noteparser backend server that is responsible for providing the song library. All three components must be set up correctly in order for the system to properly function.

### Software needed
- Java SDK
- Android Studio Flamingo or newer
- Arduino IDE
- An online/local MQTT broker

### Hardware needed
- Computer connected to wifi
- Android phone running minimum API 30
- Wio terminal & battery pack attachment
- grove speaker module & 4pin connection wire

### Important
[- Make sure that all three compoenents are connected to the same MQTT broker -] 

**Please start by cloning the latest source code from main branch on to your local machine.**
#### Android app
To install the Android app on your personal device, please make sure to have [_Android Studio Flamingo or newer_](https://developer.android.com/studio) installed on your machine.

- Navigate to the File menu, then open the project from the AndroidApp folder.
- Connect your device to the computer.
- At the top of the screen, make sure to select your device
- Click the run icon, while making sure the phone is open. A popup should appear asking for permission to install the app, press yes. Now the phone can be unplugged from the computer and the app will function as a normal Android application.

<img src="https://i.imgur.com/OiqdqAr.png" width="425" />

#### Wio terminal
Compiling and uploading the arduino code to your Wio Terminal.
- In the Arduino IDE, navigate to: **group-10-dit113-v23-mini-project-systems-development/seeed-wio-terminal/WioPlay** and open the folder.
- Connected the Wio Seeed Terminal to your computer.
- Compile and upload the sketch to the Wio Seeed Terminal.
- If you have the battery pack attached, now it is safe to unplug, otherwise please attach the battery pack.
- plug in the grove speaker to one of the 4 pin ports on the Wio Seeed Terminal.

//Insert images

#### Note Parser 
The noteparser backend server is needed for the first time setup of the system, its purpose is to send the entire song library to the app through MQTT. After first time setup this is no longer needed, except when the user deletes the app and wants to reinstall the system.

- Add installation steps


### Using the system
After completing the installation process, you can start using the system by first launching the note parser and the android app. The app should automatically send a message to the server and receive all the available songs, as well as storing them to the phone's storage at /Documents. the three files created should be songList.json, user.json, and workoutManager.json.

After that, turn on the Wio terminal, if you are a first time user, you must navigate to the settings page in the app and enter your information, which is used to provide you with your personal workout data. Then navigate to the workout page and pick a workout you would like to do, choose the calorie goal and duration, and press start.

As you exercise while holding the Wio terminal, it will detect your movements and calculate the amount of calories you are burning, in the bottom of the workout page, you can see real time statistics of the calories burnt, which will also be converted to a 1:1 ratio into calorie credits. 

In the music library page, you can spend the calorie credits you earned from burning calories to unlock new songs. After unlocking a song, a play button will appear for that specific song, by pressing it, the Wio terminal will begin to play that song.

While exercising, you can press and hold the joystick to view the real-time burn-down chart, which shows your current calorie burning rate as well as the estimated line which you should try and keep up with in order to reach your calorie goal within the workout duration.

While playing music, you can use the left and right button to cycle through all the songs that you currently have unlocked, and the middle button to pause/resume the song.

All progress and user data is kept until the app is deleted from the phone.

Have fun!


