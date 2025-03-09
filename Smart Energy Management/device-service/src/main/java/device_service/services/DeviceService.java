package device_service.services;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import device_service.entities.Device;
import device_service.repositories.DeviceRepository;
import device_service.response.DeviceResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hibernate.type.descriptor.JdbcExtractingLogging.LOGGER;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;


//    private final ObjectMapper objectMapper = new ObjectMapper();



    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> findDevices() {
        return deviceRepository.findAll();
         }

    @Autowired
    private ModelMapper mapper;

    public  DeviceService(DeviceRepository deviceRepository, RabbitTemplate rabbitTemplate){
        this.deviceRepository = deviceRepository;
    }

    public DeviceResponse findDeviceByDeviceId(UUID deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        return mapper.map(device, DeviceResponse.class);
    }
    public void deleteByUserId(UUID userId) {
        deviceRepository.deleteByUserId(userId);
    }


    public Optional<Device> findDeviceById(UUID id) {

        return deviceRepository.findById(id);
    }
    public Device updateDevice(Device device) {
        return deviceRepository.save(device);

    }
    public Device findByAddress(String address) {
        return deviceRepository.findByAddress(address);
    }
    public List<DeviceResponse> findDevicesByUserId(UUID userId) {
        List<Device> devices = deviceRepository.findByUserId(userId);
        return devices.stream()
                .map(device -> mapper.map(device, DeviceResponse.class))
                .collect(Collectors.toList());
    }
    public void deleteDevice(Device device) {
        deviceRepository.delete(device);
    }

    /*public Device findDeviceByUserId(UUID id)
    {
        return deviceRepository.findByUserId(id).orElse(null);
    }*/

    public UUID insert(Device device) {
        if (device.getUserID() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        device = deviceRepository.save(device);

     //   LOGGER.debug("Device with id {} was inserted in db", device.getId());


        return device.getId();
    }

}
