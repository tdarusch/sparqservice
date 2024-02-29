package com.sparq.sparqservice.Entities;

import java.util.List;

import com.sparq.sparqservice.Entities.UtilEntities.BulletListEntry;

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
@Table(name = "ABOUT", schema = "sparq")
public class About {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "serial")
  private Long id;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "list_item_id")
  private List<BulletListEntry> bulletList;

  @Column(columnDefinition = "text")
  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<BulletListEntry> getBulletList() {
    return bulletList;
  }

  public void setBulletList(List<BulletListEntry> bulletList) {
    this.bulletList = bulletList;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
