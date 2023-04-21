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

  int getPosition() {
    return this->position;
  }
};