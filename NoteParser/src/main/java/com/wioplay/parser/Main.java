package com.wioplay.parser;

import com.wioplay.parser.Network.Mqtt;
import com.wioplay.parser.Utils.IOScanner;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

    public static void main(String[] args) {

        try {

            new Mqtt("Parser");
            IOScanner.printSeparator();
            System.out.println();

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

    }

}
