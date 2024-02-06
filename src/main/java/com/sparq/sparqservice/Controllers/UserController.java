package com.sparq.sparqservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Entities.User;
import com.sparq.sparqservice.Entities.UtilEntities.ProfileDTO;
import com.sparq.sparqservice.Entities.UtilEntities.UserDTO;
import com.sparq.sparqservice.Services.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
  
  //each service method is commented in the UserService file
  @Autowired
  UserService service;

  @GetMapping(value = "/users/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<UserDTO> getUserInfo() {
    return service.getAllUserInfo();
  }

  @GetMapping(value = "/users/{userId}/profiles/master", produces = MediaType.APPLICATION_JSON_VALUE)
  public Profile getMasterProfile(@PathVariable UUID userId) {
    return service.getMasterProfileByUserId(userId);
  }

  @GetMapping(value = "/users/{userId}/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ProfileDTO> getProfilesInfo(@PathVariable UUID userId) {
    return service.getAllProfilesInfo(userId);
  }

  @GetMapping(value = "/users/{userId}/profiles/saved", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ProfileDTO> getSavedProfilesInfo(@PathVariable UUID userId) {
    return service.getAllSavedProfilesInfo(userId);
  }

  @PostMapping(value = "/users/{userId}/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
  public Profile addProfile(@RequestBody Profile profile, @PathVariable UUID userId) {
    return service.addProfile(userId, profile);
  }

  @PostMapping(value = "/users/{userId}/profiles/saved", produces = MediaType.APPLICATION_JSON_VALUE)
  public Profile addSavedProfile(@RequestBody Profile profile, @PathVariable UUID userId) {
    return service.addSavedProfile(userId, profile);
  }

  @GetMapping(value = "/users/{userId}/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public User getMethodName(@PathVariable UUID userId) {
    return service.getUserById(userId);
  }
  
}
