#include "seeed_line_chart.h"
TFT_eSPI tft;
TFT_eSprite spr = TFT_eSprite(&tft);

#include "LIS3DHTR.h"   //include accelerometer library
#include <map>
LIS3DHTR <TwoWire> lis;  //Initialize accelerometer

using std::string;
#include"TFT_eSPI.h"

#define MAX_SIZE 10 // maximum size of data displayed at once in graph
doubles data[2];

#include "UserInformation.h"
#include "BurndownChartBackEnd.h"

float movementValue;

float graphUIXStartValue = 20; // X-coordinate in terminal for the graph to start at

float caloriesBurnt = 0;
bool isExercising = true;


UserInformation userInformation (67, 175, 23, 0);
BurndownChartBackEnd burndownChart (1000, 20, 20, 0); // (float delayValue, float exerciseDuration, float caloriesGoal, byte chosenActivityIdx)

void setup() {
  Serial.begin(115200);  //Start serial communication
  lis.begin(Wire1);      //Start accelerometer

  //Check whether accelerometer is working
  while (!lis)
  {
    Serial.println("ERROR accelerometer not working");
  }

  lis.setOutputDataRate(LIS3DHTR_DATARATE_25HZ);  //Data output rate (5Hz up to 5kHz)
  lis.setFullScaleRange(LIS3DHTR_RANGE_2G);       //Scale range (2g up to 16g)
  tft.begin();

  pinMode(A0, INPUT); // NECESSARY?
  tft.begin();
  tft.setRotation(3);
  spr.createSprite(TFT_HEIGHT, TFT_WIDTH);
  spr.setRotation(3);

  // NOTE: THIS MIGHT BE REDUNDANT
  tft.setRotation(3); // set the screen orientation
  tft.setTextColor(TFT_BLACK); //set text color
  tft.setTextSize(3); //set text size
}

void loop()
{
  if (isExercising)
  {
    if(data[0].size() >= MAX_SIZE)
    {
      for (uint8_t i = 0; i<2; i++)
      {
        data[i].pop(); // Remove the first read variable
      }
    }

    burndownChart.currentSegments++;
    isExercising = burndownChart.isExercising();

    spr.fillSprite(TFT_WHITE);

        // Settings for the line graph title
    auto header = text(80, 0)
                      .value("Burndown chart")
                      .width(spr.width())
                      .thickness(2);

    header.height(header.font_height(&spr) * 2); // * 2
    header.draw(&spr); // Header height is the twice the height of the font

    auto headerY = text(15, 18)
                  .value("Calories burned")
                  .backgroud(TFT_WHITE)
                  .color(TFT_RED)
                  .thickness(1);

    headerY.height(headerY.font_height(&spr) * 2);
    headerY.draw(&spr); // Header height is the twice the height of the font

    auto headerX = text(240, 190)
      .value("Time elapsed")
      .backgroud(TFT_WHITE)
      .thickness(1);

    headerX.height(headerX.font_height(&spr) * 2);
    headerX.draw(&spr); // Header height is the twice the height of the font

    data[0].push(burndownChart.caloriesBurnt);
    data[1].push(burndownChart.getExpectedValue());

        // Settings for the line graph
    auto content = line_chart(graphUIXStartValue, header.height()); //(x,y) where the line graph begins   auto content = line_chart(20, header.height());
    content
        .height(spr.height() - header.height() * 1.5) // actual height of the line chart
        .width(spr.width() - content.x() * 2)         // actual width of the line chart
        .based_on(0.0)                                // Starting point of y-axis, must be a float
        .show_circle(true, false)
        .value({data[0], data[1]})
        .max_size(MAX_SIZE)
        .color(TFT_RED, TFT_BLUE)
        .backgroud(TFT_WHITE)
        .draw(&spr);

    spr.pushSprite(0, 0);

    float current_x, current_y, current_z;  //Initialize variables to store accelerometer values
    float prev_x, prev_y, prev_z;

    lis.getAcceleration(&prev_x , &prev_y, &prev_z);

    delay(burndownChart.delayValue);

    lis.getAcceleration(&current_x , &current_y, &current_z);

    float diff_x = abs(current_x - prev_x), diff_y = abs(current_y - prev_y), diff_z = abs(current_z - prev_z);
    movementValue = diff_x + diff_y + diff_z;

    burndownChart.updateBurnDownChart(userInformation, movementValue);
  }
  else
  {
    tft.fillScreen(TFT_RED);
    tft.setTextSize(3);
    tft.drawString("Menu here!", 50, 70);
  }
}