package com.sparq.sparqservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparq.sparqservice.Entities.Profile;
import java.util.List;
import com.sparq.sparqservice.Entities.User;



public interface ProfileRepository extends JpaRepository<Profile, Long> {
  
  List<Profile> findByUserAndMasterProfile(User user, boolean masterProfile);
  List<Profile> findByNameContainingIgnoreCase(String name);

}