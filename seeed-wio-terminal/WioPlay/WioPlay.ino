#include "seeed_line_chart.h"
#include <map>
#include <ArduinoJson.h>
#include "UserInformation.h"
UserInformation userInformation (67, 175, 23, 0); // (userWeight, userHeight, userAge, isMale)

DynamicJsonDocument doc(1024);
#include "MotionDetection.h"
#include "Scenes.h"
#include "BurndownChart.h"
#include "MqttConnection.h"
#include "MusicPlayer.h"


float movementValue;
bool isExercising = true;

byte caloriesPerSecondTextCoordinates[2] = {5, 50};
byte exerciseResultTextCoordinates[2] = {15, 70};

MotionDetection motionDetection;
BurndownChart burndownChart;

MusicPlayer player(song, sizeof(song) / sizeof(int), 1.2);
const char* calorie_pub = "Send/Calorie/Burn/Data";



void setup()
{
  Serial.begin(9600);  //Start serial communication
  setupMqtt();
  setupButton();
  motionDetection.startAccelerator();
  burndownChart.initializeUI();
  menuNavigationOnPress(showSecondScene, showFirstScene);
}

void loop()
{
  loopMqtt();
  
  // Exercise isn't complete yet: Continually read movement-values and update chart accordingly
  if (isExercising)
  {
    // mqttConnection.loopMqtt();
   
    burndownChart.controlConstraints();
    menuNavigationOnPress(showFirstScene, showSecondScene);
    
    motionDetection.recordPreviousAcceleration(); // Read previous user-position
    //delay(burndownChart.getDelayValue());

    player.playChunk();

    burndownChart.updateTimeElapsed(player.getCurrentPauseChunkDuration());

    // TEMPORARY
    Serial.println("***********************");
    Serial.println(burndownChart.getTimeElapsed());
    Serial.println(burndownChart.getActualCaloriesPerSecond());
    Serial.println(burndownChart.getExpectedCaloriesPerSecond());
    Serial.println("***********************");

    movementValue =  motionDetection.detectMotion(); // Read current user-position
    burndownChart.sufficientMovementInquiry(userInformation, movementValue);
    isExercising = burndownChart.isExercising();
    client.publish(calorie_pub, String(burndownChartBackEnd.getCaloriesBurnt()).c_str());
  }

  // Exercise is completed: Inactivate burndown chart and show panel
  else
  {
    displayExerciseResults();
  }
}
void showSecondScene() {
burndownChart.updateGraphVizuals();
}

void displayExerciseResults()
{
  tft.setTextSize(3);

  displayCalorieSecondComparison();

  // User completed goal
  if (burndownChart.checkIfUserAccomplishedGoal())
  {
    tft.fillScreen(TFT_GREEN);
    tft.drawString("Accomplished goal!", exerciseResultTextCoordinates[0], exerciseResultTextCoordinates[1]);
  }

  // User didn't attain the set goal
  else
  {
    tft.fillScreen(TFT_RED);
    tft.drawString("Menu here!", exerciseResultTextCoordinates[0], exerciseResultTextCoordinates[1]);
  }
}

void displayCalorieSecondComparison()
{
  String caloriesPerSecondComparison = String(burndownChart.displayCalorieStatistics().c_str());
  tft.drawString(caloriesPerSecondComparison, caloriesPerSecondTextCoordinates[0], caloriesPerSecondTextCoordinates[1]);
}