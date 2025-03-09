package MonitoringService.Service;

import MonitoringService.controllers.NotificationController;
import MonitoringService.entities.Device;
import MonitoringService.entities.Monitoring;
import MonitoringService.Repository.MonitoringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MonitoringService {
    private final MonitoringRepository monitoringRepository;

    public MonitoringService(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Autowired
    private NotificationController notificationController;

    public void checkEnergyConsumption(Device device, double totalConsumption) {
        if (totalConsumption > device.getMaxHourlyEnergyConsumption()) {
            notificationController.sendNotification(
                    device.getDeviceId(),
                    "Consum depășit pentru dispozitivul " + device.getId()
            );
        }
    }

    /*public double calculateHourlyConsumption(String deviceId, LocalDateTime timestamp) {
        // Selectează toate măsurătorile din ultima oră pentru un dispozitiv
        LocalDateTime oneHourAgo = timestamp.minusHours(1);
        List<Monitoring> measurements = monitoringRepository.findAllByDeviceIdAndTimestampBetween(
                deviceId, oneHourAgo, timestamp);

        // Calculează suma valorilor
        return measurements.stream()
                .mapToDouble(Monitoring::getMeasurementValue)
                .sum();
    }*/
}
