#include "Arduino.h"
#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

BurndownChartBackEnd burndownChartBackEnd (1000, 20, 20, 0); // (delayValue, exerciseDuration, caloriesGoal, chosenActivityIdx)
BurndownChartFrontEnd burndownChartFrontEnd (20); // (float graphUIXStartValue)

class BurndownChart
{
  public:
  void initializeUI()
  {
    burndownChartFrontEnd.initializeUI();
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