package com.wioplay.parser.Network.Subscriptions;

import com.wioplay.parser.Network.AbstractSubscription;

public class getSongs extends AbstractSubscription {

    public getSongs() {
        super("songs/all");
    }

    @Override
    public void messageArrived(String message) {
        System.out.printf("Received message: %s\n", message);
    }

}
