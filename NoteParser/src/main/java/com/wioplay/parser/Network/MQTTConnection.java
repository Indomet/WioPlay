package com.wioplay.parser.Network;

import com.google.common.reflect.ClassPath.ClassInfo;

import java.lang.reflect.InvocationTargetException;

import com.google.common.reflect.ClassPath;
import com.wioplay.parser.Utils.IOScanner;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.util.*;


public class MQTTConnection {

    private static final String SUB_PKG = "com.wioplay.parser.Network.Subscriptions";
    private static final String SERVER_IP = "tcp://broker.hivemq.com";
    private final MqttClient client;

    public MQTTConnection(String clientID) throws MqttException {

        this.client = new MqttClient(SERVER_IP, clientID);
        this.connect();
        this.loadSubscriptions();

    }

    private void connect() throws MqttException {
        this.client.connect();
        IOScanner.println("Connected to MQTT");
    }

    private void loadSubscriptions() {

        ClassLoader loader = getClass().getClassLoader();
        try {

            Set<ClassInfo> subscriptions = ClassPath.from(loader).getTopLevelClasses(SUB_PKG);
            System.out.printf("Loading %d subscription handlers...", subscriptions.size());

            System.out.println();
            IOScanner.printSeparator();
            System.out.println();

            for (ClassInfo classInfo : subscriptions) {
                try {

                    Class<AbstractSubscription> subscriptionDefinition = (Class<AbstractSubscription>) classInfo.load();
                    AbstractSubscription subscription = subscriptionDefinition.getDeclaredConstructor().newInstance();
                    this.client.subscribe(subscription.getTopic(), subscription);

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | MqttException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public MqttClient getClient() {
        return this.client;
    }

}
