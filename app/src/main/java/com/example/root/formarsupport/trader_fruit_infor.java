package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 3/3/2018.
 */

public class trader_fruit_infor {
    private String pro_name,pro_type,city,tehsil,distt,area_price,latitude,longitude;

    public trader_fruit_infor()
    {

    }

    public trader_fruit_infor(String pro_name, String pro_type, String city,
                              String tehsil, String distt, String area_price, String latitude, String longitude) {
        this.pro_name = pro_name;
        this.pro_type = pro_type;
        this.city = city;
        this.tehsil = tehsil;
        this.distt = distt;
        this.area_price = area_price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public void setDistt(String distt) {
        this.distt = distt;
    }

    public void setArea_price(String area_price) {
        this.area_price = area_price;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPro_name() {
        return pro_name;
    }

    public String getPro_type() {
        return pro_type;
    }

    public String getCity() {
        return city;
    }

    public String getTehsil() {
        return tehsil;
    }

    public String getDistt() {
        return distt;
    }

    public String getArea_price() {
        return area_price;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
