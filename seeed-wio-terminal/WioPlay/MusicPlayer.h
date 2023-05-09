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
  bool isPaused;

public:
  MusicPlayer(int* song, int size, float tempo) {
    this->song = song;
    this->size = size;
    this->tempo = tempo;
    this->position = 0;
    this->isPaused = false;

    // this is the approximate duration between notes calculated based on the provided tempo
    pauseDuration = GENERIC_DURATION  / tempo;
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
    if (song[position] != 0 && !this->isPaused) {
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
  void nextSong() {
    this->position = 0;

    songIdx++;

    if (songIdx == sizeof(allSongs)/sizeof(int))
    {
      songIdx = 0;
    }

    this->song = allSongs[songIdx];
  }

public:
void previousSong() {
  this->position = 0;

  songIdx--;

  if (songIdx < 0)
  {
    songIdx = sizeof(allSongs)/sizeof(int) - 1;
  }

  this->song = allSongs[songIdx];
}

public:
  void registerIncreasedChunkDuration() {
    currentPauseChunkDuration += pauseDuration;
  }

public:
  float getCurrentPauseChunkDuration() {
    return currentPauseChunkDuration;
  }

public:
  void toggle() {
    this->isPaused ? this->resume() : this->pause();
  }

public:
  void changeSong(int* newSong) {
    this->song = newSong;
  }

private:
  void pause() {
    this->isPaused = true;
  }

private:
void resume(){
  this->isPaused = false;
}

};