package MonitoringService.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonProperty; // pentru a ne asigura că userId este mapat corect

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Table(name="device")
@Entity

public class Device implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "deviceId")
    private UUID deviceId;

    @Column(name = "maxHourlyEnergyConsumption")
    private Double maxHourlyEnergyConsumption;

    @Column(name = "userId")
    private UUID userId;

    public Device(Long id, UUID deviceId, Double maxHourlyEnergyConsumption, UUID userId) {
        this.id = id;
        this.deviceId = deviceId;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.userId = userId;
    }

    public Device() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

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
}
