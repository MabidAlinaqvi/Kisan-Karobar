package com.example.root.formarsupport;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class BrokerLoggedIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    first_page_of_Login_User _first_pageOfLoginUser =new first_page_of_Login_User();
    FragmentTransaction transaction;
    FragmentManager manager=getFragmentManager();
    FirebaseAuth mAuth;
    CircleImageView profileImage;
    TextView name,phoneNum;
    private String choice_farmer;
    private FirebaseUser currentUser;
    private DatabaseReference userReference,UsersReference;
    private broker_infor broker_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_logged_in);
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            String online_user=currentUser.getUid();
            //userReference = FirebaseDatabase.getInstance().getReference("cardview").child(online_user);
            UsersReference=FirebaseDatabase.getInstance().getReference("Users").child(online_user).child("Time");
        }
        /**
         * default page to show
         */

        transaction=manager.beginTransaction();
        transaction.replace(R.id.brokerFrame, _first_pageOfLoginUser);
        transaction.commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        profileImage =  header.findViewById(R.id.brokerprofileimageView);
        name = header.findViewById(R.id.brokerusername);
        phoneNum = header.findViewById(R.id.brokertextView);
        broker_info=new broker_infor();
        String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference personalReference=FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("personal");
        personalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    broker_info = dataSnapshot.getValue(broker_infor.class);
                    String names = broker_info.getName();
                    String profile = broker_info.getProfile();
                    if (!TextUtils.isEmpty(names)) {
                        name.setText(names);
                        phoneNum.setText(broker_info.getPhone_num());
                        if (!TextUtils.isEmpty(profile)) {
                            Picasso.with(BrokerLoggedIn.this)
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

      /*  if (id == R.id.search) {
          *//*  transaction=manager.beginTransaction();
            transaction.add(R.id.content_main, _first_pageOfLoginUser);
            transaction.commit();*//*
        } else */if (id == R.id.inbox) {

            Friends friends=new Friends();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.brokerFrame,friends);
            transaction.commit();


        }else if(id==R.id.receiveRequest)
        {
            receivedOrSentRequests recvOrSent=new receivedOrSentRequests();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.brokerFrame,recvOrSent);
            transaction.commit();
        }

        else if (id == R.id.bookmarks) {
            BookMarkedUsers bookMarkedUsers=new BookMarkedUsers();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.brokerFrame,bookMarkedUsers);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.logout) {

            if(currentUser!=null)
            {
                UsersReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
                UsersReference.child("online").setValue("false");
            }
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(BrokerLoggedIn.this,LoginActivity.class);
            startActivity(intent);

        }
        else if(id==R.id.delete_account){
            //  Toast.makeText(Farmer_logged_In.this,getResources().getString(R.string.editcomment),Toast.LENGTH_SHORT).show();
            try{
                deleteAccount();
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
            transaction.replace(R.id.brokerFrame,first_page_of_login);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id==R.id.profile)
        {
            BrokerProfile farmerProductList=new BrokerProfile();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.brokerFrame,farmerProductList);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteAccount() {
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        //signout
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(BrokerLoggedIn.this,LoginActivity.class);
        startActivity(intent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //delete data from database

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.removeValue();
        DatabaseReference cardRefrence=FirebaseDatabase.getInstance().getReference("cardview").child(uid);
        cardRefrence.removeValue();

        //delete from authentication
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(BrokerLoggedIn.this,"account deleted",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(currentUser!=null)
        {
            UsersReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
            UsersReference.child("online").setValue("false");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent startpageIntent = new Intent(BrokerLoggedIn.this, LoginActivity.class);
            startActivity(startpageIntent);
            finish();
        } else {
            UsersReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
            UsersReference.child("online").setValue("true");

        }


    }
}
