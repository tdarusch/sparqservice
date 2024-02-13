package com.sparq.sparqservice.Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            profile.setCreatedDate(new Date());
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
}