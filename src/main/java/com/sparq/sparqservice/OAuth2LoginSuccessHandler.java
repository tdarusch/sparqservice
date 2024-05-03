package com.sparq.sparqservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.sparq.sparqservice.Entities.User;
import com.sparq.sparqservice.Repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired
  UserRepository userRepo;
  
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
    OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
    DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
    Map<String, Object> attributes = principal.getAttributes();
    String email = attributes.getOrDefault("email", "").toString();
    String name = attributes.getOrDefault("name", "").toString();
    String picture = attributes.getOrDefault("picture", "").toString();

    userRepo.findByEmail(email)
      .ifPresentOrElse(user -> {
        DefaultOAuth2User newUser = new DefaultOAuth2User(new ArrayList<SimpleGrantedAuthority>(), attributes, "email");
        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, new ArrayList<SimpleGrantedAuthority>(),
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
        if(!user.getEnabled()) {
          throw new HttpClientErrorException(HttpStatusCode.valueOf(401));
        }
        if(user.getImageUrl() == null) {
          user.setImageUrl(picture);
          userRepo.save(user);
        }
      }, () -> {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setAdmin(false);
        user.setImageUrl(picture);
        user.setEnabled(true);
        userRepo.save(user);
        DefaultOAuth2User newUser = new DefaultOAuth2User(new ArrayList<SimpleGrantedAuthority>(), attributes, "email");
        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, new ArrayList<SimpleGrantedAuthority>(),
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
      });

    this.setAlwaysUseDefaultTargetUrl(true);
    this.setDefaultTargetUrl("https://master--silly-peony-2e8ef3.netlify.app/login/success/" + userRepo.findByEmail(email).get().getId());
    super.onAuthenticationSuccess(request, response, authentication);
  }

}
