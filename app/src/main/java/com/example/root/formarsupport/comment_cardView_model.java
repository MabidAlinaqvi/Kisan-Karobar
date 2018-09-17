package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 4/22/2018.
 */

public class comment_cardView_model {
  private  String name,comment,profile,uid;

  public comment_cardView_model()
  {

  }
    public comment_cardView_model(String name, String comment, String profile,String uid) {
        this.name = name;
        this.comment = comment;
        this.profile = profile;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
