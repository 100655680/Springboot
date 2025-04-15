package com.dartsapp.model;

public class SignalingMessage {
    private String type;    // e.g., "offer", "answer", "candidate"
    private String from;    // sender's username
    private String to;      // recipient's username
    private Object payload; // the actual signaling data (offer, answer, ICE candidate)

    public SignalingMessage() {
    }

    // Getters and setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    
    public Object getPayload() {
        return payload;
    }
    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
