package com.sparq.sparqservice.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTACT", schema = "sparq")
public class Contact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  private String firstName;
  private String lastName;

  @Column(columnDefinition = "text")
  private String headline;

  private String email;
  private String phone;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private Profile profile;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getHeadline() {
    return headline;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

}
