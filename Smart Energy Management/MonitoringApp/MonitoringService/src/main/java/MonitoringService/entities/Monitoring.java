package MonitoringService.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
@Table(name="monitoring")


@Entity
public class Monitoring implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "device_id")
  //  @JsonProperty("deviceId") // Asigură maparea între JSON și entitate
    private UUID deviceId;

    @Override
    public String toString() {
        return "Monitoring{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", timestamp=" + timestamp +
                ", totalHourlyConsumption=" + totalHourlyConsumption +
                '}';
    }

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "total_hourly_consumption")

    private double totalHourlyConsumption;

        public Monitoring() {

        }
    public Monitoring(  LocalDateTime timestamp, double measurementValue,  double totalHourlyConsumption) {
        this.timestamp = timestamp;
        this.totalHourlyConsumption = totalHourlyConsumption;
    }

    public Long getId() {
        return id;
    }


    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotalHourlyConsumption() {
        return totalHourlyConsumption;
    }

    public void setTotalHourlyConsumption(double totalHourlyConsumption) {
        this.totalHourlyConsumption = totalHourlyConsumption;
    }

    // Getters and setters
}
