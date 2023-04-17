#include "Arduino.h"
// #include "UserInformation.h"

class BurndownChartBackEnd // Logic and functionality of the burndown chart
{
  public:
  float standard;
  float minMovement; // Minimal movement required for specific exercise (Deals with cases where user isn't moving enough in accordance with selected exercise)
  float maxMovement; // Maximal movement required for specific exercise (Handles the case where user selected 'Walking' but is running in reality)
  float proportionalConstant;
  float delayValue; // 200

  float exerciseDuration; // 30 (Seconds)
  float caloriesGoal; // 100 --> Put in 'ExerciseSettings'
  byte chosenActivityIdx; // 0

  float totalNumberOfSegments; // Number of update segments in graph until goal is reached
  float currentSegments;
  float balanceFactor;

  // float movementValue;
  float caloriesBurnt;

// Note: Row[i] is equivalent to the (i)th activity
// Note: Retrieve standard-value by getting the average of min and max value
// The minimal and maximal met-values for each physical activity
// Calorie reference: "List Of METs Of The Most Popular Exercises", https://betterme.world/articles/calories-burned-calculator/
  byte metRanges[3][2] =
  {
    {3, 6},  // Walking
    {9, 23}, // Running
    {5, 8}   // Hiking
  };

  // Movement values required to acquire average/standard MET-value for specified exercise
  float standardMovementValues[3] =
  {
    0.3,  // Walking
    3,    // Running
    1.5   // Hiking
  };

  BurndownChartBackEnd(float delayValue, float exerciseDuration, float caloriesGoal, byte chosenActivityIdx)
  {
    this->delayValue = delayValue;
    this->exerciseDuration = exerciseDuration;
    this->caloriesGoal = caloriesGoal;
    this->chosenActivityIdx = chosenActivityIdx;

    caloriesBurnt = 0;
    currentSegments = 0;
    balanceFactor = 0.08;
    totalNumberOfSegments = (exerciseDuration / (delayValue / 1000)) + 1;

    standard = (float)(metRanges[chosenActivityIdx][0] + metRanges[chosenActivityIdx][1]) / 2; // Average of the min and max MET-Values of chosen activity
    proportionalConstant = standard / standardMovementValues[chosenActivityIdx];
    minMovement = (float)metRanges[chosenActivityIdx][0] / proportionalConstant; // Minimal movement required for user to be considered actually doing the selected activity
    maxMovement = (float)metRanges[chosenActivityIdx][1] / proportionalConstant; // Maximal movement boundary
  }

  bool isExercising()
  {
    return currentSegments < totalNumberOfSegments;
  }

  float getExpectedValue()
  {
    return ((currentSegments - 1) / totalNumberOfSegments) * caloriesGoal;    
  }

  bool userIsMovingFastEnough(float movementValue)
  {
    return movementValue >= minMovement;
  }

  // ----------------------------------------------------

// Formula reference: "Calculating daily calorie burn", https://www.medicalnewstoday.com/articles/319731
// Takes into consideration the inputted user characteristics and how much the user has moved since last update of the burndown chart to calculate calories burnt
float burnCalories(UserInformation userInformation, float movementValue) // Burn calories based on the movement-value
{
  movementValue = getMETValue(movementValue);
  float moveFactor = (movementValue / delayValue) * balanceFactor;

  if (userInformation.isMale)
  {
    return (66 + (6.2 * userInformation.userWeight) + (12.7 * userInformation.userHeight) - (6.76 * userInformation.userAge)) * moveFactor;
  }
  else
  {
    return (655.1 + (4.35 * userInformation.userWeight) + (4.7 * userInformation.userHeight) - (4.7 * userInformation.userAge)) * moveFactor;
  }
}

float getMETValue(float movementValue)
{
  return movementValue * proportionalConstant;
}

void updateBurnDownChart(UserInformation userInformation, float movementValue)
{
  if (userIsMovingFastEnough(movementValue))
  {
    caloriesBurnt += burnCalories(userInformation, movementValue);
  }
  else
  {
    Serial.println("You are not exercising hard enough for the selected exercise!");
  }
}
};