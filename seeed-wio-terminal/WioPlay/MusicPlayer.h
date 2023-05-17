#include "pitches.h"
// #include "themes.h"

class MusicPlayer
{

  static const int GENERIC_DURATION = 1000 / 5;
  static const int CHUNK_SIZE = 5;

private:
  int position;
  int tempo;
  int pauseDuration;
  int currentPauseChunkDuration;
  // bool isPaused;

public:
  boolean hasRequested;
  DynamicJsonDocument song;
  MusicPlayer(float tempo) : song(song)
  {
    this->tempo = tempo;
    this->position = 0;
    this->isPaused = false;
    this->hasRequested = false;
    // this is the approximate duration between notes calculated based on the provided tempo
    pauseDuration = GENERIC_DURATION / tempo;
  }

private:
  void play(int position)
  {

    if (song[position] != 0 && !this->isPaused)
    {
      tone(1, song[position], GENERIC_DURATION);
    }

    delay(pauseDuration);
    noTone(1);
  }

public:
  void playChunk()
  {

    currentPauseChunkDuration = 0;
    int limit = min(this->position + CHUNK_SIZE, this->song.size());
    for (int i = this->position; i < limit; i++)
    {
      Serial.println(i);
      play(i);
      // registerIncreasedChunkDuration();
    }
    this->position = limit;
  }

public:
  int getPosition()
  {
    return this->position;
  }

public:
  void nextSong()
  {
    // this->position = 0;

    // songIdx++;

    // if (songIdx == sizeof(allSongs)/sizeof(int))
    // {
    //   songIdx = 0;
    // }

    // this->song = allSongs[songIdx];
  }

public:
  void previousSong()
  {
    // this->position = 0;

    // songIdx--;

    // if (songIdx < 0)
    // {
    //   songIdx = sizeof(allSongs)/sizeof(int) - 1;
    // }

    // this->song = allSongs[songIdx];
  }

public:
  void registerIncreasedChunkDuration()
  {
    currentPauseChunkDuration += pauseDuration;
  }

public:
  bool isPlayingSong()
  {
    return this->position != 0;
  }

public:
  float getCurrentPauseChunkDuration()
  {
    return currentPauseChunkDuration;
  }

public:
  void toggle()
  {
    this->isPaused ? this->resume() : this->pause();
  }

public:
  void changeSong(DynamicJsonDocument newSong)
  {
    this->position = 0;
    this->song = newSong;
  }

public:
bool isPaused;


private:
  void pause()
  {
    this->isPaused = true;
  }

private:
  void resume()
  {
    this->isPaused = false;
  }
};