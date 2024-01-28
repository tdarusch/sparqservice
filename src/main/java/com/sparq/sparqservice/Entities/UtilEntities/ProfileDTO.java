package com.sparq.sparqservice.Entities.UtilEntities;

//Data transfer object for holding basic profile info
public class ProfileDTO {
  
  private Long id;
  private String name;

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

}
