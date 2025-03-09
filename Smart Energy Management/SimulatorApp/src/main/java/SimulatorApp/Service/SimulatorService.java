package SimulatorApp.Service;

import SimulatorApp.Entity.Monitoring;
import SimulatorApp.Reader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;
@Service
public class SimulatorService {

    private final RabbitTemplate rabbitTemplate;
    private final List<Double> sensorData;
    private static final Logger logger = LoggerFactory.getLogger(SimulatorService.class);

    private final Map<UUID, SimulationState> userSimulations = new HashMap<>();

    @Value("${rabbitmq.queue.name2}")
    private String queueName2;

    @Value("${rabbitmq.exchange.name2}")
    private String exchangeName2;

    @Value("${rabbitmq.routing.key2}")
    private String routingKey2;

    public SimulatorService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.sensorData = Reader.readSensorData("src/main/resources/sensor.csv");
    }

    // Structura pentru a gestiona simularea unui utilizator
    private static class SimulationState {
        private UUID deviceId;
        private boolean isSimulating = false;
        private Thread simulationThread;
        private int currentIndex = 0;
        private final List<Monitoring> currentMeasurements = new ArrayList<>();

        public SimulationState(UUID deviceId) {
            this.deviceId = deviceId;
        }
    }

    // Pornește simularea pentru un utilizator
    public synchronized void startSimulation(UUID userId, UUID deviceId) {
        userSimulations.putIfAbsent(userId, new SimulationState(deviceId));
        SimulationState state = userSimulations.get(userId);

        if (state.isSimulating) {
            throw new IllegalStateException("Simulation is already running for this user.");
        }

        state.isSimulating = true;
        state.simulationThread = new Thread(() -> {
            while (state.isSimulating) {
                try {
                    sendMeasurement(state);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Simulation thread for user {} was interrupted.", userId);
                }
            }
        });

        state.simulationThread.start();
        logger.info("Simulation started for user ID: {}, device ID: {}", userId, deviceId);
    }

    // Oprește simularea pentru un utilizator
    public synchronized void stopSimulation(UUID userId) {
        SimulationState state = userSimulations.get(userId);
        if (state == null || !state.isSimulating) {
            throw new IllegalStateException("No simulation running for this user.");
        }

        state.isSimulating = false;
        if (state.simulationThread != null) {
            state.simulationThread.interrupt();
            state.simulationThread = null;
        }

        logger.info("Simulation stopped for user ID: {}", userId);
    }

    private void sendMeasurement(SimulationState state) {
        if (state.currentIndex >= sensorData.size()) {
            state.currentIndex = 0;
        }

        double measurementValue = sensorData.get(state.currentIndex++);
        Monitoring measurement = new Monitoring(
                LocalDateTime.now(),
                state.deviceId,
                measurementValue
        );
        state.currentMeasurements.add(measurement);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String jsonMessage = objectMapper.writeValueAsString(measurement);
            rabbitTemplate.convertAndSend(exchangeName2, routingKey2, jsonMessage);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing monitoring to JSON", e);
        }

        logger.info("Sent measurement: {}", measurement);
    }

    public List<Monitoring> getCurrentMeasurements(UUID userId) {
        SimulationState state = userSimulations.get(userId);
        if (state == null) {
            throw new IllegalStateException("No simulation found for this user.");
        }
        List<Monitoring> currentMeasurements2 = new ArrayList<>(state.currentMeasurements);
        state.currentMeasurements.clear();

        logger.info("Fetched and reset measurements for user ID: {}", userId);
        return currentMeasurements2; // Returnează măsurătorile curente
    }





}
