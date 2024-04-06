package com.sparq.sparqservice.Entities.UtilEntities;

import java.util.List;

public class ProfileDTOPage {
  
  private int totalResults;
  private int pageNumber;
  private int pageSize;
  private int lastPage;
  List<ProfileDTO> profiles;

  public int getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(int totalResults) {
    this.totalResults = totalResults;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getLastPage() {
    return lastPage;
  }

  public void setLastPage(int lastPage) {
    this.lastPage = lastPage;
  }

  public List<ProfileDTO> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<ProfileDTO> profiles) {
    this.profiles = profiles;
  }

}
