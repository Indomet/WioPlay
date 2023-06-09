package com.wioplay.parser.Network;

import com.google.common.reflect.ClassPath.ClassInfo;

import java.lang.reflect.InvocationTargetException;

import com.google.common.reflect.ClassPath;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.util.*;


public class MQTTConnection extends MqttClient {

    private static final String SUB_PKG = "com.wioplay.parser.Network.Subscriptions";
    private static final String SERVER_IP = "tcp://broker.hivemq.com";
    private static MQTTConnection client;

    private MQTTConnection(String clientID) throws MqttException {

        super(SERVER_IP, clientID);
        this.connect();

    }

    public void loadSubscriptions() {

        ClassLoader loader = getClass().getClassLoader();
        try {

            Set<ClassInfo> subscriptions = ClassPath.from(loader).getTopLevelClasses(SUB_PKG);
            System.out.printf("Loading %d subscription handlers...", subscriptions.size());
            System.out.println();

            for (ClassInfo classInfo : subscriptions) {
                try {

                    Class<AbstractSubscription> subscriptionDefinition = (Class<AbstractSubscription>) classInfo.load();
                    AbstractSubscription subscription = subscriptionDefinition.getDeclaredConstructor().newInstance();
                    this.subscribe(subscription.getTopic(), subscription);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void publish(String topic, String message) {

        MqttMessage payload = new MqttMessage(message.getBytes());
        try {
            this.publish(topic, payload);
        } catch (MqttException e) {
            System.out.println(e.getMessage());
        }

    }

    public static MQTTConnection getInstance()  {
        if(client == null) {
            try {
                client = new MQTTConnection("Parser");
                client.loadSubscriptions();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return client;
    }

}
