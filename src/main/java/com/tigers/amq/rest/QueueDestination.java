package com.tigers.amq.rest;

public enum QueueDestination {

    redhat("myAddress0.myQueue0"),
    queue("inbound.queue"),
    topic("inbound.topic");

    public String queueName;

    QueueDestination(String queueName) {
        this.queueName = queueName;
    }
}
