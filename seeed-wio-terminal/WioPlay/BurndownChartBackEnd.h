class BurndownChartBackEnd {
public:
  BurndownChartBackEnd(float exerciseDuration, float caloriesGoal, byte chosenActivityIdx) {
    this->exerciseDuration = exerciseDuration;
    this->caloriesGoal = caloriesGoal;
    this->chosenActivityIdx = chosenActivityIdx;

    caloriesBurnt = 0;
    timeElapsed = 0;
    isWorkingOut = true;

    balanceFactor = constrainCaloriesBurntVelocity(targetBalanceFactor, 1000);
    calculateCalorieVariableBoundaries();
  }

  // Returns a string comparing the actual calories burnt per second with the expected
  std::string displayCalorieStatistics() {
    std::string actual = std::to_string(getActualCaloriesPerSecond());
    std::string expected = std::to_string(getGeneralExpectedCaloriesPerSecond());

    return actual + "  " + expected;
  }

  /*
  // Note: We used this previously when our only intention was to make it work without MQTT connection
  bool isExercising() {
    return convertMilliToSeconds(timeElapsed) < exerciseDuration;
  }
  */

  // Check if the user is exercising with a variable associated with MQTT
  bool isExercising() {
    return isWorkingOut;
  }

  float getCaloriesBurnt() {
    return caloriesBurnt;
  }

  float getExpectedValue() {
    return (convertMilliToSeconds(timeElapsed) / exerciseDuration) * caloriesGoal;
  }

  // Formula reference: "Calculating daily calorie burn", https://www.medicalnewstoday.com/articles/319731
  // Takes into consideration the inputted user characteristics and how much the user has moved since last update of the burndown chart to calculate calories burnt
  float burnCalories(UserInformation userInformation, float movementValue)  // Burn calories based on the movement-value
  {
    movementValue = getMETValue(movementValue);
    float moveFactor = (movementValue / (pow(updateDelay, 2))) * balanceFactor;

    int sexIdx = userInformation.isMale ? 0 : 1;
    return (sexCalorieConstants[sexIdx][0] + (sexCalorieConstants[sexIdx][1] * userInformation.userWeight) + (sexCalorieConstants[sexIdx][2] * userInformation.userHeight) - (sexCalorieConstants[sexIdx][3] * userInformation.userAge)) * moveFactor;
  }

  float convertMilliToSeconds(float milli) {
    return milli / 1000;
  }

  // Conform calories burned velocity in accordance to realistic boundaries
  double constrainCaloriesBurntVelocity(float balanceFactor, float updateValue) {
    float controlVariable = (balanceFactor * 100) / updateValue;

    double minValue = min(controlVariable, realisticCaloriesBurntVelocity[0]);
    double maxValue = max(controlVariable, realisticCaloriesBurntVelocity[1]);

    return (minValue + maxValue) / 2;
  }

  // Only burn calories if user's movement-intensity corresponds with selected exercise
  void sufficientMovementInquiry(UserInformation userInformation, float movementValue) {
    if (userIsMovingFastEnough(movementValue)) {
      caloriesBurnt += burnCalories(userInformation, movementValue);
    } else {
      Serial.println("You are not exercising hard enough for the selected exercise!");
    }
  }

  void resetExerciseValues(float newExerciseDuration, float newCaloriesGoal, byte newChosenActivityIdx) {
    exerciseDuration = newExerciseDuration;
    caloriesGoal = newCaloriesGoal;
    chosenActivityIdx = newChosenActivityIdx;
    timeElapsed = 0;
    caloriesBurnt = 0;
  }

  bool checkIfUserAccomplishedGoal() {
    return caloriesBurnt >= caloriesGoal;
  }

  // Returns the calories burnt per second at a given point of time. If 'timeElapsed' = 'exerciseDuration', the method gets the calories burnt across the entire workout
  float getActualCaloriesPerSecond() {
    return caloriesBurnt / convertMilliToSeconds(timeElapsed);
  }

  // FUTURE / Out-of-scope update:
  // Returns expected calories to burn per second from current calories burnt to reach goal (real-time updating)
  float getExpectedCaloriesPerSecond() {
    float caloriesLeft = getCaloriesLeft();
    float secondsLeft = getSecondsLeft();

    return caloriesLeft / secondsLeft;
  }

  // Returns the calories to burn per second during the entire workout to reach the calorie goal
  float getGeneralExpectedCaloriesPerSecond() {
    return caloriesGoal / exerciseDuration;
  }

  float getTimeElapsed() {
    return timeElapsed;
  }

  void updateTimeElapsed() {
    timeElapsed += updateDelay;
  }

  float getSecondsLeft() {
    return exerciseDuration - convertMilliToSeconds(timeElapsed);
  }

  float getUpdateDelay() {
    return updateDelay;
  }

  void setIsWorkingOut(bool isWorkingOut) {
    this->isWorkingOut = isWorkingOut;
  }

private:
  const float realisticCaloriesBurntVelocity[2]{ 0.0025, 0.0065 };

  const float sexCalorieConstants[2][4]{
    { 66, 6.2, 12.7, 6.76 },    // Male:   {startConstant, weightConstant, heightConstant, ageConstant}
    { 655.1, 4.35, 4.7, 4.65 }  // Female: {startConstant, weightConstant, heightConstant, ageConstant}
  };

  // Notes:
  // * Row[i] is equivalent to the (i)th activity
  // * Retrieve standard-value by getting the average of min and max value
  // The minimal and maximal met-values for each physical activity
  const byte metRanges[3][2] = {
    // Calorie reference: "List Of METs Of The Most Popular Exercises", https://betterme.world/articles/calories-burned-calculator/
    { 3, 6 },   // Walking
    { 9, 23 },  // Running
    { 5, 8 }    // Hiking
  };

  // Movement values required to acquire average/standard MET-value for specified exercise
  const float standardMovementValues[3] = {
    0.3,  // Walking
    3,    // Running
    1.5   // Hiking
  };

  const float targetBalanceFactor = 0.08;
  const float updateDelay = 100;

  float standard;
  float minMovement;  // Minimal movement required for specific exercise (Deals with cases where user isn't moving enough in accordance with selected exercise)
  float maxMovement;  // Maximal movement required for specific exercise (Handles the case where user selected 'Walking' but is running in reality)
  float proportionalConstant;

  byte chosenActivityIdx;
  float balanceFactor;
  float timeElapsed;

  float exerciseDuration;
  float caloriesGoal;
  double caloriesBurnt;
  bool isWorkingOut;

  float getMETValue(float movementValue) {
    return movementValue * proportionalConstant;
  }

  float getCaloriesLeft() {
    return max(0, caloriesGoal - caloriesBurnt);
  }

  bool userIsMovingFastEnough(float movementValue) {
    return movementValue >= minMovement;
  }

  void calculateCalorieVariableBoundaries() {
    standard = (float)(metRanges[chosenActivityIdx][0] + metRanges[chosenActivityIdx][1]) / 2;  // Average of the min and max MET-Values of chosen activity
    proportionalConstant = standard / standardMovementValues[chosenActivityIdx];
    minMovement = (float)metRanges[chosenActivityIdx][0] / proportionalConstant;  // Minimal movement required for user to be considered actually doing the selected activity
    maxMovement = (float)metRanges[chosenActivityIdx][1] / proportionalConstant;  // Maximal movement boundary
  }
};