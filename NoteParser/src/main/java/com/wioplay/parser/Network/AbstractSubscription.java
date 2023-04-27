package com.wioplay.parser.Network;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class AbstractSubscription implements IMqttMessageListener {

    private final String topic;

    public AbstractSubscription(String topic) {
        this.topic = topic;
        this.startMessage();
    }

    private void startMessage() {
        System.out.printf("Subscribed to topic %s", this.topic);
        System.out.println();
    }

    public abstract void messageArrived(String message);

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {

        this.messageArrived(mqttMessage.toString());

    }

    public String getTopic() {
        return this.topic;
    }

}
