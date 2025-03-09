package device_service.dtos;

import java.util.UUID;

public class DeviceMonitoringDTO {
    private UUID deviceId;
    private UUID userId;
    private Double maxHourlyConsumption;
    public DeviceMonitoringDTO(){

    }
    // Constructor
    public DeviceMonitoringDTO(UUID deviceId, UUID userId, Double maxHourlyConsumption) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.maxHourlyConsumption = maxHourlyConsumption;
    }

    // Getters și Setters
    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Double getMaxHourlyConsumption() {
        return maxHourlyConsumption;
    }

    public void setMaxHourlyConsumption(Double maxHourlyConsumption) {
        this.maxHourlyConsumption = maxHourlyConsumption;
    }
}
