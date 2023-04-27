class BurndownChartBackEnd // Has the responsibility of dealing with logic and functionality of the burndown chart
{
  public:
  BurndownChartBackEnd(float delayValue, float exerciseDuration, float caloriesGoal, byte chosenActivityIdx)
  {
    this->delayValue = delayValue;
    this->exerciseDuration = exerciseDuration;
    this->caloriesGoal = caloriesGoal;
    this->chosenActivityIdx = chosenActivityIdx;

    caloriesBurnt = 0;
    currentSegments = 0;
    timeElapsed = 0;
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

  float getCaloriesBurnt()
  {
    return caloriesBurnt;
  }

  float getExpectedValue()
  {
    return ((currentSegments - 1) / totalNumberOfSegments) * caloriesGoal;    
  }

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

// Only burn calories if user's movement-intensity corresponds with selected exercise
void sufficientMovementInquiry(UserInformation userInformation, float movementValue)
{
  if (userIsMovingFastEnough(movementValue))
  {
    caloriesBurnt += burnCalories(userInformation, movementValue);
  }
  else
  {
    Serial.println(" ");
    // Serial.println("You are not exercising hard enough for the selected exercise!");
  }
}

float getDelayValue()
{
  return delayValue;
}

void changeAttributeValues(float newDelayValue, float newExerciseDuration, float newCaloriesGoal, byte newChosenActivityIdx)
{
  delayValue = newDelayValue;
  exerciseDuration = newExerciseDuration;
  caloriesGoal = newCaloriesGoal;
  chosenActivityIdx = newChosenActivityIdx;
}

bool checkIfUserAccomplishedGoal()
{
  return caloriesBurnt >= caloriesGoal;
}

/*
float getActualCaloriesPerSecond()
{
  return caloriesBurnt / exerciseDuration; // Note: For now, we assume that 'exerciseDuration' is measured in seconds
}
*/

// Returns the calories burnt per second at a given point of time. If 'timeElapsed' = 'exerciseDuration', the method gets the calories burnt across the entire workout
float getActualCaloriesPerSecond()
{
  return caloriesBurnt / (timeElapsed / 1000);
}

// TODO: Adjust method below
// Functioanlity to add: "Draw 'expected line from current-calories burnt to 'goal', read the calories needed for that and the seconds left to do it and calculate expectedCaloriesPerSecond"
float getExpectedCaloriesPerSecond()
{
  return caloriesGoal / exerciseDuration;
}

void increaseCurrentSegments()
{
  currentSegments++;
}

float getTimeElapsed()
{
  return timeElapsed;
}

void updateTimeElapsed(float duration)
{
  timeElapsed += duration;
}

private:
float delayValue;
float standard;
float minMovement; // Minimal movement required for specific exercise (Deals with cases where user isn't moving enough in accordance with selected exercise)
float maxMovement; // Maximal movement required for specific exercise (Handles the case where user selected 'Walking' but is running in reality)
float proportionalConstant;
byte chosenActivityIdx; // 0
float totalNumberOfSegments; // Number of update segments in graph until goal is reached
float currentSegments;
float balanceFactor;
float timeElapsed;

float exerciseDuration; // 30 (Seconds)
float caloriesGoal; // 100 --> Put in 'ExerciseSettings'
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

float getMETValue(float movementValue)
{
  return movementValue * proportionalConstant;
}

bool userIsMovingFastEnough(float movementValue)
{
  return movementValue >= minMovement;
}
};