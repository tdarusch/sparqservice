package com.sparq.sparqservice.Entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "EDUCATION", schema = "sparq")
public class Education {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @DateTimeFormat(pattern = "MM/dd/yyyy")
  private LocalDate startDate;
  
  @DateTimeFormat(pattern = "MM/dd/yyyy")
  private LocalDate endDate;

  private String school;
  private String degree;
  private String fieldOfStudy;
  private String minor;

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

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getFieldOfStudy() {
    return fieldOfStudy;
  }

  public void setFieldOfStudy(String fieldOfStudy) {
    this.fieldOfStudy = fieldOfStudy;
  }

  public String getMinor() {
    return minor;
  }

  public void setMinor(String minor) {
    this.minor = minor;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

}
