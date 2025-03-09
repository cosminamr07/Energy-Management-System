package user_service.response;

import java.util.List;
import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String name;
    private String role;
    private String username;
    private String password;

    private List<DeviceResponse> devices;

    public UserResponse() {
    }

    public UserResponse(String name, String role, String username, String password) {
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public UserResponse(UUID id, String name, String role, String username, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    // Getter și Setter pentru id
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // Getter și Setter pentru name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter și Setter pentru role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<DeviceResponse> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceResponse> devices) {
        this.devices = devices;
    }
}
