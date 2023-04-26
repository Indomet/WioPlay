#include "Arduino.h"
#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

BurndownChartBackEnd burndownChartBackEnd (1000, 10, 20, 0); // (delayValue, exerciseDuration, caloriesGoal, chosenActivityIdx)
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
    std::string expected = std::to_string(burndownChartBackEnd.getExpectedCaloriesPerSecond());

    return actual + ", " + expected;
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

  // Control constraints such as A,B,C in real-time
  void controlConstraints()
  {
    controlNumberOfDataPointsInGraph();
    increaseSegments();
    updateGraphVizuals();
  }

  bool checkIfUserAccomplishedGoal()
  {
    return burndownChartBackEnd.checkIfUserAccomplishedGoal();
  }


  private:
  void controlNumberOfDataPointsInGraph()
  {
    burndownChartFrontEnd.controlNumberOfDataPointsInGraph();
  }

  void increaseSegments()
  {
    burndownChartBackEnd.currentSegments++;
  }

  void updateGraphVizuals()
  {
    burndownChartFrontEnd.updateGraphVizuals(burndownChartBackEnd);
  }
};