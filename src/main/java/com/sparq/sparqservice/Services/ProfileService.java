package com.sparq.sparqservice.Services;

import java.util.List;

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
    public Profile getProfileById(Long profileId) {
        return profileRepo.findById(profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    //updates profile and returns updated profile object
    public Profile updateProfile(Long profileId, Profile profile) {
        Profile currentProfile = profileRepo.findById(profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile with ID " + profileId + " does not exist"));
        if(profileId != profile.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID does not match ID in body.");
        }

        profile.setUser(currentProfile.getUser());

        if(profile.getMasterProfile() == true) {
            List<Profile> oldMasterProfs = profileRepo.findByNameContainingIgnoreCase("old master profile");
            List<Profile> masterProfs = profileRepo.findByUserAndMasterProfile(currentProfile.getUser(), true);
            for(Profile p : masterProfs) {
                p.setMasterProfile(false);
                p.setName(p.getName() + " (Old Master Profile " + (oldMasterProfs.size() == 0 ? "" : Integer.toString(oldMasterProfs.size() + 1)) + ")");
                profileRepo.save(p);
            }
        }
        return profileRepo.save(profile);
    }

    //deletes profile for a given id
    public void deleteProfile(Long profileId, Profile profile) {
        if(profileId != profile.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID does not match ID in body.");
        }
        profileRepo.delete(profile);
    }

}