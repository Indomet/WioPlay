package com.wioplay.parser.Network.Subscriptions;

import com.wioplay.parser.Network.AbstractSubscription;

public class HelloWorld extends AbstractSubscription {

    public HelloWorld() {
        super("hello-world");
    }

    @Override
    public void messageArrived(String message) {
        System.out.printf("Received message: %s\n", message);
    }

}
