#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

BurndownChartBackEnd burndownChartBackEnd(10, 5, 0);  // (exerciseDuration, caloriesGoal, chosenActivityIdx)
BurndownChartFrontEnd burndownChartFrontEnd(20);         // (float graphUIXStartValue)

class BurndownChart {
public:
  void initializeUI() {
    burndownChartFrontEnd.initializeUI();
  }

  void displayExerciseResults() {
    burndownChartFrontEnd.displayExerciseResults(burndownChartBackEnd);
  }

  float getGeneralExpectedCaloriesPerSecond() {
    return burndownChartBackEnd.getGeneralExpectedCaloriesPerSecond();
  }

  float getUpdateDelay() {
    return burndownChartBackEnd.getUpdateDelay();
  }

  float getActualCaloriesPerSecond() {
    return burndownChartBackEnd.getActualCaloriesPerSecond();
  }

  float getExpectedCaloriesPerSecond() {
    return burndownChartBackEnd.getExpectedCaloriesPerSecond();
  }

  bool isExercising() {
    return burndownChartBackEnd.isExercising();
  }

  void sufficientMovementInquiry(UserInformation userInformation, float movementValue) {
    burndownChartBackEnd.sufficientMovementInquiry(userInformation, movementValue);
  }

  // Constrain dynamic variables in front-end and back-end in real-time
  void controlConstraints() {
    controlNumberOfDataPointsInGraph();
  }

  bool checkIfUserAccomplishedGoal() {
    return burndownChartBackEnd.checkIfUserAccomplishedGoal();
  }

  float getTimeElapsed() {
    return burndownChartBackEnd.getTimeElapsed();
  }

  void updateTimeElapsed() {
    burndownChartBackEnd.updateTimeElapsed();
  }

  void updateGraphVizuals() {
    burndownChartFrontEnd.updateGraphVizuals(burndownChartBackEnd);
  }

private:
  void controlNumberOfDataPointsInGraph() {
    burndownChartFrontEnd.controlNumberOfDataPointsInGraph();
  }
};