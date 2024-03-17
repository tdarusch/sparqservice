package com.sparq.sparqservice.Entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS", schema = "sparq")
public class User {
  
  @Id
  @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
  @GeneratedValue(generator = "UUIDGenerator")
  @Column(updatable = false, nullable = false)
  private UUID id;

  private Boolean enabled;
  private Boolean admin;
  private String imageUrl;
  private String email;
  private String name;

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  List<Profile> profiles;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getPassword() {
    return null;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getAdmin() {
    return admin;
  }

  public void setAdmin(Boolean admin) {
    this.admin = admin;
  }

  public List<Profile> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
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

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

}
