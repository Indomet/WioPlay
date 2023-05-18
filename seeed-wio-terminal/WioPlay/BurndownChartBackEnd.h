#include <iostream>
#include <algorithm>

class BurndownChartBackEnd  // Has the responsibility of dealing with logic and functionality of the burndown chart
{
public:
  BurndownChartBackEnd(float exerciseDuration, float caloriesGoal, byte chosenActivityIdx) {
    this->exerciseDuration = exerciseDuration;
    this->caloriesGoal = caloriesGoal;
    this->chosenActivityIdx = chosenActivityIdx;

    caloriesBurnt = 0;
    timeElapsed = 0;

    balanceFactor = constrainCaloriesBurntVelocity(0.08, 1000);  // Note: Create variable for 'originalUpdateDelay' --> Delay used when song not playing  and 'targetBalanceFactor'
    calorieVariableBoundaries();
  }

  // Returns a string comparing the actual calories burnt per second with the expected
  std::string displayCalorieStatistics() {
    std::string actual = std::to_string(getActualCaloriesPerSecond());
    std::string expected = std::to_string(getGeneralExpectedCaloriesPerSecond());

    return actual + ", " + expected;
  }

  bool isExercising() {
    return convertMilliToSeconds(timeElapsed) < exerciseDuration;
  }

  float getCaloriesBurnt() {
    return caloriesBurnt;
  }

  float getExpectedValue() {
    return (convertMilliToSeconds(timeElapsed) / exerciseDuration) * caloriesGoal;
  }

  // Formula reference: "Calculating daily calorie burn", https://www.medicalnewstoday.com/articles/319731
  // Takes into consideration the inputted user characteristics and how much the user has moved since last update of the burndown chart to calculate calories burnt
  float burnCalories(UserInformation userInformation, float movementValue, float updateDelay)  // Burn calories based on the movement-value
  {
    movementValue = getMETValue(movementValue);
    float moveFactor = (movementValue / updateDelay) * balanceFactor;

    int sexIdx = userInformation.isMale ? 0 : 1;

    return (sexCalorieConstants[sexIdx][0] + (sexCalorieConstants[sexIdx][1] * userInformation.userWeight) + (sexCalorieConstants[sexIdx][2] * userInformation.userHeight) - (sexCalorieConstants[sexIdx][3] * userInformation.userAge)) * moveFactor;
  }

  bool checkIfExerciseSettingsAreRealistic()
  {
     float expectedCaloriesPerSecond = getGeneralExpectedCaloriesPerSecond();
     float exerciseTypeAndCaloriesRelation = expectedCaloriesPerSecond / (chosenActivityIdx + 1);

     return exerciseTypeAndCaloriesRelation <= realisticExerciseSettingsComparisonFactor;
  }

  float convertMilliToSeconds(float milli) {
    return milli / 1000;
  }

  // Conform calories burned velocity in accordance to realistic boundaries
  float constrainCaloriesBurntVelocity(float balanceFactor, float updateValue) {
    // float controlVariable = 1 / balanceFactor; // ((songPauseChunkDuration + 1) * 1000) / balanceFactor;
    float controlVariable = (balanceFactor * 100) / updateValue;

    float minValue = min(controlVariable, realisticCaloriesBurntVelocity[0]);
    float maxValue = max(controlVariable, realisticCaloriesBurntVelocity[1]);

    return minValue + maxValue;
  }

  // Only burn calories if user's movement-intensity corresponds with selected exercise
  void sufficientMovementInquiry(UserInformation userInformation, float movementValue, float updateDelay) {
    if (userIsMovingFastEnough(movementValue)) {
      caloriesBurnt += burnCalories(userInformation, movementValue, updateDelay);
    } else {
      // Serial.println("You are not exercising hard enough for the selected exercise!");
    }
  }

  void changeAttributeValues(float newExerciseDuration, float newCaloriesGoal, byte newChosenActivityIdx) {
    exerciseDuration = newExerciseDuration;
    caloriesGoal = newCaloriesGoal;
    chosenActivityIdx = newChosenActivityIdx;
  }

