package com.sparq.sparqservice.Entities.UtilEntities;

import java.time.LocalDate;

//Data transfer object for holding basic profile info
public class ProfileDTO {
  
  private Long id;
  private String name;
  private LocalDate createdDate;
  private boolean masterProfile;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

  public boolean getMasterProfile() {
    return masterProfile;
  }

  public void setMasterProfile(boolean masterProfile) {
    this.masterProfile = masterProfile;
  }

}
