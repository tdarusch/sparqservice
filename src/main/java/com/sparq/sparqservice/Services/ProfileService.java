package com.sparq.sparqservice.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Repositories.ProfileRepository;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepo;

    //returns a profile object for a given id
    public Profile getProfileById(UUID userId, Long profileId) {
        return profileRepo.findById(profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no master profile"));
    }

    //updates profile and returns updated profile object
    public Profile updateProfile(Long profileId, Profile profile) {
        if(profileId != profile.getId())
        {
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID does not match ID in body.");
        }
        return profileRepo.save(profile);
    }

    //deletes profile for a given id
    public void deleteProfile(Long profileId, Profile profile) {
        if(profileId != profile.getId())
        {
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID does not match ID in body.");
        }
        profileRepo.delete(profile);
        return;

    }
}