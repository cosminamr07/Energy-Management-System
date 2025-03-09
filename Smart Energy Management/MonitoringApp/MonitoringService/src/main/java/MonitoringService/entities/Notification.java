package MonitoringService.entities;

import java.util.UUID;

public class Notification {
    private UUID deviceId;
    private String message;

    // Constructori, getter și setter
    public Notification(UUID deviceId, String message) {
        this.deviceId = deviceId;
        this.message = message;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
