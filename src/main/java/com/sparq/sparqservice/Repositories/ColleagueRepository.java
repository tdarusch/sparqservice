package com.sparq.sparqservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparq.sparqservice.Entities.Colleague;

public interface ColleagueRepository extends JpaRepository<Colleague, Long> {
  
}
