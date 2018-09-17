package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 3/3/2018.
 */

public class trader_signUp_infor {
    String namee,phonee,ntnNum,id,profile,field;
public trader_signUp_infor()
{

}

    public trader_signUp_infor(String namee, String phonee, String ntnNum, String id,String profile,String field) {
        this.namee = namee;
        this.phonee = phonee;
        this.ntnNum = ntnNum;
        this.id = id;
        this.profile=profile;
        this.field=field;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public void setPhonee(String phonee) {
        this.phonee = phonee;
    }

    public void setNtnNum(String ntnNum) {
        this.ntnNum = ntnNum;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getNamee() {
        return namee;
    }

    public String getPhonee() {
        return phonee;
    }

    public String getNtnNum() {
        return ntnNum;
    }

    public String getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public String getField() {
        return field;
    }
}
