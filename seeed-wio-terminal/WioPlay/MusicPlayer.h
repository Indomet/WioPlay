#include "pitches.h"
#include "themes.h"

class MusicPlayer {

  static const int GENERIC_DURATION = 1000 / 5;
  static const int CHUNK_SIZE = 5;

private:
  int* song;
  int size;
  int tempo;
  int position;

public:
  MusicPlayer(int* song, int size, float tempo) {
    this->song = song;
    this->size = size;
    this->tempo = tempo;
    this->position = 0;
  }

private:
  void bumpPosition() {
    if (this->position + 5 < this->size) {
      this->position = this->position + 5;
    } else {
      this->position = 0;
    }
  }

private:
  void play(int position) {

    // this is the approximate duration between notes calculated based on the provided tempo
    int pauseDuration = GENERIC_DURATION * (1 / tempo);

    if (song[position] != 0) {
      tone(1, song[position], GENERIC_DURATION);
    }

    delay(pauseDuration);
    noTone(1);
  }

public:

  void playChunk() {
    for (int i = this->position; i < this->position + CHUNK_SIZE; i++) {
      play(i);
    }
    this->bumpPosition();
  }

  int getPosition() {
    return this->position;
  }
};