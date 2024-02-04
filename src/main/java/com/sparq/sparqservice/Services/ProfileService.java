package com.sparq.sparqservice.Services;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    UserService userService;

    //returns a profile object for a given id
    public Profile getProfileById(UUID userId, Long profileId) {
        return profileRepo.findById(profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no master profile"));
    }
}