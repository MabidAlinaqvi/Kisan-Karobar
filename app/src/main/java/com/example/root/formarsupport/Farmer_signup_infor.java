package com.example.root.formarsupport;

import java.util.ArrayList;

/**
 * Created by Tom Brain on 2/22/2018.
 */

public class Farmer_signup_infor {
    private String namee,phonee,id,profile,field;
   /* private ArrayList<String> SaveImages=new ArrayList<>();*/

   public Farmer_signup_infor()
            {

            }

    public Farmer_signup_infor(String namee, String phonee, String id, String profile,String field) {
        this.namee = namee;
        this.phonee = phonee;
        this.id = id;
        this.profile=profile;
        this.field=field;
        //SaveImages = saveImages;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public void setPhonee(String phonee) {
        this.phonee = phonee;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setField(String field) {
        this.field = field;
    }
    /*  public void setSaveImages(ArrayList<String> saveImages) {
        SaveImages = saveImages;
    }*/



    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNamee() {
        return namee;
    }

    public String getPhonee() {
        return phonee;
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
    /* public ArrayList<String> getSaveImages() {
        return SaveImages;
    }*/
}
