package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 3/3/2018.
 */

public class trader_crop_infor {
   private String product_name,city,TEHSIL,DISTT,Sack_price,quantity,latitude,longitude;

   public trader_crop_infor()
   {

   }

    public trader_crop_infor(String product_name, String city, String TEHSIL, String DISTT,
                             String sack_price, String quantity, String latitude, String longitude) {
        this.product_name = product_name;
        this.city = city;
        this.TEHSIL = TEHSIL;
        this.DISTT = DISTT;
        Sack_price = sack_price;
        this.quantity = quantity;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setSack_price(String sack_price) {
        Sack_price = sack_price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getSack_price() {
        return Sack_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
