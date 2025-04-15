package com.dartsapp.controller;

import com.dartsapp.model.SignalingMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    public SignalingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Clients send signaling messages to "/app/signaling"
    @MessageMapping("/signaling")
    public void signaling(@Payload SignalingMessage message) {
        // Forward the signaling message to the recipient's private destination
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/signaling", message);
    }
}
