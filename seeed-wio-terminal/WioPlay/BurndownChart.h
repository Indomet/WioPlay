#include "Arduino.h"
#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

BurndownChartBackEnd burndownChartBackEnd (1000, 15, 20, 0); // (delayValue, exerciseDuration, caloriesGoal, chosenActivityIdx)
BurndownChartFrontEnd burndownChartFrontEnd (20); // (float graphUIXStartValue)

#include <iostream>
#include <string>

class BurndownChart
{
  public:
  void initializeUI()
  {
    burndownChartFrontEnd.initializeUI();
  }

  // Returns a string comparing the actual calories burnt per second with the expected
  std::string displayCalorieStatistics()
  {
    std::string actual = std::to_string(burndownChartBackEnd.getActualCaloriesPerSecond());
    // std::string expected = std::to_string(burndownChartBackEnd.getExpectedCaloriesPerSecond());
    std::string expected = std::to_string(burndownChartBackEnd.getGeneralExpectedCaloriesPerSecond());    

    return actual + ", " + expected;
  }

  float getGeneralExpectedCaloriesPerSecond()
  {
    return burndownChartBackEnd.getGeneralExpectedCaloriesPerSecond();
  }

  float getActualCaloriesPerSecond()
  {
    return burndownChartBackEnd.getActualCaloriesPerSecond();
  }

  float getExpectedCaloriesPerSecond()
  {
    return burndownChartBackEnd.getExpectedCaloriesPerSecond();
  }

  bool isExercising()
  {
    return burndownChartBackEnd.isExercising();
  }

  float getDelayValue()
  {
    return burndownChartBackEnd.getDelayValue();
  }

  void sufficientMovementInquiry(UserInformation userInformation, float movementValue)
  {
    burndownChartBackEnd.sufficientMovementInquiry(userInformation, movementValue);
  }

  // Constrain dynamic variables in front-end and back-end in real-time
  void controlConstraints()
  {
    controlNumberOfDataPointsInGraph();
    increaseSegments();
    // updateGraphVizuals();
  }

  bool checkIfUserAccomplishedGoal()
  {
    return burndownChartBackEnd.checkIfUserAccomplishedGoal();
  }

  float getTimeElapsed()
  {
    return burndownChartBackEnd.getTimeElapsed();
  }

  void updateTimeElapsed(float duration)
  {
    burndownChartBackEnd.updateTimeElapsed(duration);
  }

  // private:
  void controlNumberOfDataPointsInGraph()
  {
    burndownChartFrontEnd.controlNumberOfDataPointsInGraph();
  }

  void increaseSegments()
  {
    burndownChartBackEnd.increaseCurrentSegments();
  }

  void updateGraphVizuals()
  {
    burndownChartFrontEnd.updateGraphVizuals(burndownChartBackEnd);
  }
};