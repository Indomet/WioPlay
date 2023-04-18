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
    setInformation(userWeight, userHeight, userAge, isMale);
  }

  // User changes self-description of physicality
  void setInformation(float newUserWeight, float newUserHeight, byte newUserAge, bool isMale)
  {
    userWeight = newUserWeight;
    userHeight = newUserHeight;
    userAge = newUserAge;
    this->isMale = isMale;
  }
};