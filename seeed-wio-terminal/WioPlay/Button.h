#define BUTTON_NEXT WIO_KEY_A
#define BUTTON_PAUSE WIO_KEY_B
#define BUTTON_PREVIOUS WIO_KEY_C

class Button {
  public:
  void setup() {
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
};