package com.sparq.sparqservice.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Services.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProfileController {
    
    @Autowired
    ProfileService service;

    @GetMapping(value = "/profiles/{profileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Profile getProfile(@PathVariable Long profileId) {
      return service.getProfileById(profileId);
    }

    
    @PutMapping(value = "/profiles/{profileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Profile updateUserProfile(@PathVariable Long profileId, @RequestBody Profile profile) {
        return service.updateProfile(profileId, profile);
    }

    @DeleteMapping(value = "/profiles/{profileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteUserProfile(@PathVariable Long profileId) {
        service.deleteProfile(profileId);
    }

    @GetMapping(value = "/profiles/{profileId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getProfilePdf(@PathVariable Long profileId) {
        return service.generateProfilePdf(profileId);
    }
    

}

