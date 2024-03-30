package com.sparq.sparqservice.Services;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.sparq.sparqservice.Entities.Profile;
import com.sparq.sparqservice.Entities.UtilEntities.JobTechnologyListEntry;
import com.sparq.sparqservice.Entities.UtilEntities.ProjectTechnologyListEntry;
import com.sparq.sparqservice.Repositories.ProfileRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepo;

    @PersistenceContext
    EntityManager entityManager;

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

        profile.setUser(currentProfile.getUser());

        if(profile.getMasterProfile() == true) {
            List<Profile> masterProfs = profileRepo.findByUserAndMasterProfile(currentProfile.getUser(), true);
            for(Profile p : masterProfs) {
                p.setMasterProfile(false);
                p.setName("Old Master Profile");
                profileRepo.save(p);
            }
        }
        return profileRepo.save(profile);
    }

    //deletes profile for a given id
    public void deleteProfile(Long profileId) {
        profileRepo.deleteById(profileId);
    }

    public ResponseEntity<byte[]> generateProfilePdf(Long profileId) {
        Profile profile = getProfileById(profileId);
        String pdfHtml = parseProfileTemplate(profile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(pdfHtml);
        renderer.layout();
        renderer.createPDF(outputStream);
   
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "spb_profile.pdf";
        headers.add("Content-Disposition", "inline; filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        ResponseEntity<byte[]> response = new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        return response;
    }

    private String parseProfileTemplate(Profile profile) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(0);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        Context ctx = new Context();
        ctx.setVariable("name", getName(profile));
        ctx.setVariable("headline", profile.getHeadline());
        ctx.setVariable("contact", getContact(profile));
        ctx.setVariable("bio", profile.getBio());
        ctx.setVariable("bulletList", profile.getBulletList());
        ctx.setVariable("skills", profile.getSkills());
        ctx.setVariable("education", profile.getEducation());
        ctx.setVariable("workHistory", profile.getWorkHistory());
        ctx.setVariable("projects", profile.getProjects());

        return templateEngine.process("profileTemplate", ctx);
    }

    //helper methods for formatting profile entity into string components
    private String getName(Profile profile) {
        String firstName = profile.getFirstName();
        String middleName = profile.getMiddleName();
        String lastName = profile.getLastName();

        return(
            (firstName != null ? firstName + " " : "") 
            + (middleName != null ? middleName + " ": "" )
            + (lastName != null ? lastName : "")
        );
    }

    private String getContact(Profile profile) {
        String phone = profile.getPhone();
        String email = profile.getEmail();

        return(
            (phone != null ? phone + " " : "")
            + (email != null ? "| " + email : "")
        );
    }

    public List<String> getTechnologies(String name) {
        if(name == null) {
            return new ArrayList<String>();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<JobTechnologyListEntry> cq = cb.createQuery(JobTechnologyListEntry.class);
        Root<JobTechnologyListEntry> root = cq.from(JobTechnologyListEntry.class);
        Predicate predicate = cb.like(cb.upper(root.get("text")), name.toUpperCase() + "%");
        cq.where(predicate).distinct(true);

        List<String> techs = entityManager.createQuery(cq).setMaxResults(5).getResultList().stream().map(
            tech -> tech.getText()
        ).collect(Collectors.toList());

        CriteriaQuery<ProjectTechnologyListEntry> _cq = cb.createQuery(ProjectTechnologyListEntry.class);
        Root<ProjectTechnologyListEntry> _root = _cq.from(ProjectTechnologyListEntry.class);
        Predicate _predicate = cb.like(cb.upper(_root.get("text")), name.toUpperCase() + "%");
        _cq.where(_predicate).distinct(true);

        entityManager.createQuery(_cq).setMaxResults(5).getResultList().stream().forEach(tech -> techs.add(tech.getText()));

        List<String> filteredTechs = techs.stream().map(tech -> tech.strip()).distinct().collect(Collectors.toList());
        TreeSet<String> unique = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        filteredTechs.removeIf(tech -> !unique.add(tech));
        return filteredTechs;
    }

}