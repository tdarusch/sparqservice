package com.sparq.sparqservice.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparq.sparqservice.Entities.Education;
import com.sparq.sparqservice.Entities.Job;
import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Entities.Project;
import com.sparq.sparqservice.Entities.Skill;
import com.sparq.sparqservice.Entities.User;
import com.sparq.sparqservice.Entities.UtilEntities.BulletListEntry;
import com.sparq.sparqservice.Entities.UtilEntities.JobTechnologyListEntry;
import com.sparq.sparqservice.Entities.UtilEntities.ProfileDTO;
import com.sparq.sparqservice.Entities.UtilEntities.ProjectTechnologyListEntry;
import com.sparq.sparqservice.Entities.UtilEntities.UserDTO;
import com.sparq.sparqservice.Repositories.ProfileRepository;
import com.sparq.sparqservice.Repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserService {
  
  @Autowired
  UserRepository userRepo;

  @Autowired
  ProfileRepository profileRepo;

  @PersistenceContext
  EntityManager entityManager;

  //returns a list of all users' names and ids
  //used for getting list of all users for admins
  public List<UserDTO> getAllUserInfo(String userName, Boolean admin, String userEmail,
    String profileName, String bio, String profileEmail, String phone, String headline,
    String company, String school, String project, String skill
  ) {
    
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
    Root<User> root = criteriaQuery.from(User.class);
    Predicate predicate = cb.and();
    
    if(userName != null) {
      predicate = cb.and(predicate, cb.like(cb.upper(root.get("name")), "%"+userName.toUpperCase()+"%"));
    }
    if(admin != null) {
      predicate = cb.and(predicate, cb.equal(root.get("admin"), admin));
    }
    if(userEmail != null) {
      predicate = cb.and(predicate, cb.like(cb.upper(root.get("email")), "%"+userEmail.toUpperCase()+"%"));
    }

    criteriaQuery.where(predicate);
    List<User> users = entityManager.createQuery(criteriaQuery).getResultList();
    
    List<UserDTO> userDTOs = new ArrayList<UserDTO>();
    for(User user : users) {
      UserDTO dto = new UserDTO();
      dto.setId(user.getId());
      List<Profile> masterProfiles = profileRepo.findByUserAndMasterProfile(user, true);
      if(masterProfiles.size() == 0) {
        continue;
      }
      dto.setName(user.getName());
      dto.setAdmin(user.getAdmin());
      dto.setEmail(user.getEmail());
      dto.setImageUrl(user.getImageUrl());
      dto.setProfiles(getAllProfilesInfo(
        user.getId(), profileName, bio, profileEmail, phone, 
        headline, company, school, project, skill
      ));
      userDTOs.add(dto);
    }

    return userDTOs;
  }

  //find a user by a given id
  //if not found throws a 404 not found exception
  public User getUserById(UUID userId) {
    return userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not user with ID: " + userId + "found."));    
  }

  //find user by a given ID and return their master profile
  //if they do not have a master profile throw a 404
  public Profile getMasterProfileByUserId(UUID userId) {
    User user = getUserById(userId);
    List<Profile> masterProfiles = profileRepo.findByUserAndMasterProfile(user, true);
    if(masterProfiles.size() == 0) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no master profile");
    } else {
      return masterProfiles.get(0);
    }
  }

  //returns the name and ID for each profile
  //returns empty array if no profiles matching criteria
  public List<ProfileDTO> getAllProfilesInfo(
    UUID userId, String name, String bio, String email, String phone, String headline, String company, String school, String project, String skill
  ) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Profile> criteriaQuery = cb.createQuery(Profile.class);
    Root<Profile> root = criteriaQuery.from(Profile.class);
    Predicate predicate = cb.and();
    predicate = buildProfilePredicate(
      predicate, root, cb, userId, name, bio, email, phone, headline, company, school, project, skill
      );
    criteriaQuery.where(predicate);
    List<Profile> profiles = entityManager.createQuery(criteriaQuery).getResultList();
      
    List<ProfileDTO> profileDTOs = new ArrayList<ProfileDTO>(); 
    for(Profile profile : profiles) {
      ProfileDTO dto = new ProfileDTO();
      dto.setId(profile.getId());
      dto.setName(profile.getName());
      dto.setCreatedDate(profile.getCreatedDate());
      profileDTOs.add(dto);
    }

    return profileDTOs;    
  }

  //Filter null values and create a criteria query predicate for searching profiles
  private Predicate buildProfilePredicate(
    Predicate predicate, Root<Profile> root, CriteriaBuilder cb, 
    UUID userId, String name, String bio, String email, String phone, 
    String headline, String company, String school, String project, String skill
  ){
    predicate = cb.and(predicate, cb.equal(root.get("user").get("id"), userId));
    predicate = cb.and(predicate, cb.equal(root.get("masterProfile"), false));
    if(name != null) {
      predicate = cb.and(predicate, cb.like(cb.upper(root.get("name")), "%"+name.toUpperCase()+"%"));
    }
    if(bio != null) {
      predicate = cb.and(predicate, cb.like(cb.upper(root.get("bio")), "%"+bio.toUpperCase()+"%"));
    }
    if(email != null) {
      predicate = cb.and(predicate, cb.like(cb.upper(root.get("email")), "%"+email.toUpperCase()+"%"));
    }
    if(phone != null) {
      predicate = cb.and(predicate, cb.like(root.get("phone"), "%"+phone+"%"));
    }
    if(headline != null) {
      predicate = cb.and(predicate, cb.like(cb.upper(root.get("headline")), "%"+headline.toUpperCase()+"%"));
    }
    if(company != null) {
      predicate = cb.and(predicate, cb.like(
          cb.upper(root.join("workHistory").get("company")), "%"+company.toUpperCase()+"%"
        ));
    }
    if(school != null) {
      predicate = cb.and(predicate, cb.like(
          cb.upper(root.join("education").get("school")), "%"+school.toUpperCase()+"%"
        ));
    }
    if(project != null) {
      predicate = cb.and(predicate, cb.like(
        cb.upper(root.join("projects").get("name")), "%"+project.toUpperCase()+"%"
      ));
    }
    if(skill != null) {
      predicate = cb.and(predicate, cb.like(
          cb.upper(root.join("skills").get("name")), "%"+skill.toUpperCase()+"%"
        ));
    }
    return predicate;
  }

  //save a new editable profile to the user's list of profiles
  //if user has no profiles make this profile their master profile
  public Profile addProfile(UUID userId, Profile profile) {
    User user = getUserById(userId);
    if(profileRepo.findByUserAndMasterProfile(user, true).size() == 0) {
      profile.setMasterProfile(true);
      profile.setName("Master Profile");
    }
    profile.setCreatedDate(LocalDate.now());
    profile.setUser(user);
    profileRepo.save(profile);
    return profile;
  }

  public UserDTO getUserInfoDto(UUID userId) {
    User user = getUserById(userId);
    UserDTO userDto = new UserDTO();
    userDto.setId(userId);
    userDto.setAdmin(user.getAdmin());
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setImageUrl(user.getImageUrl());
    return userDto;
  }

  //clone the current master profile and return its id for the UI to route to
  public Long cloneMasterProfile(UUID userId) {
    Profile masterProfile = getMasterProfileByUserId(userId);
    Profile clonedProfile = new Profile();
    
    clonedProfile.setName("New Profile");
    clonedProfile.setCreatedDate(LocalDate.now());
    clonedProfile.setUser(masterProfile.getUser());
    clonedProfile.setFirstName(masterProfile.getFirstName());
    clonedProfile.setMiddleName(masterProfile.getMiddleName());
    clonedProfile.setLastName(masterProfile.getLastName());
    clonedProfile.setHeadline(masterProfile.getHeadline());
    clonedProfile.setEmail(masterProfile.getEmail());
    clonedProfile.setPhone(masterProfile.getPhone());
    clonedProfile.setBio(masterProfile.getBio());
    clonedProfile.setBulletList(new ArrayList<BulletListEntry>());
    clonedProfile.setEducation(new ArrayList<Education>());
    clonedProfile.setProjects(new ArrayList<Project>());
    clonedProfile.setWorkHistory(new ArrayList<Job>());
    clonedProfile.setSkills(new ArrayList<Skill>());

    masterProfile.getEducation().stream().forEach(ed -> {
      Education education = new Education();
      education.setId(null);
      education.setDegree(ed.getDegree());
      education.setSchool(ed.getSchool());
      education.setFieldOfStudy(ed.getFieldOfStudy());
      education.setMinor(ed.getMinor());
      education.setStartDate(ed.getStartDate());
      education.setEndDate(ed.getEndDate());
      clonedProfile.getEducation().add(education);
    });

    masterProfile.getWorkHistory().stream().forEach(job -> {
      Job clonedJob = new Job();
      clonedJob.setId(null);
      clonedJob.setTechnologies(new ArrayList<JobTechnologyListEntry>());
      job.getTechnologies().stream().forEach(tech -> {
        JobTechnologyListEntry clonedTech = new JobTechnologyListEntry();
        clonedTech.setId(null);
        clonedTech.setText(tech.getText());
        clonedJob.getTechnologies().add(clonedTech);
      });
      clonedJob.setStartDate(job.getStartDate());
      clonedJob.setEndDate(job.getEndDate());
      clonedJob.setCompany(job.getCompany());
      clonedJob.setRole(job.getRole());
      clonedJob.setResponsibilities(job.getResponsibilities());
      clonedJob.setCurrent(job.getCurrent());
      clonedProfile.getWorkHistory().add(clonedJob);
    });

    masterProfile.getProjects().stream().forEach(project -> {
      Project clonedProject = new Project();
      clonedProject.setId(null);
      clonedProject.setTechnologies(new ArrayList<ProjectTechnologyListEntry>());
      project.getTechnologies().stream().forEach(tech -> {
        ProjectTechnologyListEntry clonedTech = new ProjectTechnologyListEntry();
        clonedTech.setId(null);
        clonedTech.setText(tech.getText());
        clonedProject.getTechnologies().add(clonedTech);
      });
      clonedProject.setStartDate(project.getStartDate());
      clonedProject.setEndDate(project.getEndDate());
      clonedProject.setName(project.getName());
      clonedProject.setDescription(project.getDescription());
      clonedProject.setType(project.getType());
      clonedProject.setLink(project.getLink());
      clonedProfile.getProjects().add(clonedProject);
    });

    masterProfile.getSkills().stream().forEach(skill -> {
      Skill clonedSkill = new Skill();
      clonedSkill.setId(null);
      clonedSkill.setType(skill.getType());
      clonedSkill.setName(skill.getName());
      clonedSkill.setProficiency(skill.getProficiency());
      clonedSkill.setMonths(skill.getMonths());
      clonedProfile.getSkills().add(clonedSkill);
    });

    masterProfile.getBulletList().stream().forEach(entry -> {
      BulletListEntry clonedEntry = new BulletListEntry();
      clonedEntry.setId(null);
      clonedEntry.setText(entry.getText());
      clonedProfile.getBulletList().add(clonedEntry);
    });

    return profileRepo.save(clonedProfile).getId();
  }

  public void setAdmin(UUID userId, boolean makeAdmin) {
    User user = getUserById(userId);
    user.setAdmin(makeAdmin);
    userRepo.save(user);
  }

}
