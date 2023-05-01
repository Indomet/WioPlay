TFT_eSPI tft;

#include "TFT_eSPI.h"
#include "Seeed_FS.h"  // SD card library
#include "RawImage.h"  // image processing library


#define BUTTON_PIN WIO_5S_RIGHT  // left joystick
#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C

bool isOnFirstscene = true;
const char* img1_path = "/photos/prev.bmp";
const char* img2_path = "/photos/pause.bmp";
const char* img3_path = "/photos/next.bmp";


void showFirstScene() {
  if(isOnFirstscene){
  tft.fillScreen(TFT_WHITE);
  drawImage<uint16_t>(img1_path, 100,200);
  drawImage<uint16_t>(img2_path, 150,200);
  drawImage<uint16_t>(img3_path, 200,200);
  isOnFirstscene= false;
  }
}



void setupButton() {
  while (!SD.begin(SDCARD_SS_PIN, SDCARD_SPI)) {
  Serial.print("ERROR sd card not recognized");
  }
  pinMode(BUTTON_PIN, INPUT_PULLUP);

  while (digitalRead(BUTTON_PIN) == LOW) {
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
// void setup() {
//   tft.begin();
//   tft.setRotation(3);

//   setupButton();
// }

void menuNavigationOnPress(void (*firstScene)(), void (*secondScene)()) {
  if (digitalRead(BUTTON_PIN) == LOW) {
    // Button is held down
    secondScene();
    isOnFirstscene=true; // update the music player sceen only when the button is released
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

/*
#include "TFT_eSPI.h"
#include "Seeed_FS.h"  // SD card library
#include "RawImage.h"  // image processing library
TFT_eSPI tft;

#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C

const char* img1_path = "/photos/prev.bmp";
const char* img2_path = "/photos/pause.bmp";
const char* img3_path = "/photos/next.bmp";

void setup() {
  Serial.begin(115200);
  //Initialise SD card
  while (!SD.begin(SDCARD_SS_PIN, SDCARD_SPI)) {
    Serial.print("ERROR sd card not recognized");
  }
  pinMode(BUTTON_NEXT, INPUT_PULLUP);
  pinMode(BUTTON_PAUSE, INPUT_PULLUP);
  pinMode(BUTTON_PREVIOUS, INPUT_PULLUP);
  // while (digitalRead(BUTTON_NEXT) == LOW) {
  //   // Wait for button to be released
  // }
  //  while (digitalRead(BUTTON_PAUSE) == LOW) {
  //   // Wait for button to be released
  // }
  //  while (digitalRead(BUTTON_PREVIOUS) == LOW) {
  //   // Wait for button to be released
  // }
  tft.begin();
  tft.setRotation(3);
  tft.fillScreen(TFT_WHITE);
  drawImage<uint16_t>(img1_path, 100,200);
  drawImage<uint16_t>(img2_path, 150,200);
  drawImage<uint16_t>(img3_path, 200,200);
}


void loop() {

  buttonOnPress();
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


*/
