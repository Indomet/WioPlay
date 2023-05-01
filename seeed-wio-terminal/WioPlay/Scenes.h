TFT_eSPI tft;

#include "TFT_eSPI.h"
#include "RawImage.h"  // image processing library


#define RIGHT_BUTTON WIO_5S_RIGHT  // Right joystick
#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C

bool isOnMusicScene = true;
const char* ali = "/photos/ali.bmp";
const char* prevButton = "/photos/prev.bmp";
const char* pauseButton = "/photos/pause.bmp";
const char* nextButton = "/photos/next.bmp";


void showPlayerScene() {
  if (isOnMusicScene) {
    tft.fillScreen(TFT_WHITE);
    drawImage<uint16_t>(ali, 95, 35);
    drawImage<uint16_t>(prevButton, 100, 200);
    drawImage<uint16_t>(pauseButton, 150, 200);
    drawImage<uint16_t>(nextButton, 200, 200);
    isOnMusicScene = false;
  }
}

void setupButton() {
  pinMode(RIGHT_BUTTON, INPUT_PULLUP);

  while (digitalRead(RIGHT_BUTTON) == LOW) {
    // Wait for button to be released
  }

  pinMode(BUTTON_NEXT, INPUT_PULLUP);
  pinMode(BUTTON_PAUSE, INPUT_PULLUP);
  pinMode(BUTTON_PREVIOUS, INPUT_PULLUP);
}

void buttonOnPress() {
  // Check if button A is pressed
  if (digitalRead(BUTTON_NEXT) == LOW) {
    // Wait for button to be released
    do {
      delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_NEXT) == LOW);

    // play music when button A is pressed
    Serial.println("a");
  }

  // Check if button B is pressed
  if (digitalRead(BUTTON_PAUSE) == LOW) {
    // Wait for button to be released
    do {
      delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_PAUSE) == LOW);

    // play music when button B is pressed
    Serial.println("b");
  }

  // Check if button C is pressed
  if (digitalRead(BUTTON_PREVIOUS) == LOW) {
    // Wait for button to be released
    do {
      delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_PREVIOUS) == LOW);

    //play music when button C is pressed
    Serial.println("c");
  }
}

void menuNavigationOnPress(void (*firstScene)(), void (*secondScene)()) {
  if (digitalRead(RIGHT_BUTTON) == LOW) {
    // Button is held down
    secondScene();
    isOnMusicScene = true;  // update the music player sceen only when the button is released
  } else {
    // Button is not held down
    firstScene();
  }
  delay(100);
}
