// src/main/java/com/dartsapp/controller/SignalingController.java
package com.dartsapp.controller;

import com.dartsapp.model.SignalingMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SignalingController {

    private final SimpMessagingTemplate template;

    public SignalingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Receive ANY signaling message (offer, answer, candidate, turn, etc.)
     * and forward it to the specific user's private queue.
     */
    @MessageMapping("/signaling")
    public void onSignal(SignalingMessage msg) {
        template.convertAndSendToUser(
            msg.getTo(),
            "/queue/signaling",
            msg
        );
    }
}
