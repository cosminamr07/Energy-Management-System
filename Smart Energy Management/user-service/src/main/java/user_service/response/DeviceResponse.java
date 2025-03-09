package user_service.response;

import java.util.UUID;

public class DeviceResponse {


    private UUID id;

    private String description;
    private String address;

    private Double consumption;
    private UUID userId;

    public UUID getUserID() {
        return userId;
    }

    public DeviceResponse( String description, String address, Double consumption,UUID userId) {
        this.description = description;
        this.address = address;
        this.consumption = consumption;
        this.userId= userId;
    }

    public void setUserID(UUID userID) {
        this.userId = userID;
    }

    public DeviceResponse() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }
}
