package com.example.root.formarsupport;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Frame extends AppCompatActivity {
FragmentTransaction transaction;
FragmentManager manager=getFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        first_page_of_Login_User _first_pageOfLoginUser =new first_page_of_Login_User();
        transaction=manager.beginTransaction();
        transaction.add(R.id.frame, _first_pageOfLoginUser);
        transaction.commit();
    }
}
