package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 5/5/2018.
 */

public class Messages {
    private String messages,type,profile;
    private Boolean seen;
    private long time;
    private String from;

    public Messages()
    {

    }

    public Messages(String messages, String type, Boolean seen, long time, String from,String profile) {
        this.messages = messages;
        this.type = type;
        this.seen = seen;
        this.time = time;
        this.from=from;
        this.profile=profile;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
