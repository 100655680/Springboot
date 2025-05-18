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

    @MessageMapping("/signaling")
    public void onSignal(SignalingMessage msg) {
        // send directly to the target user’s private queue
        template.convertAndSendToUser(
            msg.getTo(),
            "/queue/signaling",
            msg
        );
    }
}
