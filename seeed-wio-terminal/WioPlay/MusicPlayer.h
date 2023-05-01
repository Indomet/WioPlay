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
  int pauseDuration;
  int currentPauseChunkDuration;

public:
  MusicPlayer(int* song, int size, float tempo) {
    this->song = song;
    this->size = size;
    this->tempo = tempo;
    this->position = 0;

    // this is the approximate duration between notes calculated based on the provided tempo
    pauseDuration = GENERIC_DURATION * (1 / tempo);
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
    if (song[position] != 0) {
      tone(1, song[position], GENERIC_DURATION);
    }

    delay(pauseDuration);
    noTone(1);
  }

public:
  void playChunk() {
    currentPauseChunkDuration = 0;

    for (int i = this->position; i < this->position + CHUNK_SIZE; i++) {
      play(i);
      registerIncreasedChunkDuration();
    }
    this->bumpPosition();
  }

public:
  int getPosition() {
    return this->position;
  }

public:
  void registerIncreasedChunkDuration() {
    currentPauseChunkDuration += pauseDuration;
  }

public:
  float getCurrentPauseChunkDuration() {
    return currentPauseChunkDuration;
  }
};