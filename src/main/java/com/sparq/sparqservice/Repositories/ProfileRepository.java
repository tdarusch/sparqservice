package com.sparq.sparqservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparq.sparqservice.Entities.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  
}