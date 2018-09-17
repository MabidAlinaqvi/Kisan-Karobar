package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 5/15/2018.
 */

public class fertilizer_signUp_infor {
    private String namee,phonee,ntnNum,id,profile;

    public fertilizer_signUp_infor()
    {

    }

    public fertilizer_signUp_infor(String namee, String phonee, String ntnNum, String id, String profile) {
        this.namee = namee;
        this.phonee = phonee;
        this.ntnNum = ntnNum;
        this.id = id;
        this.profile = profile;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public String getPhonee() {
        return phonee;
    }

    public void setPhonee(String phonee) {
        this.phonee = phonee;
    }

    public String getNtnNum() {
        return ntnNum;
    }

    public void setNtnNum(String ntnNum) {
        this.ntnNum = ntnNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
