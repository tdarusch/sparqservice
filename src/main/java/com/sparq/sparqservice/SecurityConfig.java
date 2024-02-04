package com.sparq.sparqservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import com.sparq.sparqservice.Entities.User;
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
      .oauth2Login(oc -> oc.userInfoEndpoint(ui -> ui.userService(oauth2LoginHandler())).successHandler(oAuth2LoginSuccessHandler))
      .authorizeHttpRequests(c -> {
        c.requestMatchers("**").permitAll();
        c.anyRequest().authenticated();
      })
      .build();
  }

  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler() {
    return userRequest -> {
      DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
      OAuth2User oauth2User = delegate.loadUser(userRequest);
      User user = new User();
      user.setUsername(oauth2User.getAttribute("login"));
      user.setName(oauth2User.getAttribute("login"));
      user.setImageUrl(oauth2User.getAttribute("avatar_url"));
      user.setPassword(UUID.randomUUID().toString());
      user.setEmail(oauth2User.getAttribute("email"));
      user.setId(UUID.nameUUIDFromBytes(user.getName().getBytes()));
      user.setAdmin(true);
      user.setEnabled(true);
      return userRepo.findById(user.getId()).orElse(userRepo.save(user));
    };
  }

}
