package user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import user_service.configuration.JwtConfig.JwtLogic;
import user_service.dtos.LoginDTO;
import user_service.entities.UserInfo;
import user_service.response.UserResponse;
import user_service.services.UserService;
import org.springframework.http.HttpHeaders;

import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3005")
@RequestMapping("User")
public class UserController {

    private final UserService userService;
    private final JwtLogic jwtLogic ;


    @Autowired
    public UserController(UserService userService, JwtLogic jwtLogic) {
        this.userService = userService;
        this.jwtLogic = jwtLogic;
    }

    @GetMapping("/GetAllUsers")
  //  @Secured("ROLE_Admin")
    public List<UserInfo> getUsers() {
        System.out.println("dsadsada"+ userService.findAllUsers()+userService.findAllUsers().size());
        return userService.findAllUsers();
    }

    @PostMapping("/Insert")
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody UserInfo userInfo) {
        UUID userID = userService.insert(userInfo);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }
    @PostMapping("/CheckLogin")
    public ResponseEntity<Map<String, Object>>checkLogin(@Valid @RequestBody LoginDTO loginRequest) {
        System.out.println("check ");

        UserInfo userInfo = userService.findByEmail(loginRequest.getEmail());
        if (userInfo != null && userInfo.getPassword().equals(loginRequest.getPassword())) {
            String redirectPath = determineRedirectPath(userInfo.getRole());

            // Generează un token JWT
            String jwtToken = jwtLogic.generateToken(userInfo.getEmail(), userInfo.getRole());
            Map<String, Object> response = new HashMap<>();
            response.put("user", userInfo);
            response.put("token", jwtToken);
            response.put("redirectPath", redirectPath); // Adaugă redirectPath în răspuns

            return new ResponseEntity<>(response, HttpStatus.OK);
}
        return null;
    }


    @GetMapping("/FindByEmail")
    public  ResponseEntity<UserInfo> findByEmail(@RequestParam String email) {
        System.out.println(email);
        UserInfo user = userService.findByEmail(email);
        //System.out.println(user.getName());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/Update")
    public ResponseEntity<UUID> updateUser(@Valid @RequestBody UserInfo userInfo) {
        UUID userID = userService.update(userInfo);
        return new ResponseEntity<>(userID, HttpStatus.OK);
    }
    @PostMapping("/Delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserInfo userInfo) {
        try {
            System.out.println("NUme "+userInfo);
            userService.deleteUserById(userInfo.getId());
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @PostMapping("/GetUserById")
    public ResponseEntity<Optional<UserResponse>> getUserById( @RequestBody UUID userId) {
        System.out.println(userId);
        Optional<UserResponse> userResponse = userService.getUserById(userId);

        if (userResponse.isPresent()) {
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private String determineRedirectPath(String userRole) {
        switch (userRole) {
            case "Client":
                return "/Platform";
            case "Admin":
                return "/AdminPage";
            default:
                return "/";
        }
    }
    //TODO: UPDATE, DELETE per resource

}
