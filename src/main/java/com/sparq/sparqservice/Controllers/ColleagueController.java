package com.sparq.sparqservice.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.sparq.sparqservice.Entities.Colleague;
import com.sparq.sparqservice.Services.ColleagueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ColleagueController {
    
  @Autowired
  ColleagueService service;

  @GetMapping(value = "/colleagues", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Colleague> getColleagues() {
      return service.getColleagues();
  }

  @GetMapping(value = "/colleagues/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Colleague getColleague(@PathVariable Long id) {
      return service.getColleague(id);
  }

}
