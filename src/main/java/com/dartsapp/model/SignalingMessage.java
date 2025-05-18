package com.dartsapp.model;

public class SignalingMessage {
    private String type;    // "game-invite" | "offer" | "answer" | "candidate"
    private String from;    // sender username
    private String to;      // recipient username
    private Object payload; // for "invite" → gameId (Long), for offer/answer → RTCSessionDescription, etc.

    public SignalingMessage() {}
    public SignalingMessage(String type, String from, String to, Object payload) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.payload = payload;
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
