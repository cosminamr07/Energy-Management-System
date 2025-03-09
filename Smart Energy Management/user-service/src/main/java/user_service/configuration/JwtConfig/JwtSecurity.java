package user_service.configuration.JwtConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class JwtSecurity {
 //   private final CorsConfigurationSource corsConfigurationSource;


    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthFilter jwtRequestFilter;

    public JwtSecurity(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;

        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }
//
//    @Bean
//    public JwtAuthFilter jwtAuthFilter() {
//        return new JwtAuthFilter();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // Permite politica CORS
                .authorizeRequests()
                .requestMatchers("/User/Login", "/User/CheckLogin").permitAll() // Rute publice

                .anyRequest().authenticated()
                .and()
                .cors(Customizer.withDefaults())
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedPage("/error/accedd-denied"));


        // Alte cereri sunt respinse
               http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
// Adăugarea filtrului JWT
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }


}
