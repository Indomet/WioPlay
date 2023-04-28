package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import info.mqtt.android.service.Ack;

public class BrokerConnection extends AppCompatActivity {

    // topics to subscribe to
    public static final String SETTINGS_CHANGE_TOPIC = "User/Data/Change";
    public static final String WORKOUT_STARTED_TOPIC = "User/Workout/Start";
    public static final String SUB_TOPIC = "Send/Calorie/Burn/Data";

    public static  final String SONG_LIST_TOPIC = "Send/Song";
    public static final String LOCALHOST = "broker.emqx.io"; // Ip address of the local host
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";   // the server uses tcp protocol on the local host ip and listens to the port 1883
    public static final String CLIENT_ID = "Android Phone";   // the app client ID name
    public static final int QOS = 0;    // quality of service

    private boolean isConnected = false;
    private MqttClient mqttClient;
    private Context context;

    //Alternatively this could be an arraylist to notify many subscribers to the message listenrs
    private MessageListener messageListener;
    //This interface is a contract between this class to notify other classes that implement it that a message has arrived
    public interface MessageListener{
        public void onMessageArrived(String payload);
    }

    //attach a class to listen to incoming messages with this setter
    public void setMessageListener(MessageListener messageListener){
        this.messageListener=messageListener;
    }

    //we automatically try to connect the broke in the constructor by calling the connectToMqttBroker method
    public BrokerConnection(Context context){
        this.context = context;
        mqttClient = new MqttClient(context, MQTT_SERVER, CLIENT_ID, Ack.AUTO_ACK);
        connectToMqttBroker();
    }

    public void connectToMqttBroker() {
        if (!isConnected) {
            //IMqttActionListener interface is used to handle the result of the connection, so we over ride the sucess failure and lost connection methods

            mqttClient.connect(CLIENT_ID, "", new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //if we suceeded in connecting then we subscribe to the topic
                    isConnected = true;
                    final String successfulConnection = "Connected to MQTT broker";
                    Toast.makeText(context, successfulConnection, Toast.LENGTH_LONG).show();

                    mqttClient.subscribe(SUB_TOPIC,0, null);
                    mqttClient.subscribe(SONG_LIST_TOPIC,0, null);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
                    Log.e(CLIENT_ID, failedConnection);
                    Toast.makeText(context, failedConnection, Toast.LENGTH_SHORT).show();
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(CLIENT_ID, connectionLost);
                    Toast.makeText(context, connectionLost, Toast.LENGTH_SHORT).show();
                }

                /**
                 * Function that handles the messages received from the broker
                 * @param topic- the topic that has been received
                 * @param message - the message received
                 * @throws Exception
                 */

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //check if the topic of the message is the subscribed topic
                    if(isConnected && topic.equals(SUB_TOPIC)){
                        Toast.makeText(context, "The message is "+ message.toString(), Toast.LENGTH_SHORT).show();
                        String messageMQTT = message.toString();
                        Log.i(CLIENT_ID, "Message" + messageMQTT);  // prints in the console
                        //this will execute the method in all classes that implement this interface and thereby forward the message to allow
                        //communicate between fragements and the broker
                        if(messageListener!=null){
                            messageListener.onMessageArrived(messageMQTT);
                        }

                    }else {
                        // prints in the console
                        Log.i(CLIENT_ID, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(CLIENT_ID, "Message delivered");
                }
            });
        }
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }


}