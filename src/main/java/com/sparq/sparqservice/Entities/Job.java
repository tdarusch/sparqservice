package com.sparq.sparqservice.Entities;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.sparq.sparqservice.Entities.UtilEntities.TechnologyListEntry;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "JOBS", schema = "sparq")
public class Job {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @OneToMany(cascade = CascadeType.ALL)
  private List<TechnologyListEntry> technologies;

  @DateTimeFormat(pattern = "MM/yyyy")
  private Date startDate;
  
  @DateTimeFormat(pattern = "MM/yyyy")
  private Date endDate;

  private String company;
  private String role;
  private String responsibilities;
  private Boolean currentRole;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<TechnologyListEntry> getTechnologies() {
    return technologies;
  }

  public void setTechnologies(List<TechnologyListEntry> technologies) {
    this.technologies = technologies;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getResponsibilities() {
    return responsibilities;
  }

  public void setResponsibilities(String responsibilities) {
    this.responsibilities = responsibilities;
  }

  public Boolean getCurrentRole() {
    return currentRole;
  }

  public void setCurrentRole(Boolean currentRole) {
    this.currentRole = currentRole;
  }

}
