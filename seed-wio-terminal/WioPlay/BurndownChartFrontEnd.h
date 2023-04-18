TFT_eSPI tft;
TFT_eSprite spr = TFT_eSprite(&tft);

#include"TFT_eSPI.h"

#define MAX_SIZE 10 // maximum size of data displayed at once in graph
doubles data[2];

class BurndownChartFrontEnd
{
  public:
  float graphUIXStartValue; // X-coordinate in terminal for the graph to start at
  BurndownChartFrontEnd(float graphUIXStartValue)
  {
    this->graphUIXStartValue = graphUIXStartValue;
  }

  void initializeUI()
  {
    tft.begin();
    tft.setRotation(3);
    tft.setTextColor(TFT_BLACK); //set text color
    tft.setTextSize(3); //set text size

    spr.createSprite(TFT_HEIGHT, TFT_WIDTH);
    spr.setRotation(3);
  }

  // Make sure that the number of data-points displayed in the graph at once never exceeds the predetermined limit
  void controlNumberOfDataPointsInGraph()
  {
    if(data[0].size() >= MAX_SIZE)
    {
      removeEarliestDataPoints();
    }

    spr.fillSprite(TFT_WHITE);
  }

  // Update headers and graph-values in real-time
  void updateGraphVizuals(BurndownChartBackEnd burndownChartBackEnd)
  {
    spr.fillSprite(TFT_WHITE);

    // Settings for the line graph title
    auto header = text(80, 0)
                      .value("Burndown chart")
                      .width(spr.width())
                      .thickness(2);

    header.height(header.font_height(&spr) * 2); // * 2
    header.draw(&spr); // Header height is the twice the height of the font

    auto headerY = text(15, 18)
                  .value("Calories burned")
                  .backgroud(TFT_WHITE)
                  .color(TFT_RED)
                  .thickness(1);

    headerY.height(headerY.font_height(&spr) * 2);
    headerY.draw(&spr); // Header height is the twice the height of the font

    auto headerX = text(240, 190)
      .value("Time elapsed")
      .backgroud(TFT_WHITE)
      .thickness(1);

    headerX.height(headerX.font_height(&spr) * 2);
    headerX.draw(&spr); // Header height is the twice the height of the font

    vizualiseGraphValues(burndownChartBackEnd);
    // data[0].push(burndownChartBackEnd.caloriesBurnt);
    // data[1].push(burndownChartBackEnd.getExpectedValue());

    // Settings for the line graph
    auto content = line_chart(graphUIXStartValue, header.height()); //(x,y) where the line graph begins   auto content = line_chart(20, header.height());
    content
        .height(spr.height() - header.height() * 1.5) // actual height of the line chart
        .width(spr.width() - content.x() * 2)         // actual width of the line chart
        .based_on(0.0)                                // Starting point of y-axis, must be a float
        .show_circle(true, false)
        .value({data[0], data[1]})
        .max_size(MAX_SIZE)
        .color(TFT_RED, TFT_BLUE)
        .backgroud(TFT_WHITE)
        .draw(&spr);

    spr.pushSprite(0, 0);
  }

  void changeAttributeValues(float newGraphUIXStartValue)
  {
    graphUIXStartValue = newGraphUIXStartValue;
  }

  private:
  const byte numberOfGraphValues = 2;

  // Don't display the data-points added earliest on the graph. Delete them to sustain the memory limit
  void removeEarliestDataPoints()
  {
    for (byte i = 0; i<numberOfGraphValues; i++)
    {
      data[i].pop(); // Remove the first read variable
    }
  }

  void vizualiseGraphValues(BurndownChartBackEnd burndownChartBackEnd)
  {
    data[0].push(burndownChartBackEnd.caloriesBurnt);
    data[1].push(burndownChartBackEnd.getExpectedValue());
  }
};