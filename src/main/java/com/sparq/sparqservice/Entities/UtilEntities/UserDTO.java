package com.sparq.sparqservice.Entities.UtilEntities;

import java.util.UUID;

//Data transfer object for holding basic user info
public class UserDTO {
  
  private UUID id;
  private String name;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
