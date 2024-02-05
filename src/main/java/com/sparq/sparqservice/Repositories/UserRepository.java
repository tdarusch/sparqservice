package com.sparq.sparqservice.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparq.sparqservice.Entities.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);
  
}
