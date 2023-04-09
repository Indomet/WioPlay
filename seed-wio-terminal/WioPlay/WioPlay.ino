#include "LIS3DHTR.h"   //include accelerometer library
#include"TFT_eSPI.h"  // drawing shapes


LIS3DHTR <TwoWire> lis;  //Initialize accelerometer
TFT_eSPI tft;

void setup() {
  Serial.begin(115200);  //Start serial communication
  lis.begin(Wire1);      //Start accelerometer

  //Check whether accelerometer is working
  if (!lis) {
    Serial.println("ERROR");
    while (1);
  }
  lis.setOutputDataRate(LIS3DHTR_DATARATE_25HZ);  //Data output rate (5Hz up to 5kHz)
  lis.setFullScaleRange(LIS3DHTR_RANGE_2G);       //Scale range (2g up to 16g)
  tft.begin();
  tft.setRotation(3); // set the screen orientation
  tft.setTextColor(TFT_BLACK); //set text color
  tft.setTextSize(3); //set text size 

}

void loop() {
  float current_x, current_y, current_z;  //Initialize variables to store accelerometer values
  float prev_x, prev_y, prev_z;

  lis.getAcceleration(&prev_x , &prev_y, &prev_z);

  delay(200);

  lis.getAcceleration(&current_x , &current_y, &current_z);

  float diff_x = abs(current_x - prev_x), diff_y = abs(current_y - prev_y), diff_z = abs(current_z - prev_z);

  if (diff_x > 0.5 || diff_y > 0.5 || diff_z > 0.5) {  // If the difference is greater than 10 (indicating movement), play the music
    Serial.println("Movement detected!");
   
    tft.fillScreen(TFT_BLUE); //fill background 
    tft.drawString("Movement detected!",0,0); //draw text string 
    delay(500);
   
  } else {
    Serial.println("No movement");
    tft.fillScreen(TFT_RED);
    tft.drawString("No movement!",0,0); 
    delay(500);
  }

}