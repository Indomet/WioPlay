TFT_eSPI tft;
#include "TFT_eSPI.h"
#include "RawImage.h"  // image processing library
// #include "image.h"
#define RIGHT_BUTTON WIO_5S_RIGHT  // Right joystick  WIO_5S_PRESS press is probably using same pins as the burndown chart
#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C
const char* imageList[] = { "/photos/ali.bmp", "/photos/Asim.bmp" };


class Scenes {
private:
  String songName;
  bool isOnMusicScene = true;
  bool messageReceived = false;
  int index = 0;
  const char* prevButton = "/photos/prev.bmp";
  const char* pauseButton = "/photos/pause.bmp";
  const char* nextButton = "/photos/next.bmp";

public:
  Scenes()
  /* : songImage(songImage) */ {
    this->songName = songName;
  }

  void playerScene() {
    if (isOnMusicScene) {
      tft.fillScreen(TFT_WHITE);
      tft.setTextColor(TFT_BLACK);
      tft.setTextSize(2);

      // const size_t capacity = JSON_OBJECT_SIZE(2500);
      // uint8_t buffer[capacity];

      // Serialize the JSON to the buffer
      // serializeJson(songImage, buffer, capacity);
      // tft.drawXBitmap(logo_x, logo_y, songImage, logo_width, logo_width, TFT_TRANSPARENT);

      drawImage<uint16_t>(imageList[index], 95, 25);
      tft.setCursor((320 - tft.textWidth(songName)) / 2, 165);
      tft.println(songName);
      drawImage<uint16_t>(prevButton, 100, 200);
      drawImage<uint16_t>(pauseButton, 150, 200);
      drawImage<uint16_t>(nextButton, 200, 200);
      isOnMusicScene = false;
    }
    if (messageReceived) {
      drawImage<uint16_t>(imageList[index], 95, 25);
      tft.fillRect(0, 165, tft.width(), tft.fontHeight() * 2, TFT_WHITE);
      tft.setCursor((320 - tft.textWidth(songName)) / 2, 165);
      tft.println(songName);
      messageReceived = false;
    }
  }


  void setupButton() {
    pinMode(RIGHT_BUTTON, INPUT_PULLUP);
    while (digitalRead(RIGHT_BUTTON) == LOW) {}

    pinMode(BUTTON_NEXT, INPUT);
    pinMode(BUTTON_PAUSE, INPUT);
    pinMode(BUTTON_PREVIOUS, INPUT);
  }

  void buttonOnPress() {
    if (digitalRead(BUTTON_NEXT) == LOW) {
      player.nextSong();
    }

    if (digitalRead(BUTTON_PAUSE) == LOW) {
      player.toggle();
    }

    if (digitalRead(BUTTON_PREVIOUS) == LOW) {
      player.previousSong();
    }
  }

  void menuNavigationOnPress(void (*firstScene)(), void (*secondScene)()) {
    if (digitalRead(RIGHT_BUTTON) == LOW) {
      secondScene();
      isOnMusicScene = true;
    } else {
      firstScene();
    }
  }

  void changeSongName(const char* newSongName) {
    this->songName = newSongName;

    index++;
    index = index % (sizeof(imageList) / sizeof(int));

    messageReceived = true;
  }

  // void changeArtwork(DynamicJsonDocument newSongImage) {
  //   this->songImage = newSongImage;
  //   messageReceived = true;
  // }
};
