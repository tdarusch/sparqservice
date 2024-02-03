package com.sparq.sparqservice.Entities.UtilEntities;

import com.sparq.sparqservice.Entities.About;

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
@Table(name = "BULLETLIST", schema = "sparq")
public class BulletListEntry {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @Column(columnDefinition = "text")
  private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "about_id")
  private About about;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public About geAbout() {
    return about;
  }

  public void setAbout(About about) {
    this.about = about;
  }

}
