package device_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import device_service.entities.Device;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {


    List<Device> findByUserId(UUID userId);
    Device findDeviceById(UUID deviceId);
    Device findByAddress(String address);
    Device findDeviceByUserId(UUID deviceId);

    void deleteByUserId(UUID userId); // metodă personalizată


}
