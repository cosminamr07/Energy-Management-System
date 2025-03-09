package MonitoringService.Service;

import MonitoringService.Repository.DeviceRepository;
import MonitoringService.Repository.MonitoringRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private final DeviceRepository  deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
}
