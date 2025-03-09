package MonitoringService.Repository;


import MonitoringService.entities.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, UUID> {

   // List<Monitoring> findAllByDeviceIdAndTimestampBetween(String deviceId, LocalDateTime start, LocalDateTime end);

}