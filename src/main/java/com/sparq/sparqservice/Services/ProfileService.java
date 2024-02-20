package com.sparq.sparqservice.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparq.sparqservice.Entities.About;
import com.sparq.sparqservice.Entities.Education;
import com.sparq.sparqservice.Entities.Equivalency;
import com.sparq.sparqservice.Entities.Job;
import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Entities.Project;
import com.sparq.sparqservice.Entities.Skill;
import com.sparq.sparqservice.Entities.UtilEntities.BulletListEntry;
import com.sparq.sparqservice.Entities.UtilEntities.JobTechnologyListEntry;
import com.sparq.sparqservice.Entities.UtilEntities.ProjectTechnologyListEntry;
import com.sparq.sparqservice.Repositories.ProfileRepository;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepo;

    //returns a profile object for a given id
    public Profile getProfileById(Long profileId) {
        return profileRepo.findById(profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    //updates profile and returns updated profile object
    public Profile updateProfile(Long profileId, Profile profile) {
        Profile currentProfile = profileRepo.findById(profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile with ID " + profileId + " does not exist"));
        if(profileId != profile.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID does not match ID in body.");
        }

        //add entity relations to new entries
        if(profile.getContact() != null && (profile.getContact().getId() == null || profile.getContact().getProfile() == null)) {
            profile.getContact().setProfile(profile);
        }
        if(profile.getAbout() != null) {
            if(profile.getAbout().getId() == null || profile.getAbout().getProfile() == null) {
                profile.getAbout().setProfile(profile);
            }
            if(profile.getAbout().getBulletList() != null) {
                for(BulletListEntry entry : profile.getAbout().getBulletList()) {
                    if(entry.getId() == null || entry.getAbout() == null) {
                        entry.setAbout(profile.getAbout());
                    }
                }
            }
        }
        if(profile.getEducation() != null) {
            for(Education education : profile.getEducation()) {
                if(education.getId() == null || education.getProfile() == null) {
                    education.setProfile(profile);
                }
            }
        }
        if(profile.getWorkHistory() != null) {
            for(Job job : profile.getWorkHistory()) {
                if(job.getId() == null || job.getProfile() == null) {
                    job.setProfile(profile);
                }
                if(job.getTechnologies() != null) {
                    for(JobTechnologyListEntry tech : job.getTechnologies()) {
                        if(tech.getId() == null || tech.getJob() == null) {
                            tech.setJob(job);
                        }
                    }
                }
            }
        }
        if(profile.getProjects() != null) {
            for(Project project : profile.getProjects()) {
                if(project.getId() == null || project.getProfile() == null) {
                    project.setProfile(profile);
                }
                if(project.getTechnologies() != null) {
                    for(ProjectTechnologyListEntry tech : project.getTechnologies()) {
                        if(tech.getId() == null || tech.getProject() == null) {
                            tech.setProject(project);
                        }
                    }
                }
            }
        }
        if(profile.getSkills() != null) {
            for(Skill skill : profile.getSkills()) {
                if(skill.getId() == null || skill.getProfile() == null) {
                    skill.setProfile(profile);
                }
            }
        }
        if(profile.getIndustryEquivalency() != null) {
            for(Equivalency eq : profile.getIndustryEquivalency()) {
                if(eq.getId() == null || eq.getProfile() == null) {
                    eq.setProfile(profile);
                }
            }
        }
        
        if(profile.getCreatedDate() == null) {
            profile.setCreatedDate(LocalDate.now());
        }

        profile.setUser(currentProfile.getUser());

        if(profile.getMasterProfile() == true) {
            List<Profile> oldMasterProfs = profileRepo.findByNameContainingIgnoreCase("old master profile");
            List<Profile> masterProfs = profileRepo.findByUserAndMasterProfile(currentProfile.getUser(), true);
            for(Profile p : masterProfs) {
                p.setMasterProfile(false);
                p.setName(p.getName() + " (Old Master Profile " + (oldMasterProfs.size() == 0 ? "" : Integer.toString(oldMasterProfs.size() + 1)) + ")");
                profileRepo.save(p);
            }
        }
        return profileRepo.save(profile);
    }

    //deletes profile for a given id
    public void deleteProfile(Long profileId, Profile profile) {
        if(profileId != profile.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request ID does not match ID in body.");
        }
        profileRepo.delete(profile);
        return;
    }

    public Profile cloneProfile(Long profileId) {
        Profile profile = profileRepo.findById(profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile with ID " + profileId + " does not exist"));
        Profile clonedProfile = new Profile();
        if(profile.getAbout() != null) {
            About clonedAbout = new About();
            clonedAbout.setDescription(profile.getAbout().getDescription());
            List<BulletListEntry> clonedBulletList = new ArrayList<BulletListEntry>();
            if(profile.getAbout().getBulletList() != null) {
                profile.getAbout().getBulletList().stream().forEach(listItem -> { 
                    BulletListEntry entry = new BulletListEntry(); 
                    entry.setText(listItem.getText());
                    entry.setAbout(clonedAbout); 
                    clonedBulletList.add(entry);
                });
            }
            clonedAbout.setBulletList(clonedBulletList);
            clonedAbout.setProfile(clonedProfile);
            clonedProfile.setAbout(clonedAbout);
        }
        if(profile.getEducation() != null) {
            List<Education> clonedEducation = new ArrayList<Education>();
            profile.getEducation().stream().forEach(ed -> {
                Education clonedEd = new Education();
                clonedEd.setDegree(ed.getDegree());
                clonedEd.setEndDate(ed.getEndDate());
                clonedEd.setFieldOfStudy(ed.getFieldOfStudy());
                clonedEd.setMinor(ed.getMinor());
                clonedEd.setSchool(ed.getSchool());
                clonedEd.setStartDate(ed.getStartDate());
                clonedEd.setProfile(clonedProfile);
                clonedEducation.add(clonedEd);
            });
            profile.setEducation(clonedEducation);
        }
        if(profile.getProjects() != null) {
            List<Project> clonedProjects = new ArrayList<Project>();
            profile.getProjects().stream().forEach(proj -> {
                Project clonedProject = new Project();
                clonedProject.setDescription(proj.getDescription());
                clonedProject.setEndDate(proj.getEndDate());
                clonedProject.setName(proj.getName());
                clonedProject.setProfile(clonedProfile);
                clonedProject.setStartDate(proj.getStartDate());
                clonedProject.setType(proj.getType());
                if(proj.getTechnologies() != null) {
                    List<ProjectTechnologyListEntry> clonedTechs = new ArrayList<ProjectTechnologyListEntry>();
                    proj.getTechnologies().stream().forEach(tech -> {
                        ProjectTechnologyListEntry clonedTech = new ProjectTechnologyListEntry();
                        clonedTech.setProject(clonedProject);
                        clonedTech.setText(tech.getText());
                        clonedTechs.add(clonedTech);
                    });
                }
                clonedProjects.add(clonedProject);
            });
        }
        //add jobs, skills
        return clonedProfile;
    }
}