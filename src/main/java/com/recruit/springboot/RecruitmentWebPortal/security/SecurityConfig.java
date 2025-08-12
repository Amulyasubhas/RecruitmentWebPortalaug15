
package com.recruit.springboot.RecruitmentWebPortal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Allow access to error path and static resources
                .requestMatchers("/error", "/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                
                // Health check and actuator endpoints
                .requestMatchers("/actuator/**", "/health", "/info").permitAll()

                // Public endpoints
                .requestMatchers("/auth/**", "/reset/**", "/password/**", "/otp/**").permitAll()

                // Admin-specific endpoints
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                
                // Open Requirements
                .requestMatchers(HttpMethod.GET, "/api/open-requirements").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")
                .requestMatchers(HttpMethod.GET, "/api/open-requirements/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")
                .requestMatchers(HttpMethod.POST, "/api/open-requirements").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")
                .requestMatchers(HttpMethod.PUT, "/api/open-requirements/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")
                .requestMatchers(HttpMethod.DELETE, "/api/open-requirements/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")

                // Candidate Management
                .requestMatchers(HttpMethod.POST, "/api/candidates").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")
                .requestMatchers(HttpMethod.GET, "/api/candidates").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")
                .requestMatchers(HttpMethod.PUT, "/api/candidates/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")
                .requestMatchers(HttpMethod.DELETE, "/api/candidates/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECRUITER")

                // Interview Tracker - Now using method-level security instead of URL-level
                .requestMatchers("/api/interview-tracker/**").authenticated()

                // All other requests
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.getWriter().write("Unauthorized: " + authException.getMessage());
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.getWriter().write("Access Denied: " + accessDeniedException.getMessage());
                })
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

