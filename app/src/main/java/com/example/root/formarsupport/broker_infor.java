package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 2/22/2018.
 */

public class broker_infor {
    private String name,NTN,phone_num,pass,product_name,city,TEHSIL,DISTT,product_quant,id,latitude,longitude,profile;

    public broker_infor()
    {

    }

    public broker_infor(String name, String NTN, String phone_num, String pass, String product_name, String city, String TEHSIL, String DISTT, String product_quant,String id, String latitude, String longitude,String profile) {
        this.name = name;
        this.NTN = NTN;
        this.phone_num = phone_num;
        this.pass = pass;
        this.product_name = product_name;
        this.city = city;
        this.TEHSIL = TEHSIL;
        this.DISTT = DISTT;
        this.product_quant = product_quant;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profile=profile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNTN(String NTN) {
        this.NTN = NTN;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTEHSIL(String TEHSIL) {
        this.TEHSIL = TEHSIL;
    }

    public void setDISTT(String DISTT) {
        this.DISTT = DISTT;
    }

    public void setProduct_quant(String product_quant) {
        this.product_quant = product_quant;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public String getNTN() {
        return NTN;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getPass() {
        return pass;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getCity() {
        return city;
    }

    public String getTEHSIL() {
        return TEHSIL;
    }

    public String getDISTT() {
        return DISTT;
    }

    public String getProduct_quant() {
        return product_quant;
    }

    public String getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getProfile() {
        return profile;
    }
}
