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
import android.util.Log;
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

public class Farmer_logged_In extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    first_page_of_Login_User _first_pageOfLoginUser =new first_page_of_Login_User();

   // BookMarkedUsers bookMarkedUsers=new BookMarkedUsers();
   // Trader_fruit_click_Own_listItem own_listItem=new Trader_fruit_click_Own_listItem();
    FragmentTransaction transaction;
    FragmentManager manager=getFragmentManager();
    FirebaseAuth mAuth;
    CircleImageView profileImage;
    TextView name,phoneNum;
    Farmer_signup_infor farmer_signup_infor;
    private FarmerChoiceDone farmerChoiceDone;
    private String choice_farmer;
    private FirebaseUser currentUser;
    private DatabaseReference userReference,UsersReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_logged__in);
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            String online_user=currentUser.getUid();
           // userReference = FirebaseDatabase.getInstance().getReference("cardview").child(online_user);
            UsersReference=FirebaseDatabase.getInstance().getReference("Users").child(online_user).child("Time");
        }
        //String=FirebaseAuth.getInstance().getCurrentUser().getUid();
       // FirebaseAuth.getInstance().signOut();
        /**
         * default page to show
         */
       /* transaction=manager.beginTransaction();
        transaction.add(R.id.content_main,_first_pageOfLoginUser);
        transaction.commit();*/
      /*  transaction=manager.beginTransaction();
        transaction.add(R.id.content_main,farmer_list_of_product);
        transaction.commit();*/
        /*transaction=manager.beginTransaction();
        transaction.add(R.id.frame3,own_listItem);
        transaction.commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame3, _first_pageOfLoginUser);
       // transaction.add(R.id.frame3,bookMarkedUsers);
        transaction.commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        profileImage =  header.findViewById(R.id.profileimageView);
        name = header.findViewById(R.id.username);
        phoneNum = header.findViewById(R.id.textView);
        farmer_signup_infor=new Farmer_signup_infor();
        String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference personalReference=FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("personal");
        personalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               try{
                    farmer_signup_infor = dataSnapshot.getValue(Farmer_signup_infor.class);
                    String names = farmer_signup_infor.getNamee();
                    String profile = farmer_signup_infor.getProfile();
                    if (!TextUtils.isEmpty(names)) {
                        name.setText(names);
                        phoneNum.setText(farmer_signup_infor.getPhonee());
                        if (!TextUtils.isEmpty(profile)) {
                            Picasso.with(Farmer_logged_In.this)
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.farmer_logged__in, menu);

     *//**
         * doing but no result naqvi
         *//*
     *//*TextView textView=findViewById(R.id.searchPeople);
     MenuItem menuItem=menu.findItem(R.id.bookmarks);
     menuItem.setTitle(textView.getText());*//*
     *//*
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "urdu.ttf");
        SpannableStringBuilder title = new SpannableStringBuilder(getResources().getString(R.string.bookmarks));
        title.setSpan(tf, 0, title.length(), 0);
        MenuItem menuItem = menu.findItem(R.id.bookmarks); // OR THIS
        menuItem.setTitle(title);*//*


        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(Farmer_logged_In.this,LoginActivity.class);
            startActivity(intent);
        //    Toast.makeText(this,"logout successfully",Toast.LENGTH_SHORT).show();
            //return true;
        }
        if(id==R.id.action_delete)
        {
            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            final FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
            //signout
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(Farmer_logged_In.this,LoginActivity.class);
            startActivity(intent);

            //delete data from database



            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uid);
            databaseReference.removeValue();
            DatabaseReference cardRefrence=FirebaseDatabase.getInstance().getReference("cardview").child(uid);
            cardRefrence.removeValue();

            //delete from authentication
            currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Farmer_logged_In.this,"account deleted",Toast.LENGTH_SHORT).show();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.search) {
          *//*  transaction=manager.beginTransaction();
            transaction.add(R.id.content_main, _first_pageOfLoginUser);
            transaction.commit();*//*
        } else */if (id == R.id.inbox) {

            Friends friends=new Friends();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame3,friends);
            transaction.commit();


        }else if(id==R.id.receiveRequest)
        {
            receivedOrSentRequests recvOrSent=new receivedOrSentRequests();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame3,recvOrSent);
            transaction.commit();
        }

        else if (id == R.id.bookmarks) {
            BookMarkedUsers bookMarkedUsers=new BookMarkedUsers();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame3,bookMarkedUsers);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.logout) {

            if(currentUser!=null)
            {
                UsersReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
                UsersReference.child("online").setValue("false");
            }
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(Farmer_logged_In.this,LoginActivity.class);
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
            transaction.replace(R.id.frame3,first_page_of_login);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id==R.id.profile)
        {
            FarmerProfile farmerProductList=new FarmerProfile();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame3,farmerProductList);
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
        Intent intent=new Intent(Farmer_logged_In.this,LoginActivity.class);
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
                Toast.makeText(Farmer_logged_In.this,"account deleted",Toast.LENGTH_SHORT).show();
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
            //   databaseReference.child("online").setValue("false");
            //    Log.d("PauseOrStop", "stop");
        }
    }

    //profileimageafterlogin.xml

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof FarmerChoiceDone) {
            farmerChoiceDone = (FarmerChoiceDone) fragment;
        }
    }

    public void use_camera(View view) {
        farmerChoiceDone.sendData("camera");
    }

    public void use_gallery(View view) {
        farmerChoiceDone.sendData("gallery");
    }

    /*public void send_requests(View view) {
        Log.d("send_requests", "clik");
        farmerChoiceDone.sendData("send");
    }
    public void cancel_requests(View view)
    {
        Log.d("send_requests", "click");
        farmerChoiceDone.sendData("cancel");
    }*/


    //farmer choice to register crop or fruit

    public void crops(View view)
    {
        choice_farmer="crop";
    }
    public void fruits(View view)
    {
        choice_farmer="fruit";
    }

    public void moveFarmer(View view)
    {
       // view.startAnimation(anim_alpha);

        if (choice_farmer == null) {

            Toast.makeText(this, getResources().getString(R.string.atleastOne), Toast.LENGTH_LONG).show();
        } else {
            farmerChoiceDone.sendData(choice_farmer);
        }

    }

    public void send_requests(View view) {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Intent loginPage = new Intent(Main2Activity.this, Main2Activity.class);
        startActivity(loginPage);*/
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent startpageIntent = new Intent(Farmer_logged_In.this, LoginActivity.class);
            //    startpageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //user will be unable to press back button
            startActivity(startpageIntent);
            finish();
        } else {
            UsersReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
            UsersReference.child("online").setValue("true");

        }


    }
}
