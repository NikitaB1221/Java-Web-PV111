package step.learning.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Comment {
    private UUID id;
    private UUID userId;
    private UUID newsId;
    private String text;
    private Date createDt;
    private Date deleteDt;

    private User user;

    public User getUser() {
        return user;
    }

    public Comment() {
    }

    public Comment(ResultSet resultSet) throws SQLException {
        this.setId(UUID.fromString(resultSet.getString("id")));
        this.setUserId(UUID.fromString(resultSet.getString("user_id")));
        ;
        this.setNewsId(UUID.fromString(resultSet.getString("news_id")));
        ;
        this.setText(resultSet.getString("text"));
        Timestamp timestamp = resultSet.getTimestamp("created_dt");
        if (timestamp != null) {
            this.setCreateDt(new Date(timestamp.getTime()));
        }
        timestamp = resultSet.getTimestamp("deleted_dt");
        if (timestamp != null) {
            this.setDeleteDt(new Date(timestamp.getTime()));
        }
        try{
            user = new User(resultSet);
        } catch (Exception ignore) {}
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getNewsId() {
        return newsId;
    }

    public void setNewsId(UUID newsId) {
        this.newsId = newsId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getDeleteDt() {
        return deleteDt;
    }

    public void setDeleteDt(Date deleteDt) {
        this.deleteDt = deleteDt;
    }
}
