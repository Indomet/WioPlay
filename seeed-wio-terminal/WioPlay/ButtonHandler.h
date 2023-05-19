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
    // Check if button A is pressed
    if (digitalRead(BUTTON_NEXT) == LOW) {
      // Wait for button to be released
      do {
        delay(10);  // Wait for a short time
      } while (digitalRead(BUTTON_NEXT) == LOW);

      // play music when button A is pressed
      client.publish(Request_pub, "NEXT");
      Serial.println("a");
    }

    // Check if button B is pressed
    if (digitalRead(BUTTON_PAUSE) == LOW) {
      // Wait for button to be released
      do {
        delay(10);  // Wait for a short time
      } while (digitalRead(BUTTON_PAUSE) == LOW);

      // play music when button B is pressed
      player.toggle();
      Serial.println("b");
    }

    // Check if button C is pressed
    if (digitalRead(BUTTON_PREVIOUS) == LOW) {
      // Wait for button to be released
      do {
        delay(10);  // Wait for a short time
      } while (digitalRead(BUTTON_PREVIOUS) == LOW);

      //play music when button C is pressed
      client.publish(Request_pub, "PREVIOUS");
      Serial.println("c");
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