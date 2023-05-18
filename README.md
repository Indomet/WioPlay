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

[**Wiki**](https://git.chalmers.se/courses/dit113/2023/group-10/group-10-dit113-v23-mini-project-systems-development/-/wikis/home)


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

## Technologies Used

- [Mosquitto Broker](https://mosquitto.org/) - For managing the MQTT clients and messages
- [Paho Java Client](https://www.eclipse.org/paho/index.php?page=clients/java/index.php) - A java library for interacting with the MQTT protocol
- [PubSubClient](https://github.com/knolleary/pubsubclient/releases/tag/v2.8) - An arduino library for interacting with the MQTT protocol

## Team

The Wio Play is currently being developed by the following members

|   Member        |                       Profile                       |
| :-------------: | :-------------------------------------------------: |
|     Ali         |      [Click Here](https://git.chalmers.se/almuslim) |
|     Asim        | [Click Here](https://git.chalmers.se/mehmetas)      |
|     Jackson     |     [Click Here](https://git.chalmers.se/jacniy)    |
|     Joel        |     [Click Here](https://git.chalmers.se/joelmat)   |
|     Mohamad     |     [Click Here](https://git.chalmers.se/mohamadk)  |
|     Zepei		  |    [Click Here](https://git.chalmers.se/zepei)      |