  bool checkIfUserAccomplishedGoal() {
    return caloriesBurnt >= caloriesGoal;
  }

  // Returns the calories burnt per second at a given point of time. If 'timeElapsed' = 'exerciseDuration', the method gets the calories burnt across the entire workout
  float getActualCaloriesPerSecond() {
    return caloriesBurnt / convertMilliToSeconds(timeElapsed);
  }

  // Expected calories to burn per second from current calories burnt to reach goal (real-time updating)
  float getExpectedCaloriesPerSecond() {
    float caloriesLeft = getCaloriesLeft();
    float secondsLeft = getSecondsLeft();

    return caloriesLeft / secondsLeft;
  }

  // Get the calories per second to burn in order to accomplish the calorie goal within the exercise duration
  float getGeneralExpectedCaloriesPerSecond() {
    return caloriesGoal / exerciseDuration;
  }

  float getTimeElapsed() {
    return timeElapsed;
  }

  void updateTimeElapsed(float duration) {
    timeElapsed += duration;
  }

  float getSecondsLeft() {
    float secondsLeft = exerciseDuration - convertMilliToSeconds(timeElapsed);
  }

private:
  const float realisticCaloriesBurntVelocity[2]{ 0.0025, 0.0065 };
  const float realisticExerciseSettingsComparisonFactor = 0.5;

  float standard;
  float minMovement;  // Minimal movement required for specific exercise (Deals with cases where user isn't moving enough in accordance with selected exercise)
  float maxMovement;  // Maximal movement required for specific exercise (Handles the case where user selected 'Walking' but is running in reality)
  float proportionalConstant;

  // Note for developers: It's important that the intensity of the activities increases as the this index does, to preserve the beneficial formula used in 'checkIfExerciseSettingsAreRealistic()'
  byte chosenActivityIdx;       // 0 = Walking, 1 = Hiking, 2 = Running
  float balanceFactor;
  float timeElapsed;

  float exerciseDuration;  // 30 (Seconds)
  float caloriesGoal;      // 100 --> Put in 'ExerciseSettings'
  float caloriesBurnt;


  float sexCalorieConstants[2][4]{
    { 66, 6.2, 12.7, 6.76 },    // Male:   {startConstant, weightConstant, heightConstant, ageConstant}
    { 655.1, 4.35, 4.7, 4.65 }  // Female: {startConstant, weightConstant, heightConstant, ageConstant}
  };

  // Note: Row[i] is equivalent to the (i)th activity
  // Note: Retrieve standard-value by getting the average of min and max value
  // The minimal and maximal met-values for each physical activity
  // Calorie reference: "List Of METs Of The Most Popular Exercises", https://betterme.world/articles/calories-burned-calculator/
  byte metRanges[3][2] = {
    { 3, 6 },   // Walking
    { 9, 23 },  // Running
    { 5, 8 }    // Hiking
  };

  // Movement values required to acquire average/standard MET-value for specified exercise
  float standardMovementValues[3] = {
    0.3,  // Walking
    3,    // Running
    1.5   // Hiking
  };

  float getMETValue(float movementValue) {
    return movementValue * proportionalConstant;
  }

  float getCaloriesLeft() {
    return max(0, caloriesGoal - caloriesBurnt);
  }

  bool userIsMovingFastEnough(float movementValue) {
    return movementValue >= minMovement;
  }

  void calorieVariableBoundaries() {
    standard = (float)(metRanges[chosenActivityIdx][0] + metRanges[chosenActivityIdx][1]) / 2;  // Average of the min and max MET-Values of chosen activity
    proportionalConstant = standard / standardMovementValues[chosenActivityIdx];
    minMovement = (float)metRanges[chosenActivityIdx][0] / proportionalConstant;  // Minimal movement required for user to be considered actually doing the selected activity
    maxMovement = (float)metRanges[chosenActivityIdx][1] / proportionalConstant;  // Maximal movement boundary
  }
};