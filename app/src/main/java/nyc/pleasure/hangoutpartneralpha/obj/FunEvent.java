package nyc.pleasure.hangoutpartneralpha.obj;

/**
 * Created by Chien on 12/21/2015.
 */
public class FunEvent {

    private String eventId = null;
    private String createrUserId = null;
    private String createrUserDisplayName = null;
    private Long createdTime = null;
    private String title = null;
    private String detail = null;
    private Long startTime = null;
    private Long endTime = null;
    private String locationName = null;
    private String locationAddress = null;
    private String descriptionIdealPartner = null;
    private String descriptionReasonJoinMe = null;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCreaterUserId() {
        return createrUserId;
    }

    public void setCreaterUserId(String createrUserId) {
        this.createrUserId = createrUserId;
    }

    public String getCreaterUserDisplayName() {
        return createrUserDisplayName;
    }

    public void setCreaterUserDisplayName(String createrUserDisplayName) {
        this.createrUserDisplayName = createrUserDisplayName;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getDescriptionIdealPartner() {
        return descriptionIdealPartner;
    }

    public void setDescriptionIdealPartner(String descriptionIdealPartner) {
        this.descriptionIdealPartner = descriptionIdealPartner;
    }

    public String getDescriptionReasonJoinMe() {
        return descriptionReasonJoinMe;
    }

    public void setDescriptionReasonJoinMe(String descriptionReasonJoinMe) {
        this.descriptionReasonJoinMe = descriptionReasonJoinMe;
    }
}
