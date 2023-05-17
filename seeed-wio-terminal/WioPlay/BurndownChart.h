#include "BurndownChartBackEnd.h"
#include "BurndownChartFrontEnd.h"

BurndownChartBackEnd burndownChartBackEnd(80, 300, 0);  // (exerciseDuration, caloriesGoal, chosenActivityIdx)
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

  float getActualCaloriesPerSecond() {
    return burndownChartBackEnd.getActualCaloriesPerSecond();
  }

  float getExpectedCaloriesPerSecond() {
    return burndownChartBackEnd.getExpectedCaloriesPerSecond();
  }

  bool isExercising() {
    return burndownChartBackEnd.isExercising();
  }

  void sufficientMovementInquiry(UserInformation userInformation, float movementValue, float updateDelay) {
    burndownChartBackEnd.sufficientMovementInquiry(userInformation, movementValue, updateDelay);
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

  void resetExercise(float newExerciseDuration, float newCaloriesGoal, int newChosenActivityIdx) {
    burndownChartBackEnd.resetExerciseValues(newExerciseDuration, newCaloriesGoal, newChosenActivityIdx);
    burndownChartFrontEnd.resetChart();

    /*
    if (tempCounter > 1)
    {
      burndownChartFrontEnd.resetChart();
    }
    */    
  }

private:
  void controlNumberOfDataPointsInGraph() {
    burndownChartFrontEnd.controlNumberOfDataPointsInGraph();
  }
};