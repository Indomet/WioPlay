#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C
#define RIGHT_BUTTON WIO_5S_RIGHT  // Right joystick  WIO_5S_PRESS press is probably using same pins as the burndown chart

class ButtonHandler {
private:
  const unsigned long UPDATE_INTERVAL = 1500;  // update interval in milliseconds //non blocking  delay
  unsigned long lastUpdateTime = 0;            // time of last update  //non blocking  delay
  
public:
  void setup() {
    pinMode(RIGHT_BUTTON, INPUT_PULLUP);
    while (digitalRead(RIGHT_BUTTON) == LOW) {}
    pinMode(BUTTON_NEXT, INPUT);
    pinMode(BUTTON_PAUSE, INPUT);
    pinMode(BUTTON_PREVIOUS, INPUT);
  }

public:
  void onPress() {
    if (digitalRead(BUTTON_NEXT) == LOW) {
      client.publish(Request_pub, "NEXT");
    }

    if (digitalRead(BUTTON_PAUSE) == LOW) {
      player.toggle();
    }

    if (digitalRead(BUTTON_PREVIOUS) == LOW) {
      client.publish(Request_pub, "PREVIOUS");
    }
  }

  void menuNavigationOnPress(void (*firstScene)(), void (*secondScene)()) {
    unsigned long currentTime = millis();
    if (currentTime - lastUpdateTime >= UPDATE_INTERVAL) {  // non blocking delay
      lastUpdateTime = currentTime;
      if (digitalRead(RIGHT_BUTTON) == LOW) {
        secondScene();
        scenes.isOnMusicScene = true;
      } else {
        firstScene();
      }
    }
  }
};