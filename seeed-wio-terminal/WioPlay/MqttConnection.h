#include <rpcWiFi.h>
#include <PubSubClient.h>
// This should be created by the user to enter wifi name password and the broker for mqtt
/*
  #define SSID "Wifi-name-here"
  #define PASSWORD "Password-here"
  #define my_IPv4 "Broker-adress-here"
  */
#include "WifiInformation.h"

// Update these with values suitable for your network.
const char *ssid = SSID;          // WiFi Name
const char *password = PASSWORD;  // WiFi Password
const char *server = my_IPv4;     // MQTT Broker URL

const char *Music_sub = "Music/Data/Change";
const char *TOPIC_sub = "User/Data/Change";
const char *Workout_sub = "User/Workout/Start";
const char *TOPIC_pub_connection = "Send/Calorie/Burn/Data";
const char *Music_buttons_pub = "Music/Song/Buttons";
const char *Music_notes_sub = "Music/Song/Notes";
const char *Loop_trigger_sub = "Music/Loop";
const char *Request_pub = "request/notes";


WiFiClient wioClient;
PubSubClient client(wioClient);
long lastMsg = 0;
char msg[50];
int value = 0;

void setup_wifi() {

  delay(10);

  tft.setTextSize(2);
  tft.setCursor((320 - tft.textWidth("Connecting to Wi-Fi..")) / 2, 120);
  tft.print("Connecting to Wi-Fi..");

  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);  // Connecting WiFi

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");

  tft.fillScreen(TFT_BLACK);
  tft.setCursor((320 - tft.textWidth("Connected!")) / 2, 120);
  tft.print("Connected!");

  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());  // Display Local IP Address
}

void updateSettings(char json[]) {

  DynamicJsonDocument doc(1024);
  deserializeJson(doc, json);
  int age = doc["age"];
  const char *name = doc["username"];
  const char *sex = doc["sex"];
  float weight = doc["weight"];
  float height = doc["height"];
  float calorieCredit = doc["calorieCredit"];
  userInformation.setInformation(weight, height, age, strcmp("Female", sex) == 0 ? false : true);
}

void updateChart(char json[]) {

  DynamicJsonDocument doc(1024);
  deserializeJson(doc, json);
  float calorieGoal = doc["calorieGoal"];
  float duration = doc["durationInSeconds"];
  byte workoutType = doc["workoutType"];

  burndownChartBackEnd.changeAttributeValues(duration, calorieGoal, workoutType);
}

void updateSongData(char json[]) {

  scenes.changeSongName(json);
}


void updateSong(char json[]) {

  Serial.println("switching chunk");

  DynamicJsonDocument doc(JSON_ARRAY_SIZE(40));
  DeserializationError error = deserializeJson(doc, json);

  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    return;
  }

  doc.shrinkToFit();
  player.changeSong(doc);
  player.hasRequested = false;
}


void startStreaming() {
  Serial.println("Requesting notes..");
  client.publish("request/notes", "I need a new set of notes");
}

void callback(char *topic, byte *payload, unsigned int length) {
  // tft.fillScreen(TFT_BLACK);
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");

  char charBuf[length] PROGMEM;
  for (int i = 0; i < length; i++) {
    charBuf[i] = (char)payload[i];
  }

  if (strcmp(TOPIC_sub, topic) == 0) {
    updateSettings(charBuf);
  } else if (strcmp(Workout_sub, topic) == 0) {
    updateChart(charBuf);

  } else if (strcmp(Music_sub, topic) == 0) {
    updateSongData(charBuf);
  } else if (strcmp(Loop_trigger_sub, topic) == 0) {
    startStreaming();
  } else if (strcmp(Music_notes_sub, topic) == 0) {
    updateSong(charBuf);
  }
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "wioClient";
    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      client.publish(calorie_pub, "hello world");
      client.setBufferSize(10024);
      Serial.println("Published connection message ");
      // ... and resubscribe
      client.subscribe(TOPIC_sub);
      Serial.print("Subcribed to: ");
      Serial.println(TOPIC_sub);
      // subscribe to start
      client.subscribe(Workout_sub);
      Serial.print("Subcribed to: ");
      Serial.println(Workout_sub);

      client.subscribe(Music_sub);
      Serial.print("Subcribed to: ");
      Serial.println(Music_sub);

      client.subscribe(Music_notes_sub);
      Serial.print("Subcribed to: ");
      Serial.println(Music_notes_sub);


      client.subscribe(Loop_trigger_sub);
      Serial.print("Subcribed to: ");
      Serial.println(Loop_trigger_sub);

    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void setupMqtt() {
  tft.begin();
  tft.fillScreen(TFT_BLACK);
  tft.setRotation(3);

  setup_wifi();
  client.setServer(server, 1883);  // Connect the MQTT Server   hive_mqtt_server
  client.setCallback(callback);
}

void loopMqtt() {

  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}
