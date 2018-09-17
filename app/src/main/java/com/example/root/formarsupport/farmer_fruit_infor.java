package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 3/3/2018.
 */

public class farmer_fruit_infor {
   private String Product_name,Product_type,city,TEHIL,DISTT,area_price,plants,harvest,latitude,longitude;

   public farmer_fruit_infor()
   {

   }

    public farmer_fruit_infor(String product_name, String product_type, String city, String TEHIL, String DISTT,
                              String area_price, String plants, String harvest, String latitude, String longitude) {
        Product_name = product_name;
        Product_type = product_type;
        this.city = city;
        this.TEHIL = TEHIL;
        this.DISTT = DISTT;
        this.area_price = area_price;
        this.plants = plants;
        this.harvest = harvest;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public void setProduct_type(String product_type) {
        Product_type = product_type;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTEHIL(String TEHIL) {
        this.TEHIL = TEHIL;
    }

    public void setDISTT(String DISTT) {
        this.DISTT = DISTT;
    }

    public void setArea_price(String area_price) {
        this.area_price = area_price;
    }

    public void setPlants(String plants) {
        this.plants = plants;
    }

    public void setHarvest(String harvest) {
        this.harvest = harvest;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public String getProduct_type() {
        return Product_type;
    }

    public String getCity() {
        return city;
    }

    public String getTEHIL() {
        return TEHIL;
    }

    public String getDISTT() {
        return DISTT;
    }

    public String getArea_price() {
        return area_price;
    }

    public String getPlants() {
        return plants;
    }

    public String getHarvest() {
        return harvest;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
