#include "Arduino.h"

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
  float currentSegments = 0;

  float movementValue;

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

  bool userIsMovingFastEnough()
  {
    return movementValue >= minMovement;
  }
};