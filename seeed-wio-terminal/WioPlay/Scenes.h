TFT_eSPI tft;
<<<<<<< HEAD

#include "TFT_eSPI.h"
#include "RawImage.h" // image processing library

#define RIGHT_BUTTON WIO_5S_RIGHT // Right joystick  WIO_5S_PRESS press is probably using same pins as the burndown chart
#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C

bool isOnMusicScene = true;
bool messageReceived = false;
const char *artwork = "/photos/ali.bmp";
const char *prevButton = "/photos/prev.bmp";
const char *pauseButton = "/photos/pause.bmp";
const char *nextButton = "/photos/next.bmp";
String songName;

void updateSongName(const char *newSongName)
{
  songName = newSongName;
  messageReceived = true;
}

void showPlayerScene()
{

  if (isOnMusicScene)
  {

    tft.fillScreen(TFT_WHITE);   // declare a backgroundColor
    tft.setTextColor(TFT_BLACK); // initializee the text color to white
    tft.setTextSize(2);
    // songName = "Song Name here";
    drawImage<uint16_t>(artwork, 95, 25);

    // Update TFT display and print input message
    // tft.setTextColor(textColor, bgColor);  // set the text and background color

    tft.setCursor((320 - tft.textWidth(songName)) / 2, 165); // Make sure to align the text to the center of the screen
    tft.println(songName);                                   // print the text
                                                             // display the buttons
    drawImage<uint16_t>(prevButton, 100, 200);
    drawImage<uint16_t>(pauseButton, 150, 200);
    drawImage<uint16_t>(nextButton, 200, 200);

    isOnMusicScene = false;
  }

  if (messageReceived)
  {
    // this is to fill the previous song name with white so that we dont write it on top of the previous text
    tft.fillRect(0, 165, tft.width(), tft.fontHeight() * 2, TFT_WHITE);
    tft.setCursor((320 - tft.textWidth(songName)) / 2, 165); // Make sure to align the text to the center of the screen
    tft.println(songName);                                   // display the new song name
    messageReceived = false;
  }
}

void setupButton()
{
  pinMode(RIGHT_BUTTON, INPUT_PULLUP);

  while (digitalRead(RIGHT_BUTTON) == LOW)
  {
    // Wait for button to be released
=======
#include "TFT_eSPI.h" 
#include "RawImage.h"  // image processing library
// #include "image.h"
#define RIGHT_BUTTON WIO_5S_RIGHT  // Right joystick  WIO_5S_PRESS press is probably using same pins as the burndown chart
#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C
const char* imageList[] = { "/photos/Ali.bmp", "/photos/Asim.bmp", "/photos/Zepei.bmp", "/photos/Joel.bmp", 
"/photos/Jackson.bmp", "/photos/Mohamad.bmp" ,"/photos/Ali2.bmp", "/photos/Zepei2.bmp", "/photos/Asim3.bmp"};


class Scenes {
private:
  String songName;
  bool isOnMusicScene = true;
  bool messageReceived = false;
  int index = 0;
  const char* prevButton = "/photos/prev.bmp";
  const char* pauseButton = "/photos/pause.bmp";
  const char* nextButton = "/photos/next.bmp";
  const unsigned long UPDATE_INTERVAL = 1500;  // update interval in milliseconds //non blocking  delay
  unsigned long lastUpdateTime = 0;            // time of last update  //non blocking  delay

public:
  Scenes() {
    this->songName = songName;
  }

  void playerScene() {
    if (isOnMusicScene) {
      tft.fillScreen(TFT_WHITE);
      tft.setTextColor(TFT_BLACK);
      tft.setTextSize(2);

      drawImage<uint16_t>(imageList[index], 95, 25); 

      tft.setCursor((320 - tft.textWidth(songName)) / 2, 165); // location to display music name on the screen
      tft.println(songName); // display the music name on the screen

      drawImage<uint16_t>(prevButton, 100, 200);
      drawImage<uint16_t>(pauseButton, 150, 200);
      drawImage<uint16_t>(nextButton, 200, 200);
      isOnMusicScene = false;
    }
    if (messageReceived) {
      drawImage<uint16_t>(imageList[index], 95, 25); //change the image and reprint
      tft.fillRect(0, 165, tft.width(), tft.fontHeight() * 2, TFT_WHITE); // delete the old music name on the display
      tft.setCursor((320 - tft.textWidth(songName)) / 2, 165); // location to display music name on the screen
      tft.println(songName);  display the new music name on the screen
      messageReceived = false;
    }
>>>>>>> main
  }


<<<<<<< HEAD
void buttonOnPress()
{
  // Check if button A is pressed
  if (digitalRead(BUTTON_NEXT) == LOW)
  {
    // Wait for button to be released
    do
    {
      // delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_NEXT) == LOW);
=======
  void setupButton() {
    pinMode(RIGHT_BUTTON, INPUT_PULLUP);
    while (digitalRead(RIGHT_BUTTON) == LOW) {}
>>>>>>> main

    pinMode(BUTTON_NEXT, INPUT);
    pinMode(BUTTON_PAUSE, INPUT);
    pinMode(BUTTON_PREVIOUS, INPUT);
  }

<<<<<<< HEAD
  // Check if button B is pressed
  if (digitalRead(BUTTON_PAUSE) == LOW)
  {
    // Wait for button to be released
    do
    {
      // delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_PAUSE) == LOW);
=======
  void buttonOnPress() {
    if (digitalRead(BUTTON_NEXT) == LOW) {
      player.nextSong();
    }
>>>>>>> main

    if (digitalRead(BUTTON_PAUSE) == LOW) {
      player.toggle();
    }

    if (digitalRead(BUTTON_PREVIOUS) == LOW) {
      player.previousSong();
    }
  }

<<<<<<< HEAD
  // Check if button C is pressed
  if (digitalRead(BUTTON_PREVIOUS) == LOW)
  {
    // Wait for button to be released
    do
    {
      // delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_PREVIOUS) == LOW);

    // play music when button C is pressed
    player.previousSong();
  }
}

void menuNavigationOnPress(void (*firstScene)(), void (*secondScene)())
{
  if (digitalRead(RIGHT_BUTTON) == LOW)
  {
    // Button is held down
    secondScene();
    isOnMusicScene = true; // update the music player sceen only when the button is released
  }
  else
  {
    // Button is not held down
    firstScene();
=======
  void menuNavigationOnPress(void (*firstScene)(), void (*secondScene)()) {
    unsigned long currentTime = millis();
    if (currentTime - lastUpdateTime >= UPDATE_INTERVAL) { // non blocking delay
      lastUpdateTime = currentTime;
      if (digitalRead(RIGHT_BUTTON) == LOW) {
        secondScene();
        isOnMusicScene = true;
      } else {
        firstScene();
      }
    }
  }

  void changeSongName(const char* newSongName) {
    this->songName = newSongName;

    index++;
    index = index % (sizeof(imageList) / sizeof(int));

    messageReceived = true;
>>>>>>> main
  }

};
