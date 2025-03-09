package MonitoringService.controllers;

import MonitoringService.entities.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(UUID deviceId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications", new Notification(deviceId, message));
    }
}
