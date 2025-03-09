//package user_service.services;
//
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import user_service.configuration.JwtConfig.JwtLogic;
//import user_service.dtos.LoginDTO;
//import user_service.entities.UserInfo;
//import user_service.repositories.UserRepository;
//
//import java.util.HashMap;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//    private final UserRepository repository;
//    private final PasswordEncoder encoder;
//    private final JwtLogic jwtService;
//    private final AuthenticationManager authenticationManager;
//
///*
//    public String authenticate(LoginDTO request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        var user = repository.findUserInfoByEmail(request.getEmail());
//
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("role", user.getRole());
//        return jwtService.generateToken(claims, user);
//    }*/
//}
//
