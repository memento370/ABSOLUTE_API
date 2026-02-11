package com.example.absoluteweb.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtAuthenticationFilterForum jwtAuthenticationFilterForum;
    @Autowired
    private RateLimitingFilter rateLimitingFilter;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/site/accounts/register",
//                                "/api/server/accounts/register",
//                                "/api/site/accounts/check-register",
//                                "/api/site/accounts/send-verification",
//                                "/api/site/accounts/verify-code",
//                                "/api/site/accounts/login",
//                                "/api/files/**",
//                                "/api/forum/user/create-user",
//                                "/api/forum/user/check-register",
//                                "/api/forum/user/send-verification",
//                                "/api/forum/user/verify-code",
//                                "/api/forum/user/login",
//                                "/api/forum/user/send-verification-restore",
//                                "/api/forum/user/verify-code-restore",
//                                "/api/forum/user/restore-password",
//                                "/api/forum/user/check-token",
//                                "/api/forum/user/get-user/{id}",
//                                "/api/forum/user/check-user-token",
//                                "/api/forum/topic/by-section/{section}",
//                                "/api/forum/topic/{id}",
//                                "/api/forum/comment-topic/{topicId}",
//                                "/api/forum/topic/by-section/**").permitAll()
//                        .requestMatchers("/index.html", "/static/**", "/favicon.ico").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable());
//        http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(jwtAuthenticationFilterForum, UsernamePasswordAuthenticationFilter.class);
//
//
//        return http.build();
//    }

    @Bean
    @Order(1)
    public SecurityFilterChain siteFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/site/**", "/api/server/**", "/api/files/**")  // Matcher для шляхів сайту
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/site/accounts/register",
                                "/api/site/accounts/check-register",
                                "/api/site/accounts/send-verification",
                                "/api/site/accounts/verify-code",
                                "/api/site/accounts/login"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain forumFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/forum/**")  // Matcher для шляхів форуму
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/forum/user/create-user",
                                "/api/forum/user/check-register",
                                "/api/forum/user/send-verification",
                                "/api/forum/user/verify-code",
                                "/api/forum/user/login",
                                "/api/forum/user/send-verification-restore",
                                "/api/forum/user/verify-code-restore",
                                "/api/forum/user/restore-password",
                                "/api/forum/user/check-token",
                                "/api/forum/user/get-user/{id}",
                                "/api/forum/user/check-user-token",
                                "/api/forum/topic/by-section/{section}",
                                "/api/forum/topic/{id}",
                                "/api/forum/comment-topic/{topicId}",
                                "/api/forum/topic/by-section/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilterForum, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "https://l2-absolute.com",
                "https://152.53.125.216:8081",
                "https://152.53.125.216:8080",
                "https://152.53.125.216:587",
                "https://localhost:8080",
                "https://localhost:4200",
                "http://localhost:4200",
                "https://144.91.99.218"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Whirlpool2PasswordEncoder();
    }

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream("src/main/resources/l2-absolute-1737806935341-3658b86cb89e.json"))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}

