package com.sparq.sparqservice.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparq.sparqservice.Entities.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, UUID> {

  List<User> findByEmail(String email);
  
}
