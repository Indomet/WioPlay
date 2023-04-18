#include "seeed_line_chart.h"
// #include "LIS3DHTR.h"   //include accelerometer library
#include <map>
// LIS3DHTR <TwoWire> lis;  //Initialize accelerometer

using std::string;

#include "MotionDetection.h"
#include "UserInformation.h"
#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

float movementValue;
bool isExercising = true;

UserInformation userInformation (67, 175, 23, 0); // (userWeight, userHeight, userAge, isMale)
BurndownChartBackEnd burndownChartBackEnd (1000, 20, 20, 0); // (float delayValue, float exerciseDuration, float caloriesGoal, byte chosenActivityIdx)
BurndownChartFrontEnd burndownChartFrontEnd (20); // (graphUIXStartValue)
MotionDetection motionDetection;

void setup()
{
  Serial.begin(115200);  //Start serial communication

  motionDetection.startAccelerator();
  /*
  lis.begin(Wire1);      //Start accelerometer

  //Check whether accelerometer is working
  while (!lis)
  {
    Serial.println("ERROR accelerometer not working");
  }

  lis.setOutputDataRate(LIS3DHTR_DATARATE_25HZ);  //Data output rate (5Hz up to 5kHz)
  lis.setFullScaleRange(LIS3DHTR_RANGE_2G);       //Scale range (2g up to 16g)
  */

  burndownChartFrontEnd.start();
}

void loop()
{
  if (isExercising)
  {
    burndownChartFrontEnd.checkMax();

    burndownChartBackEnd.currentSegments++;
    isExercising = burndownChartBackEnd.isExercising();

    burndownChartFrontEnd.update(burndownChartBackEnd);

    motionDetection.recordPreviousAcceleration();

    /*
    float current_x, current_y, current_z;  //Initialize variables to store accelerometer values
    float prev_x, prev_y, prev_z;

    lis.getAcceleration(&prev_x , &prev_y, &prev_z);
    */

    delay(burndownChartBackEnd.delayValue);

    /*
    lis.getAcceleration(&current_x , &current_y, &current_z);

    float diff_x = abs(current_x - prev_x), diff_y = abs(current_y - prev_y), diff_z = abs(current_z - prev_z);
    movementValue = diff_x + diff_y + diff_z;
    */
  
    movementValue =  motionDetection.detectMotion();

    burndownChartBackEnd.updateBurnDownChart(userInformation, movementValue);
  }
  else
  {
    tft.fillScreen(TFT_RED);
    tft.setTextSize(3);
    tft.drawString("Menu here!", 50, 70);
  }
}