package com.sparq.sparqservice.Entities;

import java.util.List;
import java.util.UUID;

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

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "profile")
  private Contact contact;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
  private List<Education> education;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
  private List<Job> workHistory;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "profile")
  private About about;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
  private List<Project> projects;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
  private List<Skill> skills;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
  private List<Equivalency> industryEquivalency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private String name;
  private Boolean masterProfile;
  private Boolean savedProfile;

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

  public List<Equivalency> getIndustryEquivalency() {
    return industryEquivalency;
  }

  public void setIndustryEquivalency(List<Equivalency> industryEquivalency) {
    this.industryEquivalency = industryEquivalency;
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

}
