#include "seeed_line_chart.h"
#include <map>
#include <ArduinoJson.h>
#include "UserInformation.h"
UserInformation userInformation (67, 175, 23, 0); // (userWeight, userHeight, userAge, isMale)

DynamicJsonDocument doc(1024);
#include "MotionDetection.h"
#include "BurndownChart.h"
#include "MqttConnection.h"
#include "MusicPlayer.h"

float movementValue;
bool isExercising = true;

MotionDetection motionDetection;
BurndownChart burndownChart;
MusicPlayer player(song, sizeof(song) / sizeof(int), 1.2);
const char* calorie_pub = "Send/Calorie/Burn/Data";

void setup()
{
  Serial.begin(9600);  //Start serial communication
  setupMqtt();
  motionDetection.startAccelerator();
  burndownChart.initializeUI();
}

void loop()
{
  loopMqtt();
  // Exercise isn't complete yet: Continually read movement-values and update chart accordingly


  if (isExercising)
  {
   //mqttConnection.loopMqtt();
    burndownChart.controlConstraints();
    motionDetection.recordPreviousAcceleration(); // Read previous user-position
    //delay(burndownChart.getDelayValue());
    player.playChunk();

    movementValue =  motionDetection.detectMotion(); // Read current user-position
    burndownChart.sufficientMovementInquiry(userInformation, movementValue);
    isExercising = burndownChart.isExercising();
    client.publish(calorie_pub, String(burndownChartBackEnd.caloriesBurnt).c_str());

  }

  // Exercise is completed: Inactivate burndown chart and show panel
  else
  {    
    tft.fillScreen(TFT_RED);
    tft.setTextSize(3);
    tft.drawString("Menu here!", 50, 70);
  }

}