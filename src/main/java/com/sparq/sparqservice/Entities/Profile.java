package com.sparq.sparqservice.Entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROFILES", schema = "sparq")
public class Profile {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial", name = "p_id")
  private Long id;

  @Nullable
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "contact_id")
  private Contact contact;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "education_id", nullable = false)
  private List<Education> education;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "job_id", nullable = false)
  private List<Job> workHistory;

  @Nullable
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "about_id")
  private About about;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "project_id", nullable = false)
  private List<Project> projects;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "skill_id", nullable = false)
  private List<Skill> skills;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  private String name;
  private boolean masterProfile;
  private boolean savedProfile;

  @DateTimeFormat(pattern = "MM/dd/yyyy")
  private LocalDate createdDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public List<Education> getEducation() {
    return education;
  }

  public void setEducation(List<Education> education) {
    this.education = education;
  }

  public List<Job> getWorkHistory() {
    return workHistory;
  }

  public void setWorkHistory(List<Job> workHistory) {
    this.workHistory = workHistory;
  }

  public About getAbout() {
    return about;
  }

  public void setAbout(About about) {
    this.about = about;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public boolean getMasterProfile() {
    return masterProfile;
  }

  public void setMasterProfile(Boolean masterProfile) {
    this.masterProfile = masterProfile;
  }

  public boolean getSavedProfile() {
    return savedProfile;
  }

  public void setSavedProfile(Boolean savedProfile) {
    this.savedProfile = savedProfile;
  }

  public LocalDate getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
  }

}
