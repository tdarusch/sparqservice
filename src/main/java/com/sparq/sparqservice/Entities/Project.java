package com.sparq.sparqservice.Entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.sparq.sparqservice.Entities.UtilEntities.ProjectTechnologyListEntry;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "PROJECTS", schema = "sparq")
public class Project {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "tech_id", nullable = false)
  private List<ProjectTechnologyListEntry> technologies;

  @DateTimeFormat(pattern = "MM/dd/yyyy")
  private LocalDate startDate;
  
  @DateTimeFormat(pattern = "MM/dd/yyyy")
  private LocalDate endDate;
  
  private String name;

  @Column(columnDefinition = "text")
  private String description;

  private String type;
  private String link;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<ProjectTechnologyListEntry> getTechnologies() {
    return technologies;
  }

  public void setTechnologies(List<ProjectTechnologyListEntry> technologies) {
    this.technologies = technologies;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

}
