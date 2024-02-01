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
import com.sparq.sparqservice.Repositories.UserRepository;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepo;

    //returns a profile object for a given id
    public Profile getProfileById(UUID userId, Long profileId) {
        User user = getUserById(userId);
        List<Profile> userProfiles= user.getProfiles();
        foreach(Profile prof : userProfiles){
            if(prof.getId() == profileId);
            {
                return prof;
            }
        }
    }
}