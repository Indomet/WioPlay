TFT_eSPI tft;
#include "TFT_eSPI.h"
#include "RawImage.h"  // image processing library

const char* imageList[] = { "/photos/Ali.bmp", "/photos/Asim.bmp", "/photos/Zepei.bmp", "/photos/Joel.bmp", "/photos/Jackson.bmp",
                            "/photos/Mohamad.bmp", "/photos/Ali2.bmp", "/photos/Zepei2.bmp", "/photos/Asim3.bmp" };

class Scenes {
public:
  bool isOnMusicScene = true;

private:
  String songName;
  bool messageReceived = false;
  int index = 0;
  const char* prevButton = "/photos/prev.bmp";
  const char* pauseButton = "/photos/pause.bmp";
  const char* nextButton = "/photos/next.bmp";

public:
  Scenes() {
    this->songName = songName;
  }

public:
  void playerScene() {
    if (isOnMusicScene) {
      tft.fillScreen(TFT_WHITE);
      tft.setTextColor(TFT_BLACK);
      tft.setTextSize(2);

      drawImage<uint16_t>(imageList[index], 95, 25);

      tft.drawString(songName, (320 - tft.textWidth(songName)) / 2, 165);  // display the music name on the screen

      drawImage<uint16_t>(prevButton, 100, 200);
      drawImage<uint16_t>(pauseButton, 150, 200);
      drawImage<uint16_t>(nextButton, 200, 200);
      isOnMusicScene = false;
    }
    if (messageReceived) {
      drawImage<uint16_t>(imageList[index], 95, 25);                       //change the image and reprint
      tft.fillRect(0, 165, tft.width(), tft.fontHeight() * 2, TFT_WHITE);  // delete the old music name on the display
      tft.drawString(songName, (320 - tft.textWidth(songName)) / 2, 165);  // display the new music name on the screen
      messageReceived = false;
    }
  }

public:
  void changeSongName(const char* newSongName) {
    this->songName = newSongName;

    index++;
    index = index % (sizeof(imageList) / sizeof(int)); 

    messageReceived = true;
  }
};
