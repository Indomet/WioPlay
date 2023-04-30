TFT_eSPI tft;

#include "TFT_eSPI.h"

#define BUTTON_PIN 32

void showFirstScene() {
  tft.fillScreen(TFT_BLUE);
  tft.setCursor(0, 0);
  tft.setTextColor(TFT_WHITE);
  tft.setTextSize(2);
  tft.println("Main Menu");
}



void setupButton() {
  pinMode(BUTTON_PIN, INPUT_PULLUP);

  while (digitalRead(BUTTON_PIN) == LOW) {
    // Wait for button to be released
  }
}

// void setup() {
//   tft.begin();
//   tft.setRotation(3);

//   setupButton();
// }

void menuNavigationOnPress(void (*firstScene)(), void (*secondScene)()) {
  if (digitalRead(BUTTON_PIN) == LOW) {
    // Button is held down
    secondScene();
  } else {
    // Button is not held down
    firstScene();
  }
  delay(100);
}
// void loop() {
//   FirstScenes firstScenes;
//   SecondScenes secondScenes;

//   menuNavigationOnPress(&firstScenes, &secondScenes);
// }

