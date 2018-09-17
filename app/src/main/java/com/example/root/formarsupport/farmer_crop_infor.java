package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 2/22/2018.
 */

public class farmer_crop_infor {
    private String  Product_name, Product_type, city, TEHIL, DISTT, area, sack_price, quantity,lati,longi;

    public farmer_crop_infor()
    {

    }

    public farmer_crop_infor(String product_name, String product_type, String city, String TEHIL, String DISTT, String area, String sack_price, String quantity,String lati,String longi) {
        Product_name = product_name;
        Product_type = product_type;
        this.city = city;
        this.TEHIL = TEHIL;
        this.DISTT = DISTT;
        this.area = area;
        this.sack_price = sack_price;
        this.quantity = quantity;
        this.lati=lati;
        this.longi=longi;
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

    public void setArea(String area) {
        this.area = area;
    }

    public void setSack_price(String sack_price) {
        this.sack_price = sack_price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getArea() {
        return area;
    }

    public String getSack_price() {
        return sack_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getLati() {
        return lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLati(String lati) {

        this.lati = lati;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }
}
