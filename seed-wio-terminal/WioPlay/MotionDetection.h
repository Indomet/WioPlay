#include "LIS3DHTR.h"
LIS3DHTR <TwoWire> lis;

class MotionDetection
{
  public:
  float current_x, current_y, current_z;  //Initialize variables to store accelerometer values
  float prev_x, prev_y, prev_z;

  void startAccelerator()
  {
    lis.begin(Wire1);      //Start accelerometer

  //Check whether accelerometer is working
  while (!lis)
  {
    Serial.println("ERROR accelerometer not working");
  }

  lis.setOutputDataRate(LIS3DHTR_DATARATE_25HZ);  //Data output rate (5Hz up to 5kHz)
  lis.setFullScaleRange(LIS3DHTR_RANGE_2G);       //Scale range (2g up to 16g)
  }

  void readPrevAccelValues()
  {

    lis.getAcceleration(&prev_x , &prev_y, &prev_z);
  }

  float readCurrentAccelValues()
  {
    lis.getAcceleration(&current_x , &current_y, &current_z);


  }

  float getMovementValue(float x, float y, float z) // diff_x,y,z values
  {
    float diff_x = abs(current_x - prev_x), diff_y = abs(current_y - prev_y), diff_z = abs(current_z - prev_z);
    return diff_x + diff_y + diff_z;
  }
};
