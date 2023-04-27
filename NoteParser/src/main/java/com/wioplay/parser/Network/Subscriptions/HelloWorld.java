package com.wioplay.parser.Network.Subscriptions;

import com.wioplay.parser.Utils.IOScanner;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class HelloWorld implements IMqttMessageListener {

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {

        IOScanner.println("Received Message: " + s);

    }

}
