#include "Arduino.h"

class UserInformation
{
  public:
  // Settings: Selection of physical user characteristics
  float userWeight; // 65 Kilogram
  float userHeight; // 175 Centimeters
  byte userAge; // 23
  bool isMale; // 0

  UserInformation(float userWeight, float userHeight, byte userAge, bool isMale)
  {
    this->userWeight = userWeight;
    this->userHeight = userHeight;
    this->userAge = userAge;
    this->isMale = isMale;
  }
};