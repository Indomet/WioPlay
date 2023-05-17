

#define MAX_SIZE 30  // maximum size of data displayed at once in graph
doubles data[2];

class BurndownChartFrontEnd {
public:
  float graphUIXStartValue;  // X-coordinate in terminal for the graph to start at
  BurndownChartFrontEnd(float graphUIXStartValue) {
    this->graphUIXStartValue = graphUIXStartValue;
  }

  void initializeUI() {
    tft.begin();
    tft.setRotation(3);
    tft.setTextColor(TFT_BLACK);  //set text color
    tft.setTextSize(3);           //set text size
  }

  // Make sure that the number of data-points displayed in the graph at once never exceeds the predetermined limit
  void controlNumberOfDataPointsInGraph() {
    if (data[0].size() >= MAX_SIZE) {
      removeEarliestDataPoints();
    }
  }

  // Update headers and graph-values in real-time
  void updateGraphVizuals(BurndownChartBackEnd burndownChartBackEnd) {
    tft.fillScreen(TFT_WHITE);

    // Settings for the line graph title
    auto header = text(80, 0)
                    .value("Burndown chart")
                    .width(tft.width())
                    .thickness(2);

    header.height(header.font_height(&tft) * 2);  // * 2
    header.draw(&tft);                            // Header height is the twice the height of the font

    auto headerY = text(15, 18)
                     .value("Calories burned")
                     .backgroud(TFT_WHITE)
                     .color(TFT_RED)
                     .thickness(1);

    headerY.height(headerY.font_height(&tft) * 2);
    headerY.draw(&tft);  // Header height is the twice the height of the font

    auto headerX = text(240, 190)
                     .value("Time elapsed")
                     .backgroud(TFT_WHITE)
                     .thickness(1);

    headerX.height(headerX.font_height(&tft) * 2);
    headerX.draw(&tft);  // Header height is the twice the height of the font

    vizualiseGraphValues(burndownChartBackEnd);

    // Settings for the line graph
    auto content = line_chart(graphUIXStartValue, header.height());  //(x,y) where the line graph begins   auto content = line_chart(20, header.height());
    content
      .height(tft.height() - header.height() * 1.5)  // actual height of the line chart
      .width(tft.width() - content.x() * 2)          // actual width of the line chart
      .based_on(0.0)                                 // Starting point of y-axis, must be a float
      .show_circle(true, false)
      .value({ data[0], data[1] })
      .max_size(MAX_SIZE)
      .color(TFT_RED, TFT_BLUE)
      .backgroud(TFT_WHITE)
      .draw(&tft);
  }

  void displayExerciseResults(BurndownChartBackEnd burndownChartBackEnd) {
    tft.setTextSize(3);

    displayCalorieSecondComparison(burndownChartBackEnd);

    // User completed goal
    if (burndownChartBackEnd.checkIfUserAccomplishedGoal()) {
      tft.fillScreen(TFT_GREEN);
      tft.drawString("Accomplished goal!", exerciseResultTextCoordinates[0], exerciseResultTextCoordinates[1]);
    }

    // User didn't attain the set goal
    else {
      tft.fillScreen(TFT_RED);
      tft.drawString("Menu here!", exerciseResultTextCoordinates[0], exerciseResultTextCoordinates[1]);
    }
  }

  void displayCalorieSecondComparison(BurndownChartBackEnd burndownChartBackEnd) {
    String caloriesPerSecondComparison = String(burndownChartBackEnd.displayCalorieStatistics().c_str());
    tft.drawString(caloriesPerSecondComparison, caloriesPerSecondTextCoordinates[0], caloriesPerSecondTextCoordinates[1]);
  }

  void resetChart()
  {
      for (byte i = 0; i < numberOfGraphValues; i++)
      {
        for (byte j = 0; j < 29; j++) // maxDataPoints - 1
        {
          data[i].pop();
        }        
      }
  }

private:
  const byte numberOfGraphValues = 2;
  byte caloriesPerSecondTextCoordinates[2] = { 5, 50 };
  byte exerciseResultTextCoordinates[2] = { 15, 70 };
  const int maxDataPoints = 30;

  // Don't display the data-points added earliest on the graph. Delete them to sustain the memory limit
  void removeEarliestDataPoints() {
    for (byte i = 0; i < numberOfGraphValues; i++) {
      data[i].pop();  // Remove the first read variable
    }

    /*
      for (byte i = 0; i < numberOfGraphValues; i++)
      {
        for (byte j = 0; j < maxDataPoints - 1; j++)
        {
          data[i].pop();
        }        
      }
    */
  }

  void vizualiseGraphValues(BurndownChartBackEnd burndownChartBackEnd) {
    data[0].push(burndownChartBackEnd.getCaloriesBurnt());
    data[1].push(burndownChartBackEnd.getExpectedValue());
  }
};
