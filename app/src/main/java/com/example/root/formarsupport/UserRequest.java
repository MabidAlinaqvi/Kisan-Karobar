package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 5/5/2018.
 */

public class UserRequest {
   private String request_type;

   public UserRequest()
   {

   }

    public UserRequest(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
