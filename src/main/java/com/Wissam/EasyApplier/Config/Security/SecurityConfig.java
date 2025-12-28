package com.Wissam.EasyApplier.Config.Security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.Wissam.EasyApplier.Config.Security.FilterChains.JwtFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService userDetailsService;
  private final JwtFilterChain jwtFilterChain;
  private final OAuth2SuccessHandler oath2SuccesHandler;
  private final AuthenticationEntryPointImpl authEntryPoint;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfig()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authenticationProvider(authProvider())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/auth/**",
                "/error",
                "/login**",
                "/oauth2/**",
                "/v3/api-docs/**",
                "/swagger-ui/**")
            .permitAll()
            .anyRequest().authenticated())
        // .formLogin(form -> form.permitAll())
        .oauth2Login(oath2 -> oath2.successHandler(oath2SuccesHandler)
            .failureUrl("/auth/failure"))
        .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> {
          RequestMatcher jsonMatcher = new MediaTypeRequestMatcher(
              MediaType.APPLICATION_JSON);
          ex.defaultAuthenticationEntryPointFor(authEntryPoint, jsonMatcher);
        });

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfig() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:8080"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  AuthenticationProvider authProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  AuthenticationManager authManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }

}
