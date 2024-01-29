package com.sparq.sparqservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import com.sparq.sparqservice.Entities.User;
import com.sparq.sparqservice.Repositories.UserRepository;

@Configuration
public class SecurityConfig {

  @Autowired
  UserRepository userRepo;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .oauth2Login(oc -> oc.userInfoEndpoint(ui -> ui.userService(oauth2LoginHandler()).oidcUserService(oidcLoginHandler())))
      .authorizeHttpRequests(c -> c.anyRequest().authenticated())
      .build();
  }

  private OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler() {
    return userRequest -> {
      OidcUserService delegate = new OidcUserService();
      OidcUser oidcUser = delegate.loadUser(userRequest);
      User user = new User();
      user.setEmail(oidcUser.getEmail());
      user.setUsername(oidcUser.getPreferredUsername());
      user.setImageUrl(oidcUser.getPicture());
      user.setPassword(UUID.randomUUID().toString());
      user.setName(oidcUser.getFullName());
      user.setId(UUID.nameUUIDFromBytes(oidcUser.getName().getBytes()));
      return userRepo.findById(user.getId()).orElse(userRepo.save(user));
    };
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
      user.setId(UUID.nameUUIDFromBytes(oauth2User.getName().getBytes()));
      return userRepo.findById(user.getId()).orElse(userRepo.save(user));
    };
  }

}
