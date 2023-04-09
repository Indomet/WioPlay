#include "LIS3DHTR.h"   //include accelerometer library


LIS3DHTR <TwoWire> lis;  //Initialize accelerometer

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
}

void loop() {
  float x_values, y_values, z_values;  //Initialize variables to store accelerometer values
  float prev_x, prev_y, prev_z;
  prev_x = lis.getAccelerationX();  //store x-axis accelerometer values
  prev_y = lis.getAccelerationY();  //store y-axis accelerometer values
  prev_z = lis.getAccelerationZ();  //store z-axis accelerometer values
  delay(200);
  x_values = lis.getAccelerationX();  //store x-axis accelerometer values
  y_values = lis.getAccelerationY();  //store y-axis accelerometer values
  z_values = lis.getAccelerationZ();  //store z-axis accelerometer values

  float diff_x = abs(x_values - prev_x);
  float diff_y = abs(y_values - prev_y);
  float diff_z = abs(z_values - prev_z);

  if (diff_x > 0.5 || diff_y > 0.5 || diff_z > 0.5) {  // If the difference is greater than 10 (indicating movement), play the music
    Serial.println("Movement detected!");
  }
}