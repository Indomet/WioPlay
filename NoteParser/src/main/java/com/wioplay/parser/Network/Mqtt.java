package com.wioplay.parser.Network;

import com.wioplay.parser.Network.Subscriptions.*;
import com.wioplay.parser.Utils.IOScanner;
import org.eclipse.paho.client.mqttv3.*;


public class Mqtt {

    private static final String SERVER_IP = "tcp://broker.hivemq.com";
    private final MqttClient client;

    public Mqtt(String clientID) throws MqttException {

        this.client = new MqttClient(SERVER_IP, clientID);
        this.connect();
        this.subscribeToTopics();

    }

    private void connect() throws MqttException {
        this.client.connect();
        IOScanner.println("Connected to MQTT");
    }

    private void subscribeToTopics() throws MqttException {
            this.client.subscribe("hello-world", new HelloWorld());
    }

    public MqttClient getClient() {
        return this.client;
    }

}
