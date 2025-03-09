package SimulatorApp.Controller;

import SimulatorApp.Entity.Monitoring;
import SimulatorApp.Service.SimulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/simulator")
@CrossOrigin
public class SimulatorController {

    private final SimulatorService simulatorService;
    private static final Logger logger = LoggerFactory.getLogger(SimulatorController.class);

    public SimulatorController(SimulatorService simulatorService) {
        this.simulatorService = simulatorService;
    }

    @PostMapping("/setDeviceId")
    public ResponseEntity<String> setDeviceId(
            @RequestParam("deviceId") String deviceId,
            @RequestParam("userId") String userId) {
        try {
            UUID deviceUUID = UUID.fromString(deviceId);
            UUID userUUID = UUID.fromString(userId);

            simulatorService.startSimulation(userUUID, deviceUUID);

            return ResponseEntity.ok("Simulation started for device ID: " + deviceId + " by user: " + userId);
        } catch (Exception e) {
            logger.error("Error starting simulation: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed to start simulation: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation(@RequestParam("userId") String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            simulatorService.stopSimulation(userUUID);

            return ResponseEntity.ok("Simulation stopped for user: " + userId);
        } catch (Exception e) {
            logger.error("Error stopping simulation: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Failed to stop simulation: " + e.getMessage());
        }
    }

    @GetMapping("/getMeasurements")
    public ResponseEntity<List<Monitoring>> getMeasurements(@RequestParam("userId") String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            List<Monitoring> measurements = simulatorService.getCurrentMeasurements(userUUID);
            return ResponseEntity.ok(measurements);
        } catch (Exception e) {
            logger.error("Error retrieving measurements: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


}


