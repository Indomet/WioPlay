package com.example.wioplayMQTT;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Broker extends AppCompatActivity {
    public static final String SUB_TOPIC = "helloworld";
    // this is the local host, can be chnaged to an online server later on
    public static final String LOCALHOST = "10.0.9.170";

    // server url
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883"; // the server uses tcp protocol on the
                                                                              // local host ip and listens to the port
                                                                              // 1883
    public static final String CLIENT_ID = "Android Phone"; // the app client ID name
    public static final int QOS = 0; // quality of service

    private MqttClient mqttClient;
    // context is the activity pretty much
    Context context;
    TextView connectionMessage;

    public BrokerConnection(Context context){
        this.context = context;
        mqttClient = new MqttClient(context, MQTT_SERVER, CLIENT_ID);
        connectToMqttBroker();
    }

    public void connectToMqttBroker() {
        if (!isConnected) {
            mqttClient.connect(CLIENT_ID, "", new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    final String successfulConnection = "Connected to MQTT broker";
                    // logs the message connected to the mqtt broker
                    Log.i(CLIENT_ID, successfulConnection);
                    // make a toast message that displays if a connection was established
                    Toast.makeText(context, successfulConnection, Toast.LENGTH_LONG).show();
                    mqttClient.subscribe("helloworld", 0, null);

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
                 * 
                 * @param topic-  the topic that has been received
                 * @param message - the message received
                 * @throws Exception
                 */

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    /*
                     * TODO
                     * The code commented below is missing the if-condition.
                     * Uncomment the code provide the correct if-condition so that
                     * the connectionMessage can be display.
                     */

                    if (isConnected) {

                      String messageMQTT = message.toString();
                        connectionMessage.setText(messageMQTT);
                        Log.i(CLIENT_ID, "Message" + messageMQTT); // prints in the console
                    } else {
                        // prints in the console
                        Log.i(CLIENT_ID, "[MQTT] Topic: " + topic + " | Message: " +
                                message.toString());
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(CLIENT_ID, "Message delivered");
                }
            });
        }
    }

    /**
     * Function that set the textview reference from the MainActivity to the
     * connectionMessage textview
     * in the brokerConnection.
     * 
     * @param textView
     */
    public void setConnectionMessage(TextView textView) {
        this.connectionMessage = textView;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

}
