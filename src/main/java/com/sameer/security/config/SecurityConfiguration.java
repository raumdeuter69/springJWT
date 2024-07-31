package com.sameer.security.config;

import com.sameer.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.sameer.security.user.Role.ADMIN;
import static com.sameer.security.user.Role.USER;

@Configurable
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CORS
        http.csrf(AbstractHttpConfigurer :: disable).
authorizeRequests(auth->auth.requestMatchers("/api/v1/auth/*") // this means this regex don't need login
        .permitAll()
        .requestMatchers("/api/v1/user/**").hasAnyAuthority(USER.name())
        .requestMatchers(("/api/v1/admin/**")).hasAnyAuthority(ADMIN.name())
        .anyRequest()//OTher routes other than auth needs authentication
        .authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
