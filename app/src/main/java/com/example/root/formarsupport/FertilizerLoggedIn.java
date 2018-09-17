package com.example.root.formarsupport;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FertilizerLoggedIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    first_page_of_Login_User first_page_of_login =new first_page_of_Login_User();
    FragmentTransaction transaction;
    FragmentManager manager=getFragmentManager();
    FirebaseAuth mAuth;
    CircleImageView profileImage;
    TextView name,phoneNum;
    fertilizer_signUp_infor fertilizer_signup;
    private FarmerChoiceDone farmerChoiceDone;
    private String choice_farmer;
    private FirebaseUser currentUser;
    private DatabaseReference userReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_logged_in);

        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            String online_user=currentUser.getUid();
            userReference = FirebaseDatabase.getInstance().getReference("cardview").child(online_user);
        }

        transaction=manager.beginTransaction();
        transaction.replace(R.id.Fertilizer_Frame, first_page_of_login);
        transaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        profileImage =  header.findViewById(R.id.profileimageView);
        name = header.findViewById(R.id.username);
        phoneNum = header.findViewById(R.id.textView);
        fertilizer_signup=new fertilizer_signUp_infor();
        String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference personalReference=FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("personal");
        personalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    fertilizer_signup = dataSnapshot.getValue(fertilizer_signUp_infor.class);
                    String names = fertilizer_signup.getNamee();
                    String profile = fertilizer_signup.getProfile();
                    if (!TextUtils.isEmpty(names)) {
                        name.setText(names);
                        phoneNum.setText(fertilizer_signup.getPhonee());
                        if (!TextUtils.isEmpty(profile)) {
                            Picasso.with(FertilizerLoggedIn.this)
                                    .load(profile)
                                    .placeholder(R.drawable.profile1)
                                    .into(profileImage);
                        } else {
                            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
                        }
                    }
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.search) {
          *//*  transaction=manager.beginTransaction();
            transaction.add(R.id.content_main, _first_pageOfLoginUser);
            transaction.commit();*//*
        } else*/ if (id == R.id.inbox) {

            Friends friends=new Friends();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.Fertilizer_Frame,friends);
            transaction.commit();


        }else if(id==R.id.receiveRequest)
        {
            receivedOrSentRequests recvOrSent=new receivedOrSentRequests();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.Fertilizer_Frame,recvOrSent);
            transaction.commit();
        }

        else if (id == R.id.bookmarks) {
            BookMarkedUsers bookMarkedUsers=new BookMarkedUsers();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.Fertilizer_Frame,bookMarkedUsers);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.logout) {

            if(currentUser!=null)
            {
                userReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
                userReference.child("online").setValue("false");
            }
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(FertilizerLoggedIn.this,LoginActivity.class);
            startActivity(intent);


        }
        else if(id==R.id.delete_account){
            //  Toast.makeText(Farmer_logged_In.this,getResources().getString(R.string.editcomment),Toast.LENGTH_SHORT).show();
            try{
        //        deleteAccount();
            }
            catch (Exception e)
            {

            }
        }
        else if(id==R.id.userhome)
        {
            first_page_of_Login_User first_page_of_login=new first_page_of_Login_User();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.Fertilizer_Frame,first_page_of_login);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id==R.id.profile)
        {
            FertilizerProfile ProductList=new FertilizerProfile();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.Fertilizer_Frame,ProductList);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
