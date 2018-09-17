package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 4/18/2018.
 */

public class cardview_model_farmer_firstpage {
    String name,id,city,product,comment,distt,rating,profile,phone,book;

    public cardview_model_farmer_firstpage() {
    }

    public cardview_model_farmer_firstpage(String name,String id, String city, String product, String comment, String distt, String rating,String profile,String Phone,String book) {
        this.id = id;
        this.city = city;
        this.product = product;
        this.comment = comment;
        this.distt = distt;
        this.rating = rating;
        this.profile=profile;
        this.phone=Phone;
        this.name=name;
        this.book=book;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDistt(String distt) {
        this.distt = distt;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getProduct() {
        return product;
    }

    public String getComment() {
        return comment;
    }

    public String getDistt() {
        return distt;
    }

    public String getRating() {
        return rating;
    }

    public String getProfile() {
        return profile;
    }

    public String getPhone() {
        return phone;
    }
}
