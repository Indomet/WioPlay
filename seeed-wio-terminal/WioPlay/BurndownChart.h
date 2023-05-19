#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

BurndownChartBackEnd burndownChartBackEnd(300, 30, 0);  // (exerciseDuration, caloriesGoal, chosenActivityIdx)
BurndownChartFrontEnd burndownChartFrontEnd(20);        // (float graphUIXStartValue)

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

  /*
  // Note: Previous implementation that works as intended without MQTT
  bool isExercising() {
    return burndownChartBackEnd.isExercising();
  }
  */

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

  void resetExercise(float newExerciseDuration, float newCaloriesGoal, int newChosenActivityIdx) {
    burndownChartBackEnd.resetExerciseValues(newExerciseDuration, newCaloriesGoal, newChosenActivityIdx);
    burndownChartFrontEnd.resetChart();
  }

private:
  void controlNumberOfDataPointsInGraph() {
    burndownChartFrontEnd.controlNumberOfDataPointsInGraph();
  }
};