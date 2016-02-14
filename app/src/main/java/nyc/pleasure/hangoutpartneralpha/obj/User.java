package nyc.pleasure.hangoutpartneralpha.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chien on 12/21/2015.
 */
public class User {

    private String userId = null;
    private Long joinedDate = null;
    private String firstName = null;
    private String lastName = null;
    private String displayName = null;
    private Long birthDate = null;
    private String gender = null;

    private String email = null;
    private Integer zipCode = null;

    private String descriptionMe = null;
    private String descriptionIdealPartner = null;
    private String descriptionReasonJoinMe = null;

    private List<String> photos = new ArrayList<String>();
    private List<String> videos = new ArrayList<String>();
    private List<String> feedback = new ArrayList<String>();

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescriptionMe() {
        return descriptionMe;
    }

    public void setDescriptionMe(String descriptionMe) {
        this.descriptionMe = descriptionMe;
    }

    public String getDescriptionIdealPartner() {
        return descriptionIdealPartner;
    }

    public void setDescriptionIdealPartner(String descriptionIdealPartner) {
        this.descriptionIdealPartner = descriptionIdealPartner;
    }
}
