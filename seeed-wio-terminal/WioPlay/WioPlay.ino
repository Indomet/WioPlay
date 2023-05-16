#include "seeed_line_chart.h" // library for drawing the burndown chart
#include <map> 
#include <ArduinoJson.h> // json library
#include "Seeed_FS.h"  // SD card library
#include "UserInformation.h"
UserInformation userInformation(67, 175, 23, 0);  // (userWeight, userHeight, userAge, isMale)

#include "MotionDetection.h"
#include "MusicPlayer.h"

MusicPlayer player(2);

#include "Scenes.h"
Scenes scenes;
#include "BurndownChart.h"
#include "MqttConnection.h"

float movementValue;

MotionDetection motionDetection;
BurndownChart burndownChart;

const char *calorie_pub = "Send/Calorie/Burn/Data";

void setup() {
  Serial.begin(9600);  // Start serial communication
  setupMqtt();
   scenes.setupButton();
  while (!SD.begin(SDCARD_SS_PIN, SDCARD_SPI)) {  // setup sd
    Serial.print("ERROR sd card not recognized");
  }
  motionDetection.startAccelerator();
  burndownChart.initializeUI();

  burndownChart.updateGraphVizuals();  // menuNavigationOnPress(showBurndownChartScene, showPlayerScene); //this is here to start burndownchart in the background
}

void loop() {
  loopMqtt();

  // Exercise isn't complete yet: Continually read movement-values and update chart accordingly
  if (burndownChart.isExercising()) {

    burndownChart.controlConstraints();
     scenes.buttonOnPress();
    scenes.menuNavigationOnPress(showPlayerScene, showBurndownChartScene);

    motionDetection.recordPreviousAcceleration();  // Read previous user-position
    bool isPlayingSong = player.isPlayingSong();


    if (player.song.size() > 0 && !player.isPaused)
    {
      if (player.getPosition() >= player.song.size())
      {
        client.publish("request/notes", "I need a new set of notes");
      }
      else
      {
        player.playChunk();
      }
    }
    else
    {
      delay(1000);
    }


    float updateDelay = isPlayingSong ? player.getCurrentPauseChunkDuration() : 1000;

    burndownChart.updateTimeElapsed(updateDelay);


    // TEMPORARY - Note: Commit 'future updates' statistics
    // Serial.println("***********************");
    // Serial.println(burndownChart.getTimeElapsed());
    // Serial.println(burndownChart.getActualCaloriesPerSecond());
    // Serial.println(burndownChart.getExpectedCaloriesPerSecond());
    // Serial.println("***********************");

    movementValue = motionDetection.detectMotion();
    burndownChart.sufficientMovementInquiry(userInformation, movementValue, updateDelay);

    client.publish(calorie_pub, String(burndownChartBackEnd.getCaloriesBurnt()).c_str());
  }

  // Exercise is completed: Inactivate burndown chart and show panel
  else {
    burndownChart.displayExerciseResults();
  }
}

void showBurndownChartScene() {
  burndownChart.updateGraphVizuals();
}

void showPlayerScene() {
  scenes.playerScene();
}

