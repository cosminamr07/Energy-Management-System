package device_service.controllers;


import device_service.dtos.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import device_service.entities.Device;
import device_service.response.DeviceResponse;
import device_service.services.DeviceService;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
//@RequiredArgsConstructor
public class DeviceController {

    public DeviceController(DeviceService deviceService, DeviceProducer deviceProducer) {
        this.deviceService = deviceService;
        this.deviceProducer = deviceProducer;
    }

    private final DeviceService deviceService;
    //@Autowired
    private final DeviceProducer deviceProducer;


    @GetMapping("/GetAllDevices")
    public ResponseEntity<List<Device>> getDevices() {
        List<Device> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    @PostMapping("/DeleteDevice")
    public ResponseEntity<String> deleteDevice(@Valid @RequestBody Device device) {
        deviceService.deleteDevice(device);
        return new ResponseEntity<>("Device deleted", HttpStatus.OK);
    }
    @PostMapping("/UpdateDevice")
    public ResponseEntity<String> updateDevice(@Valid @RequestBody Device device) {
        deviceService.updateDevice(device);
        return new ResponseEntity<>("Device updated", HttpStatus.OK);
    }
    @PostMapping("/InsertDevice")
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody Device device) {
        System.out.println("userId received in controller: " + device.getUserID());  // Verifică valoarea userId

        // Inserare dispozitiv și obținere UUID
        UUID id = deviceService.insert(device);

        // Creare DeviceDTO pentru trimiterea către RabbitMQ
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(id);
        deviceDTO.setMaxHourlyEnergyConsumption(device.getMaxHourlyConsumption());
        deviceDTO.setUserId(device.getUserID());
        System.out.printf(deviceDTO.toString());

        // Trimitere mesaj cu DeviceDTO către RabbitMQ
        deviceProducer.sendDeviceDTO(deviceDTO);
        System.out.println(deviceDTO.toString());

        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
/*
    @GetMapping("/device-info/{id}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable UUID id) {
        DeviceResponse deviceResponse = deviceService.findDeviceByDeviceId(id);
        return deviceResponse != null ? ResponseEntity.ok(deviceResponse) : ResponseEntity.notFound().build();
    }*/
    @PostMapping("/delete-devices-by-user/{userId}")
    public ResponseEntity<Void> deleteDevicesByUserId(@PathVariable UUID userId) {
        List<Device> devices = deviceService.findDevices();
        for (Device device : devices) {
            if (device.getUserID().equals(userId)) {
                deviceService.deleteDevice(device);
            }
        }
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/devices-by-user/{userId}")
    public ResponseEntity<List<DeviceResponse>> getDevicesByUserId(@PathVariable UUID userId) {
        System.out.println(userId);
        List<DeviceResponse> devices = deviceService.findDevicesByUserId(userId);
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    //TODO: UPDATE, DELETE per resource

}
