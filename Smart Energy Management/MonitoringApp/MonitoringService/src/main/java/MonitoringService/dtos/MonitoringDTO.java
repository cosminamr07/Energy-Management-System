package MonitoringService.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class MonitoringDTO {
    private LocalDateTime timestamp;
    private UUID deviceId;
    private double measurementValue;

    public MonitoringDTO() {
    }

    public MonitoringDTO(LocalDateTime timestamp, UUID deviceId, double measurementValue) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(double measurementValue) {
        this.measurementValue = measurementValue;
    }

    @Override
    public String toString() {
        return "MonitoringDTO   {" +
                "timestamp=" + timestamp +
                ", deviceId='" + deviceId + '\'' +
                ", measurementValue=" + measurementValue +
                '}';
    }
}
