package com.sale.message.processor.model;


public class MessageMetric {

    private long messageCounter;

    private long pauseCounter;

    public MessageMetric(long messageCounter, long pauseCounter) {
        this.messageCounter = messageCounter;
        this.pauseCounter = pauseCounter;
    }

    public long getMessageCounter() {
        return messageCounter;
    }

    public long getPauseCounter() {
        return pauseCounter;
    }

    @Override
    public String toString() {
        return "MessageMetric{" +
                "messageCounter=" + messageCounter +
                ", pauseCounter=" + pauseCounter +
                '}';
    }
}
