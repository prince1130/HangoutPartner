package nyc.pleasure.hangoutpartneralpha.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chien on 12/21/2015.
 */
public class User {

    private String userId = null;
    private String displayName = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String gender = null;
    private Integer age = null;
    private String selfIntroduction = null;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
}
