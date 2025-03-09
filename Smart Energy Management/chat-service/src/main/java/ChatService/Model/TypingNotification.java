package ChatService.Model;

public class TypingNotification {
    private String user;
    private boolean isTyping;

    public TypingNotification(String user, boolean isTyping) {
        this.user = user;
        this.isTyping = isTyping;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }


}
