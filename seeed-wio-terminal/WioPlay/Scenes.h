TFT_eSPI tft;

#include "TFT_eSPI.h"
#include "RawImage.h"  // image processing library
#include "image.h"


#define RIGHT_BUTTON WIO_5S_RIGHT  // Right joystick  WIO_5S_PRESS press is probably using same pins as the burndown chart
#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C

bool isOnMusicScene = true;
bool messageReceived = false;
// const char* artwork = "/photos/ali.bmp";
// const char* images = "/image.bmp";
const char* prevButton = "/photos/prev.bmp";
const char* pauseButton = "/photos/pause.bmp";
const char* nextButton = "/photos/next.bmp";
String songName;


void changeSongName(const char* newSongName) {
  songName = newSongName;
  messageReceived = true;
}

void changeArtwork(const char* newSongImage){
// for (int i = 0; i < 2448; i++) {
//     songImage[i] = newSongImage[i];
// }
songImage =newSongImage;
}

void showPlayerScene() {

  if (isOnMusicScene) {

    tft.fillScreen(TFT_WHITE);    // declare a backgroundColor
    tft.setTextColor(TFT_BLACK);  // initializee the text color to white
    tft.setTextSize(2);
    // songName = "Song Name here";
    //  bmpDraw(image, logo_x, logo_y );// drawImage<uint16_t>(artwork, 95, 25);

    // Update TFT display and print input message
    // tft.setTextColor(textColor, bgColor);  // set the text and background color
    //   uint16_t* image16 = new uint16_t[sizeof(image) / sizeof(unsigned char) * 2];

    //   for (int i = 0; i < sizeof(image); i++) {
    //   uint16_t pixel = image[i];
    //   pixel <<= 8;
    //   i++;
    //   pixel |= image[i];
    //   image16[i / 2] = pixel;
    // }
    //  tft.drawImage<uint16_t>(image, 95, 25);
    // tft.drawBitmap(image, 95, 25, logo_width, logo_height, TFT_WHITE);
    // drawImage<uint16_t>(images, 95, 25);
    // tft.drawXBitmap(logo_x, logo_y, image, logo_width, logo_width, TFT_GREEN, TFT_BLACK );
    tft.drawXBitmap(logo_x, logo_y, songImage, logo_width, logo_width, TFT_TRANSPARENT);// working
    // tft.pushImage(logo_x, logo_y, logo_width, logo_height, songImage);  //started not working

    // tft.drawBitmap(logo_x, logo_y, reinterpret_cast<const uint8_t*>(image), logo_width, logo_width, TFT_TRANSPARENT);


    tft.setCursor((320 - tft.textWidth(songName)) / 2, 165);  // Make sure to align the text to the center of the screen
    tft.println(songName);                                    // print the text
                                                              
    drawImage<uint16_t>(prevButton, 100, 200);
    drawImage<uint16_t>(pauseButton, 150, 200);
    drawImage<uint16_t>(nextButton, 200, 200);

    isOnMusicScene = false;
  }

  if (messageReceived) {
    // this is to fill the previous song name with white so that we dont write it on top of the previous text
    tft.fillRect(0, 165, tft.width(), tft.fontHeight() * 2, TFT_WHITE);
    tft.setCursor((320 - tft.textWidth(songName)) / 2, 165);  // Make sure to align the text to the center of the screen
    tft.println(songName);                                    // display the new song name
    messageReceived = false;
  }
}


void setupButton() {
  pinMode(RIGHT_BUTTON, INPUT_PULLUP);

  while (digitalRead(RIGHT_BUTTON) == LOW) {
    // Wait for button to be released
  }

  pinMode(BUTTON_NEXT, INPUT);
  pinMode(BUTTON_PAUSE, INPUT);
  pinMode(BUTTON_PREVIOUS, INPUT);
}

void buttonOnPress() {
  // Check if button A is pressed
  if (digitalRead(BUTTON_NEXT) == LOW) {
    // Wait for button to be released
    do {
      // delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_NEXT) == LOW);

    player.nextSong();
  }

  // Check if button B is pressed
  if (digitalRead(BUTTON_PAUSE) == LOW) {
    // Wait for button to be released
    do {
      // delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_PAUSE) == LOW);

    // play music when button B is pressed
    player.toggle();
  }

  // Check if button C is pressed
  if (digitalRead(BUTTON_PREVIOUS) == LOW) {
    // Wait for button to be released
    do {
      // delay(10);  // Wait for a short time
    } while (digitalRead(BUTTON_PREVIOUS) == LOW);

    //play music when button C is pressed
    player.previousSong();
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
}
