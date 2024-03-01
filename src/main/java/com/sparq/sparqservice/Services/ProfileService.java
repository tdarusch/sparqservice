package com.sparq.sparqservice.Services;

import java.io.ByteArrayOutputStream;
import java.util.List;

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

        profile.setUser(currentProfile.getUser());

        if(profile.getMasterProfile() == true) {
            List<Profile> oldMasterProfs = profileRepo.findByNameContainingIgnoreCase("old master profile");
            List<Profile> masterProfs = profileRepo.findByUserAndMasterProfile(currentProfile.getUser(), true);
            for(Profile p : masterProfs) {
                p.setMasterProfile(false);
                p.setName("Old Master Profile" + (oldMasterProfs.size() == 0 ? "" : " " + Integer.toString(oldMasterProfs.size() + 1)));
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
        ctx.setVariable("headline", profile.getContact().getHeadline());
        ctx.setVariable("contact", getContact(profile));
        ctx.setVariable("bio", getBio(profile));
        ctx.setVariable("bulletList", profile.getAbout() != null ? profile.getAbout().getBulletList() : null);
        ctx.setVariable("skills", profile.getSkills());
        ctx.setVariable("education", profile.getEducation());
        ctx.setVariable("workHistory", profile.getWorkHistory());
        ctx.setVariable("projects", profile.getProjects());

        return templateEngine.process("profileTemplate", ctx);
    }

    //helper methods for formatting profile entity into string components
    private String getName(Profile profile) {
        if(profile.getContact() == null) {
            return "";
        }
        String firstName = profile.getContact().getFirstName();
        String middleName = profile.getContact().getMiddleName();
        String lastName = profile.getContact().getLastName();

        return(
            (firstName != null ? firstName + " " : "") 
            + (middleName != null ? middleName + " ": "" )
            + (lastName != null ? lastName : "")
        );
    }

    private String getContact(Profile profile) {
        if(profile.getContact() == null) {
            return "";
        }

        String phone = profile.getContact().getPhone();
        String email = profile.getContact().getEmail();

        return(
            (phone != null ? phone + " " : "")
            + (email != null ? "| " + email : "")
        );
    }

    private String getBio(Profile profile) {
        if(profile.getAbout() == null) {
            return "";
        }

        return(profile.getAbout().getDescription() != null ? profile.getAbout().getDescription() : "");
    }

}