package com.wioplay.parser;

import org.eclipse.paho.client.mqttv3.MqttException;
import com.wioplay.parser.Network.MQTTConnection;
import com.wioplay.parser.Utils.IOScanner;

public class Main {

    public static void main(String[] args) {

        try {

            new MQTTConnection("Parser");
            IOScanner.printSeparator();
            System.out.println();

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

    }

}
