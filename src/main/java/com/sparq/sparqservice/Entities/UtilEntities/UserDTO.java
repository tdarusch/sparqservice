package com.sparq.sparqservice.Entities.UtilEntities;

import java.util.List;
import java.util.UUID;

//Data transfer object for holding basic user info
public class UserDTO {
  
  private UUID id;
  private String name;
  private Boolean admin;
  private String email;
  private String imageUrl;
  private List<ProfileDTO> profiles;
  private Boolean enabled;

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

  public Boolean getAdmin() {
    return admin;
  }

  public void setAdmin(Boolean admin) {
    this.admin = admin;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public  void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<ProfileDTO> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<ProfileDTO> profiles) {
    this.profiles = profiles;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

}
