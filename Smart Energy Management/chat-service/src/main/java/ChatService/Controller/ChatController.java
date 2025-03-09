package ChatService.Controller;

import ChatService.Model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
//@CrossOrigin(origins = "http://localhost:3005")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/chat/message/admin")
    public ChatMessage sendMessageToAdmin(@Payload ChatMessage message) {
        messagingTemplate.convertAndSend("chat/message/admin",message);
        return message;
    }


    @MessageMapping("/chat/{username}/typing")
    public String SendTypingNotification(String notification, @DestinationVariable("username") String username) {
        {
            messagingTemplate.convertAndSendToUser(username, "/typing", notification);
            return notification;
        }
    }
    @MessageMapping("/chat/admin/typing")
    public String sendTypingNotificationToAdmin(@Payload String message){
        messagingTemplate.convertAndSend("/chat/admin/typing", message);
        return message;
    }

    @MessageMapping("/chat/{username}/message")
    public ChatMessage sendMessageToUser(@Payload ChatMessage message, @DestinationVariable("username") String username){
        messagingTemplate.convertAndSendToUser(username, "/message", message);
        return message;
    }
    @MessageMapping("/chat/admin/read")
    public String sendReadNotificationToAdmin(@Payload String message){
        messagingTemplate.convertAndSend("/chat/admin/read", message);
        return message;
    }



}
