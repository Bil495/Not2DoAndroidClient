package bil495.not2do.model;

import java.util.Date;

/**
 * Created by burak on 7/7/2017.
 */

public class Not2DoModel {
    private long id;
    private UserModel creator;
    private String content;
    private Date createdAd;
    private int participants;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserModel getCreator() {
        return creator;
    }

    public void setCreator(UserModel creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAd() {
        return createdAd;
    }

    public void setCreatedAd(Date createdAd) {
        this.createdAd = createdAd;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
}
