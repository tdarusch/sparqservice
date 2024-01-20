package com.sparq.sparqservice.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import com.sparq.sparqservice.Entities.Colleague;
import com.sparq.sparqservice.Repositories.ColleagueRepository;

import java.util.List;

@Service
public class ColleagueService {

  @Autowired
  ColleagueRepository colleagueRepo;

  public List<Colleague> getColleagues() {
    return colleagueRepo.findAll();
  }

  public Colleague getColleague(Long id) {
    return colleagueRepo.findById(id).orElseThrow(() -> new DataAccessResourceFailureException("No data found."));
  }

}
