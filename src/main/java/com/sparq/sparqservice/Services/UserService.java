package com.sparq.sparqservice.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Entities.User;
import com.sparq.sparqservice.Entities.UtilEntities.ProfileDTO;
import com.sparq.sparqservice.Entities.UtilEntities.UserDTO;
import com.sparq.sparqservice.Repositories.ProfileRepository;
import com.sparq.sparqservice.Repositories.UserRepository;

@Service
public class UserService {
  
  @Autowired
  UserRepository userRepo;

  @Autowired
  ProfileRepository profileRepo;

  //returns a list of all users' names and ids
  //used for getting list of all users for admins
  public List<UserDTO> getAllUserInfo() {
    List<User> users = userRepo.findAll();
    List<UserDTO> userDTOs = new ArrayList<UserDTO>();

    for(User user : users) {
      UserDTO dto = new UserDTO();
      dto.setId(user.getId());
      List<Profile> masterProfiles = profileRepo.findByUserAndMasterProfile(user, true);
      if(masterProfiles.size() == 0) {
        continue;
      }
      dto.setName(user.getName());
      dto.setAdmin(user.getAdmin());
      dto.setEmail(user.getEmail());
      dto.setImageUrl(user.getImageUrl());
      userDTOs.add(dto);
    }

    return userDTOs;
  }

  //find a user by a given id
  //if not found throws a 404 not found exception
  public User getUserById(UUID userId) {
    return userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not user with ID: " + userId + "found."));    
  }

  //find user by a given ID and return their master profile
  //if they do not have a master profile throw a 404
  public Profile getMasterProfileByUserId(UUID userId) {
    User user = getUserById(userId);
    List<Profile> masterProfiles = profileRepo.findByUserAndMasterProfile(user, true);
    if(masterProfiles.size() == 0) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no master profile");
    } else {
      return masterProfiles.get(0);
    }
  }

  //returns the name and ID for each profile
  //returns empty array if no profiles
  public List<ProfileDTO> getAllProfilesInfo(UUID userId) {
    User user = getUserById(userId);
    List<ProfileDTO> profileDTOs = new ArrayList<ProfileDTO>();

    Profile masterProfile = getMasterProfileByUserId(userId);
    ProfileDTO masterDto = new ProfileDTO();
    masterDto.setId(masterProfile.getId());
    masterDto.setName("Master");
    profileDTOs.add(masterDto);

    for(Profile profile : user.getProfiles()) {
      ProfileDTO dto = new ProfileDTO();
      dto.setId(profile.getId());
      dto.setName(profile.getName());
      profileDTOs.add(dto);
    }

    return profileDTOs;    
  }

  //similar to getAllProfilesInfo but returns the same info for a users saved profile pdfs
  //returns empty list if none saved
  public List<ProfileDTO> getAllSavedProfilesInfo(UUID userId) {
    List<ProfileDTO> profileDTOs = new ArrayList<ProfileDTO>();
    User user = getUserById(userId);
    List<Profile> savedProfiles = profileRepo.findByUserAndSavedProfile(user, true);
    
    for(Profile profile : savedProfiles) {
      ProfileDTO dto = new ProfileDTO();
      dto.setId(profile.getId());
      dto.setName(profile.getName());
      profileDTOs.add(dto);
    }

    return profileDTOs;
  }

  //save a new editable profile to the user's list of profiles
  //if user has no profiles make this profile their master profile
  public Profile addProfile(UUID userId, Profile profile) {
    User user = getUserById(userId);
    if(user.getProfiles().size() == 0) {
      profile.setMasterProfile(true);
      user.getProfiles().add(profile);
      userRepo.save(user);
    } else {
      profile.setMasterProfile(false);
      profile.setSavedProfile(false);
      user.getProfiles().add(profile);
      userRepo.save(user);
    }
    return profile;
  }

  //add a new profile to a user's saved profiles (pdfs)
  public Profile addSavedProfile(UUID userId, Profile profile) {
    User user = getUserById(userId);
    profile.setSavedProfile(true);
    profile.setMasterProfile(false);
    user.getProfiles().add(profile);
    userRepo.save(user);
    return profile;
  }

  public UserDTO getUserInfoDto(UUID userId) {
    User user = getUserById(userId);
    UserDTO userDto = new UserDTO();
    userDto.setId(userId);
    userDto.setAdmin(user.getAdmin());
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setImageUrl(user.getImageUrl());
    return userDto;
  }

}
