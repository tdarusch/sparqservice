package com.sparq.sparqservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.sparq.sparqservice.Entities.Profile;
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
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {
  
  //each service method is commented in the UserService file
  @Autowired
  UserService service;

  @GetMapping(value = "/users/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<UserDTO> getUserInfo(@RequestParam(required = false) String name) {
    return service.getAllUserInfo(name);
  }

  @GetMapping(value = "/users/{userId}/profiles/master", produces = MediaType.APPLICATION_JSON_VALUE)
  public Profile getMasterProfile(@PathVariable UUID userId) {
    return service.getMasterProfileByUserId(userId);
  }

  @GetMapping(value = "/users/{userId}/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ProfileDTO> getProfilesInfo(@PathVariable UUID userId, @RequestParam(required = false) String name) {
    return service.getAllProfilesInfo(userId, name);
  }
  
  @PostMapping(value = "/users/{userId}/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
  public Profile addProfile(@RequestBody Profile profile, @PathVariable UUID userId) {
    return service.addProfile(userId, profile);
  }

  @GetMapping(value = "/users/{userId}/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDTO getMethodName(@PathVariable UUID userId) {
    return service.getUserInfoDto(userId);
  }

  @PostMapping(value = "/users/{userId}/profiles/master/clone", produces = MediaType.APPLICATION_JSON_VALUE)
  public Long cloneMasterProfile(@PathVariable UUID userId) {
    return service.cloneMasterProfile(userId);
  }
  
}
