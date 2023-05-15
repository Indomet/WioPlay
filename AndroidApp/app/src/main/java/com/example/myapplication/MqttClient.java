package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.android.service.MqttAndroidClient;
// we use this for api 31 and above, since above is not updated yet
import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

public class MqttClient {

    private MqttAndroidClient mMqttAndroidClient;

    public MqttClient(Context context, String serverUrl, String clientId, Ack ack) {
        //create an android mqttclient which is the phone
        mMqttAndroidClient =  new MqttAndroidClient(context, serverUrl, clientId, ack.AUTO_ACK);
    }

    //to connect a client we need authentication
    // connection call back is used to handle result of connection success or failure
    //client call back used to handle incoming messages to the client
    public void connect(String username, String password, IMqttActionListener connectionCallback, MqttCallback clientCallback) {

        mMqttAndroidClient.setCallback(clientCallback);
        //setting the user name password and automatic reconnect
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        try {
            mMqttAndroidClient.connect(options, null, connectionCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // receive message
    public void subscribe(String topic, int qos, IMqttActionListener subscriptionCallback) {
        try {
            mMqttAndroidClient.subscribe(topic, qos, null, subscriptionCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // send message
    public void publish(String topic, String message, int qos, IMqttActionListener publishCallback) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        mqttMessage.setQos(qos);

        try {
            mMqttAndroidClient.publish(topic, mqttMessage, null, publishCallback);
            Log.d("tag","THE MESSAGE WAS SENT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}