package com.sparq.sparqservice.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparq.sparqservice.Entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
  
}
