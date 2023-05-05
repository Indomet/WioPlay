#include "Arduino.h"
#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

BurndownChartBackEnd burndownChartBackEnd(40, 20, 0);  // (exerciseDuration, caloriesGoal, chosenActivityIdx)
BurndownChartFrontEnd burndownChartFrontEnd(20);             // (float graphUIXStartValue)

#include <iostream>
#include <string>

class BurndownChart {
public:
  void initializeUI() {
    burndownChartFrontEnd.initializeUI();
  }

  void displayExerciseResults()
  {
    burndownChartFrontEnd.displayExerciseResults(burndownChartBackEnd);
  }

  float getGeneralExpectedCaloriesPerSecond() {
    return burndownChartBackEnd.getGeneralExpectedCaloriesPerSecond();
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

  void sufficientMovementInquiry(UserInformation userInformation, float movementValue, float songPauseChunkDuration) {
    burndownChartBackEnd.sufficientMovementInquiry(userInformation, movementValue, songPauseChunkDuration);
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

  void updateTimeElapsed(float duration) {
    burndownChartBackEnd.updateTimeElapsed(duration);
  }

  void updateGraphVizuals() {
    burndownChartFrontEnd.updateGraphVizuals(burndownChartBackEnd);
  }

private:
  void controlNumberOfDataPointsInGraph() {
    burndownChartFrontEnd.controlNumberOfDataPointsInGraph();
  }
};