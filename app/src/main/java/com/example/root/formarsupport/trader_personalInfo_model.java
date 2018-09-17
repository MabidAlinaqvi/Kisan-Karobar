package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 4/19/2018.
 */

public class trader_personalInfo_model {
   String namee,phonee,ntnNum,id,profile,field;
    public trader_personalInfo_model(){

    }
    public trader_personalInfo_model(String namee, String phonee, String ntnNum, String id, String profile,String field) {
        this.namee = namee;
        this.phonee = phonee;
        this.ntnNum = ntnNum;
        this.id = id;
        this.profile = profile;
        this.field=field;
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

    public void setField(String field) {
        this.field = field;
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

    public String getField() {
        return field;
    }
}
