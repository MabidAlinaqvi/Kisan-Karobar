package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 5/15/2018.
 */

public class fertilizer_pestcide_model {
    private String  Product_name, pest_name, city, TEHIL, DISTT, medic_name, RateUse, quantity,Usage,lati,longi;

    public fertilizer_pestcide_model()
    {

    }

    public fertilizer_pestcide_model(String product_name, String pest_name, String city, String TEHIL, String DISTT, String medic_name, String rateUse, String quantity, String usage, String lati, String longi) {
        Product_name = product_name;
        this.pest_name = pest_name;
        this.city = city;
        this.TEHIL = TEHIL;
        this.DISTT = DISTT;
        this.medic_name = medic_name;
        RateUse = rateUse;
        this.quantity = quantity;
        Usage = usage;
        this.lati = lati;
        this.longi = longi;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getPest_name() {
        return pest_name;
    }

    public void setPest_name(String pest_name) {
        this.pest_name = pest_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTEHIL() {
        return TEHIL;
    }

    public void setTEHIL(String TEHIL) {
        this.TEHIL = TEHIL;
    }

    public String getDISTT() {
        return DISTT;
    }

    public void setDISTT(String DISTT) {
        this.DISTT = DISTT;
    }

    public String getMedic_name() {
        return medic_name;
    }

    public void setMedic_name(String medic_name) {
        this.medic_name = medic_name;
    }

    public String getRateUse() {
        return RateUse;
    }

    public void setRateUse(String rateUse) {
        RateUse = rateUse;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUsage() {
        return Usage;
    }

    public void setUsage(String usage) {
        Usage = usage;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }
}
