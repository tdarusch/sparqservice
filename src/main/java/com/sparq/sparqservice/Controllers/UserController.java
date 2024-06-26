package com.sparq.sparqservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Entities.UtilEntities.ProfileDTO;
import com.sparq.sparqservice.Entities.UtilEntities.ProfileDTOPage;
import com.sparq.sparqservice.Entities.UtilEntities.UserDTO;
import com.sparq.sparqservice.Services.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {
  
  //each service method is commented in the UserService file
  @Autowired
  UserService service;

  @GetMapping(value = "/users/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<UserDTO> getUserInfo(
      @RequestParam(required = false) String userName,
      @RequestParam(required = false) Boolean admin,
      @RequestParam(required = false) Boolean enabled,
      @RequestParam(required = false) String userEmail,
      @RequestParam(required = false) String profileName,
      @RequestParam(required = false) String bio,
      @RequestParam(required = false) String profileEmail,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String headline,
      @RequestParam(required = false) String company,
      @RequestParam(required = false) String school,
      @RequestParam(required = false) String project,
      @RequestParam(required = false) String skill,
      @RequestParam(required = false) String technology
    ) {
    return service.getAllUserInfo(userName, admin, enabled, userEmail, profileName, bio, profileEmail, phone, headline, company, school, project, skill, technology);
  }

  @GetMapping(value = "/users/{userId}/profiles/master", produces = MediaType.APPLICATION_JSON_VALUE)
  public Profile getMasterProfile(@PathVariable UUID userId) {
    return service.getMasterProfileByUserId(userId);
  }

  @GetMapping(value = "/users/{userId}/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ProfileDTO> getProfilesInfo(
      @PathVariable UUID userId, 
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String bio,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String headline,
      @RequestParam(required = false) String company,
      @RequestParam(required = false) String school,
      @RequestParam(required = false) String project,
      @RequestParam(required = false) String skill,
      @RequestParam(required = false) String technology
    ){
    return service.getAllProfilesInfo(userId, name, bio, email, phone, headline, company, school, project, skill, technology);
  }

  @GetMapping(value = "/users/{userId}/profiles/page", produces = MediaType.APPLICATION_JSON_VALUE)
  public ProfileDTOPage getProfileDTOsPage(
    @PathVariable UUID userId,
    @RequestParam(required = true) int pageNumber,
    @RequestParam(required = true) int pageSize,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String bio,
    @RequestParam(required = false) String email,
    @RequestParam(required = false) String phone,
    @RequestParam(required = false) String headline,
    @RequestParam(required = false) String company,
    @RequestParam(required = false) String school,
    @RequestParam(required = false) String project,
    @RequestParam(required = false) String skill,
    @RequestParam(required = false) String technology
  ) {
    return service.getAllProfilesPage(pageNumber, pageSize, userId, name, bio, email, phone, headline, company, school, project, skill, technology);
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

  @PostMapping(value="/users/{userId}/promote", produces = MediaType.APPLICATION_JSON_VALUE)
  public void promoteUser(@PathVariable UUID userId) {
    service.setAdmin(userId, true);
  }

  @PostMapping(value="/users/{userId}/demote", produces = MediaType.APPLICATION_JSON_VALUE)
  public void demoteUser(@PathVariable UUID userId) {
    service.setAdmin(userId, false);
  }

  @GetMapping(value = "/users/{userId}/isNew", produces = MediaType.APPLICATION_JSON_VALUE)
  public boolean isNew(@PathVariable UUID userId) {
    return service.isNewUser(userId);
  }

  @PostMapping(value="/users/{userId}/enable", produces = MediaType.APPLICATION_JSON_VALUE)
  public void enableUser(@PathVariable UUID userId) {
    service.setEnabled(userId, true);
  }

  @PostMapping(value="/users/{userId}/disable", produces = MediaType.APPLICATION_JSON_VALUE)
  public void disableUser(@PathVariable UUID userId) {
    service.setEnabled(userId, false);
  }

  @PutMapping(value="/users/{userId}/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserDTO updateUser(
      @PathVariable UUID userId, 
      @RequestBody Map<String, String> userDetails
    ) {
    return service.updateUser(userId, userDetails);
  }

}
