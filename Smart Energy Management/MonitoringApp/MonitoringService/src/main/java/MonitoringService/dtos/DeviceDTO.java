package MonitoringService.dtos;

import java.util.UUID;

public class DeviceDTO {
    @Override
    public String toString() {
        return "DeviceDTO{" +
                "id=" + deviceId +
                ", maxHourlyEnergyConsumption=" + maxHourlyEnergyConsumption +
                ", userId=" + deviceId +
                '}';
    }
    private UUID deviceId;
    private Double maxHourlyEnergyConsumption;
    private UUID userId;

    public Double getMaxHourlyEnergyConsumption() {
        return maxHourlyEnergyConsumption;
    }

    public void setMaxHourlyEnergyConsumption(Double maxHourlyEnergyConsumption) {
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }
}
