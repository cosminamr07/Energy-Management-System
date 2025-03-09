package device_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import device_service.dtos.DeviceDTO;
import device_service.services.DeviceService;
import org.slf4j.Logger;
import device_service.dtos.DeviceDTO;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Service
public class DeviceProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

   //    @Autowired
    private RabbitTemplate rabbitTemplate;

    public DeviceProducer(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
}
    public void sendMessage(String msg){
        LOGGER.info(String.format("Message: %s", msg));
        rabbitTemplate.convertAndSend(exchange,routingKey,msg);
    }

    public void sendDeviceDTO(DeviceDTO deviceDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(deviceDTO);
            System.out.println("send1: "+deviceDTO.toString());
            LOGGER.info(String.format("Sending DeviceDTO: %s", jsonMessage));

            rabbitTemplate.convertAndSend(exchange, routingKey, jsonMessage);
            System.out.println("send2: "+deviceDTO.toString());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error serializing DeviceDTO to JSON", e);
        }

    }
}
