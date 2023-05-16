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

    // balanceFactor = 0.08;
    balanceFactor = constrainCaloriesBurntVelocity();

    // standard = (float)(metRanges[chosenActivityIdx][0] + metRanges[chosenActivityIdx][1]) / 2;  // Average of the min and max MET-Values of chosen activity
    standard = getAverageValue(new float[] {(float)(metRanges[chosenActivityIdx][0]), (float)(metRanges[chosenActivityIdx][1])});

    proportionalConstant = standard / standardMovementValues[chosenActivityIdx];
    minMovement = (float)metRanges[chosenActivityIdx][0] / proportionalConstant;  // Minimal movement required for user to be considered actually doing the selected activity
    maxMovement = (float)metRanges[chosenActivityIdx][1] / proportionalConstant;  // Maximal movement boundary
  }

  // Returns a string comparing the actual calories burnt per second with the expected
  std::string displayCalorieStatistics() {
    std::string actual = std::to_string(getActualCaloriesPerSecond());
    // std::string expected = std::to_string(burndownChartBackEnd.getExpectedCaloriesPerSecond());
    std::string expected = std::to_string(getGeneralExpectedCaloriesPerSecond());

    return actual + ", " + expected;
  }

  bool isExercising()
  {
    return (timeElapsed / 1000) < exerciseDuration;
  }

  float getCaloriesBurnt() {
    return caloriesBurnt;
  }

  float getExpectedValue() {
    return  ((timeElapsed / 1000) / exerciseDuration) * caloriesGoal;
  }

  // Formula reference: "Calculating daily calorie burn", https://www.medicalnewstoday.com/articles/319731
  // Takes into consideration the inputted user characteristics and how much the user has moved since last update of the burndown chart to calculate calories burnt
  float burnCalories(UserInformation userInformation, float movementValue, float songPauseChunkDuration)  // Burn calories based on the movement-value
  {
    movementValue = getMETValue(movementValue);
    float moveFactor = (movementValue / songPauseChunkDuration) * balanceFactor;

    if (userInformation.isMale) {
      return (66 + (6.2 * userInformation.userWeight) + (12.7 * userInformation.userHeight) - (6.76 * userInformation.userAge)) * moveFactor;
    } else {
      return (655.1 + (4.35 * userInformation.userWeight) + (4.7 * userInformation.userHeight) - (4.7 * userInformation.userAge)) * moveFactor;
    }
  }

  // TODO
  // Constant values for magic values in line 52 and 54

  // Conform calories burned velocity in accordance to realistic boundaries
  float constrainCaloriesBurntVelocity()
  {
    // Takes into consideration: moveFactor = (movementValue / songPauseChunkDuration) * balanceFactor;
    float controlVariable = songPauseChunkDuration / balanceFactor;

    float maxValue = max(controlVariable, realisticCaloriesBurntVelocity[0]);
    float minValue = min(controlVariable, realisticCaloriesBurntVelocity[1]);

    return getAverageValue(new float[] {maxValue, minValue});
  }

  float getAverageValue(float[] values)
  {
    float sum = 0;

    for (int i = 0; i < values.length; i++)
    {
      sum += values[i];
    }

    return sum / values.length;
  }

  // Only burn calories if user's movement-intensity corresponds with selected exercise
  void sufficientMovementInquiry(UserInformation userInformation, float movementValue, float songPauseChunkDuration) {
    if (userIsMovingFastEnough(movementValue)) {
      caloriesBurnt += burnCalories(userInformation, movementValue, songPauseChunkDuration);
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
    return caloriesBurnt / (timeElapsed / 1000);
  }

  // Expected calories to burn per second from current calories burnt to reach goal
  float getExpectedCaloriesPerSecond() {
    float caloriesLeft = max(0, caloriesGoal - caloriesBurnt);
    float secondsLeft = exerciseDuration - (timeElapsed / 1000);

    return caloriesLeft / secondsLeft;
  }

  float getGeneralExpectedCaloriesPerSecond() {
    return caloriesGoal / exerciseDuration;
  }

  float getTimeElapsed() {
    return timeElapsed;
  }

  void updateTimeElapsed(float duration) {
    timeElapsed += duration;
  }

private:
  const float realisticCaloriesBurntVelocity[] {0.3, 0.9};
  float standard;
  float minMovement;  // Minimal movement required for specific exercise (Deals with cases where user isn't moving enough in accordance with selected exercise)
  float maxMovement;  // Maximal movement required for specific exercise (Handles the case where user selected 'Walking' but is running in reality)
  float proportionalConstant;
  byte chosenActivityIdx;       // 0 = Walking
  float balanceFactor;
  float timeElapsed;

  float exerciseDuration;  // 30 (Seconds)
  float caloriesGoal;      // 100 --> Put in 'ExerciseSettings'
  float caloriesBurnt;

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

  bool userIsMovingFastEnough(float movementValue) {
    return movementValue >= minMovement;
  }
};