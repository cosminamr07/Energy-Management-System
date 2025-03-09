package MonitoringService.Repository;

import MonitoringService.entities.Device;
import MonitoringService.entities.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
