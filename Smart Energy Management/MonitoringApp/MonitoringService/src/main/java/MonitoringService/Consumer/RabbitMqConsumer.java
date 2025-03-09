package MonitoringService.Consumer;

import MonitoringService.Repository.DeviceRepository;
import MonitoringService.dtos.DeviceDTO;
import MonitoringService.dtos.MonitoringDTO;
import MonitoringService.entities.Device;
import MonitoringService.entities.Monitoring;
import MonitoringService.Repository.MonitoringRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMqConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MonitoringRepository monitoringRepository; // Repository pentru salvarea datelor în DB
    @Autowired
    private DeviceRepository deviceRepository;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message) {
        try {
            // Deserializare mesaj JSON în MeasurementDTO
            DeviceDTO deviceDTO = objectMapper.readValue(message, DeviceDTO.class);
            System.out.println(deviceDTO.toString());
            LOGGER.info("Consumed and parsed message: {}", deviceDTO.toString());

            // Transformare MeasurementDTO în entitatea Monitoring
            Device device = new Device();
            device.setDeviceId(deviceDTO.getDeviceId());
            device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
            device.setUserId(deviceDTO.getUserId());

            // Salvare în baza de date
            deviceRepository.save(device);
            LOGGER.info("Measurement saved to database: {}", device);

        } catch (Exception e) {
            LOGGER.error("Error parsing or processing message: ", e);
        }
    }
    @RabbitListener(queues = {"${rabbitmq.queue.name2}"})
    public void consume2(String message) {
        LOGGER.info("Message received on queue2: {}", message); // Log mesajul brut

      try {
            // Verifică dacă mesajul este nul sau gol
            if (message == null || message.isEmpty()) {
                LOGGER.error("Received empty message");
                return;
            }

            // Deserializare mesaj JSON în Measurement
          MonitoringDTO monitoringDTO= objectMapper.readValue(message, MonitoringDTO.class);
            LOGGER.info("Consumed and parsed measurement: {}", monitoringDTO);
            // Crează MonitoringDTO din Measurement
           double partialSum = 0.0;
           int readingCount = 0;

// La fiecare citire (executat de simulatorul tău)
          partialSum += monitoringDTO.getMeasurementValue();
          readingCount++;

            // Salvare în baza de date
            Monitoring monitoring = new Monitoring();
            monitoring.setTimestamp(monitoringDTO.getTimestamp());
            monitoring.setDeviceId(monitoringDTO.getDeviceId());
            monitoring.setTotalHourlyConsumption(monitoringDTO.getMeasurementValue() * 4);

            // Salvare în DB
            monitoringRepository.save(monitoring);

            LOGGER.info("Measurement saved to database: {}", monitoring);

        } catch (Exception e) {
            LOGGER.error("Error parsing or processing message: ", e);
        }
    }
}
