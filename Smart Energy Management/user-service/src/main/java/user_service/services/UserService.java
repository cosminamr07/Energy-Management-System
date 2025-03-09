package user_service.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import user_service.entities.UserInfo;
import user_service.repositories.UserRepository;
import user_service.response.DeviceResponse;
import user_service.response.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private ModelMapper mapper;


    @Value(value = "${DEVICE_SERVICE_URL}")
    private String deviceServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<UserResponse> getUserById(UUID id) {
        Optional<UserInfo> userInfo = userRepository.findById(id);
      UserResponse userResponse = mapper.map(userInfo, UserResponse.class);

        String url = deviceServiceUrl+"/device-service/devices-by-user/{id}";

        System.out.println(deviceServiceUrl);

        ResponseEntity<List<DeviceResponse>> deviceResponseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceResponse>>() {},
                id
        );

        List<DeviceResponse> devices = deviceResponseEntity.getBody();
        userResponse.setDevices(devices);
        return Optional.of(userResponse);
    }
    public UUID insert(UserInfo userInfo) {
        userInfo = userRepository.save(userInfo);
        LOGGER.debug("User with id {} was inserted in db", userInfo.getId());
        return userInfo.getId();
    }
    public Optional<UserInfo> findUserById(UUID id) {
        Optional<UserInfo> user = userRepository.findById(id);
        return user;
    }
    public List<UserInfo> findAllUsers() {
        return (List<UserInfo>) userRepository.findAll();
    }
    public UUID update(UserInfo userInfo) {
    userRepository.save(userInfo);
    return userInfo.getId();
    }

    public void deleteUserById(UUID userId) {
        // Verifică dacă utilizatorul există
        Optional<UserInfo> userInfo = userRepository.findById(userId);
        System.out.println(userInfo);

        if (userInfo.isEmpty()) {
            System.out.println("EROARE: "+userInfo);

            throw new IllegalArgumentException("User not found");
        }
        System.out.println("dupa if: "+userInfo);

        String url = deviceServiceUrl + "/device-service/delete-devices-by-user/{userId}";
        ResponseEntity<Void> deviceResponseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                null,
                Void.class,
                userId
        );

        userRepository.deleteById(userId);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    public UserInfo findByEmail(String email) {
        System.out.println("service_"+email);
        UserInfo existingUserInfo = userRepository.findUserInfoByEmail(email);
        if(existingUserInfo!=null) {
            return existingUserInfo;
        }
        else
        {
            System.out.println("Eroare null");
            return null;

        }
    }
}
