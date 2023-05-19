class UserInformation {
private:
  // Settings: Selection of physical user characteristics
  float userWeight;  // Kilogram
  float userHeight;  // Centimeters
  byte userAge;      // Years
  bool isMale;       // Sex

public:
  UserInformation(float userWeight, float userHeight, byte userAge, bool isMale) {
    setInformation(userWeight, userHeight, userAge, isMale);
  }

public:
  // User changes self-description of physicality
  void setInformation(float newUserWeight, float newUserHeight, byte newUserAge, bool isMale) {
    userWeight = newUserWeight;
    userHeight = newUserHeight;
    userAge = newUserAge;
    this->isMale = isMale;
  }

public:
  float getUserWeight()
  {
    return userWeight;
  }

public:
  float getUserHeight()
  {
    return userWeight;
  }

public:
  float getUserAge()
  {
    return userWeight;
  }

public:
  float getIsMale()
  {
    return isMale;
  }
};