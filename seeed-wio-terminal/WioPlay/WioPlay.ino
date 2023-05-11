#include "seeed_line_chart.h"
#include <map>
#include <ArduinoJson.h>
#include "Seeed_FS.h"  // SD card library
#include "UserInformation.h"
UserInformation userInformation(67, 175, 23, 0);  // (userWeight, userHeight, userAge, isMale)

DynamicJsonDocument doc(1024);
#include "MotionDetection.h"
#include "MusicPlayer.h"
#include "themes.h"

MusicPlayer player(song, sizeof(song) / sizeof(int), 2);

#include "Scenes.h"
#include "BurndownChart.h"
#include "MqttConnection.h"

float movementValue;

MotionDetection motionDetection;
BurndownChart burndownChart;

const char* calorie_pub = "Send/Calorie/Burn/Data";

void setup() {
  Serial.begin(115200);  //Start serial communication
  setupMqtt();
  setupButton();
  while (!SD.begin(SDCARD_SS_PIN, SDCARD_SPI)) { // setup sd
    Serial.print("ERROR sd card not recognized");
  }
  motionDetection.startAccelerator();
  burndownChart.initializeUI();

  burndownChart.updateGraphVizuals();  // menuNavigationOnPress(showBurndownChartScene, showPlayerScene); //this is here to start burndownchart in the background

  // menuNavigationOnPress(showPlayerScene, showBurndownChartScene);
}

void loop() {
  loopMqtt();

  // Exercise isn't complete yet: Continually read movement-values and update chart accordingly
  if (burndownChart.isExercising()) {

    burndownChart.controlConstraints();
    buttonOnPress();
    menuNavigationOnPress(showPlayerScene, showBurndownChartScene);

    motionDetection.recordPreviousAcceleration();  // Read previous user-position

    player.playChunk();
    burndownChart.updateTimeElapsed(player.getCurrentPauseChunkDuration());

    // TEMPORARY
    // Serial.println("***********************");
    // Serial.println(burndownChart.getTimeElapsed());
    // Serial.println(burndownChart.getActualCaloriesPerSecond());
    // Serial.println(burndownChart.getExpectedCaloriesPerSecond());
    // Serial.println("***********************");

    movementValue = motionDetection.detectMotion();  // Read current user-position
    burndownChart.sufficientMovementInquiry(userInformation, movementValue, player.getCurrentPauseChunkDuration());
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