package bil495.not2do.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by burak on 7/7/2017.
 */

public class Not2DoModel implements Serializable{
    private long id;
    private UserModel creator;
    private String content;
    private Date createdAt;
    private int participants;
    private int failures;
    private boolean didParticipate;
    private boolean didFail;

    public boolean isDidParticipate() {
        return didParticipate;
    }

    public void setDidParticipate(boolean didParticipate) {
        this.didParticipate = didParticipate;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public boolean isDidFail() {
        return didFail;
    }

    public void setDidFail(boolean didFail) {
        this.didFail = didFail;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    @Override
    public String toString() {
        return "Not2DoModel{" +
                "id=" + id +
                ", creator=" + creator +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", participants=" + participants +
                ", didParticipate=" + didParticipate +
                '}';
    }
}
