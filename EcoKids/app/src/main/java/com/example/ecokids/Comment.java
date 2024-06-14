package com.example.ecokids;

public class Comment {
    private String userId;
    private String comment;
    private long timestamp;

    public Comment() {

    }

    public Comment(String userId, String comment, long timestamp) {
        this.userId = userId;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
