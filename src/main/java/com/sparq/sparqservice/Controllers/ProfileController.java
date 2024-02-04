package com.sparq.sparqservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Entities.UtilEntities.ProfileDTO;
import com.sparq.sparqservice.Entities.UtilEntities.UserDTO;
import com.sparq.sparqservice.Services.ProfileService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProfileController {
    
    @Autowired
    ProfileService service;

    @GetMapping(value = "{userId}/profiles/{profileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Profile getProfile(@PathVariable UUID userId , @PathVariable Long profileId) {
      return service.getProfileById(userId, profileId);
    }

    //TODO: Add updateProfile(userId, profileId, profile) to profileService;
    // @PutMapping(value = "{userId}/profiles/{profileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    // public Profile updateProfile(@RequestBody Profile profile, @PathVariable UUID userId, @PathVariable Long profileId) {
        // return service.getProfileById(userId, profileId, profile);
    // }

}

