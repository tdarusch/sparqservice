package com.sparq.sparqservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sparq.sparqservice.Repositories.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  UserRepository userRepo;

  @Autowired
  OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .oauth2Login(oc -> {
        oc.successHandler(oAuth2LoginSuccessHandler);
      })
      .logout(lo -> {
        lo.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        lo.invalidateHttpSession(true);
        lo.clearAuthentication(true);
        SecurityContextHolder.clearContext();
        lo.logoutSuccessUrl("http://localhost:3000/logout/success").deleteCookies("JSESSIONID");
      })
      .authorizeHttpRequests(c -> {
        c.requestMatchers("**").permitAll();
      })
      .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000"));
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
    return urlBasedCorsConfigurationSource;
  }

}
