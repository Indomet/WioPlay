#include "seeed_line_chart.h"  // library for drawing the burndown chart
#include <map>
#include <ArduinoJson.h>  // json library
#include "Seeed_FS.h"     // SD card library
#include "UserInformation.h"
UserInformation userInformation(67, 175, 23, 0);  // (userWeight, userHeight, userAge, isMale)

#include "MotionDetection.h"
#include "MusicPlayer.h"


MusicPlayer player(2);

#include "Scenes.h"
Scenes scenes;
#include "BurndownChart.h"
#include "MqttConnection.h"
#include "Button.h"
Button button;
float movementValue;

MotionDetection motionDetection;
BurndownChart burndownChart;


void setup() {
  Serial.begin(9600);  // Start serial communication
  setupMqtt();
  button.setup();

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
    button.onPress();
    scenes.menuNavigationOnPress(showPlayerScene, showBurndownChartScene);

    motionDetection.recordPreviousAcceleration();  // Read previous user-position
    bool isPlayingSong = player.isPlayingSong();


    if (player.song.size() > 0 && !player.hasRequested) {
      if (player.getPosition() >= player.song.size()) {
        Serial.println(player.getPosition());
        client.publish(Request_pub, "I need a new set of notes");
        player.hasRequested = true;
      } else {
        player.playChunk();
      }
    } else {
      delay(100);
    }


    float updateDelay = 100;
    // burndownChart.updateTimeElapsed(1000); // player.getCurrentPauseChunkDuration()

    burndownChart.updateTimeElapsed(updateDelay);


    // TEMPORARY - Note: Commit 'future updates' statistics
    // Serial.println("***********************");
    // Serial.println(burndownChart.getTimeElapsed());
    // Serial.println(burndownChart.getActualCaloriesPerSecond());
    // Serial.println(burndownChart.getExpectedCaloriesPerSecond());
    // Serial.println("***********************");

    movementValue = motionDetection.detectMotion();  // Read current user-position

    // burndownChart.sufficientMovementInquiry(userInformation, movementValue, 1000); // player.getCurrentPauseChunkDuration() -------------> Add if-statement for this case
    burndownChart.sufficientMovementInquiry(userInformation, movementValue, updateDelay);

    client.publish(calorie_pub, String(burndownChartBackEnd.getCaloriesBurnt()).c_str());
  }

  // Exercise is completed: Inactivate burndown chart and show panel
  else {
    burndownChart.displayExerciseResults();
    delay(200); // to make the scene flicker less
  }
  
}

void showBurndownChartScene() {
  burndownChart.updateGraphVizuals();
}

void showPlayerScene() {
  scenes.playerScene();
}
