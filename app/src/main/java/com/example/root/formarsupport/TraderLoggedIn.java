package com.example.root.formarsupport;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

public class TraderLoggedIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    first_page_of_Login_User _first_pageOfLoginUser =new first_page_of_Login_User();

    // BookMarkedUsers bookMarkedUsers=new BookMarkedUsers();
    // Trader_fruit_click_Own_listItem own_listItem=new Trader_fruit_click_Own_listItem();
    FragmentTransaction transaction;
    FragmentManager manager=getFragmentManager();
    FirebaseUser currentUser;
    CircleImageView profileImage;
    TextView name,phoneNum;
    Farmer_signup_infor farmer_signup_infor;
  //  private FarmerChoiceDone farmerChoiceDone;
 //   private traderChoiceDone traderChoicedone;
    private String choice_farmer;
    private DatabaseReference userReference,UsersReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader_logged_in);
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            String online_user=currentUser.getUid();
         //   userReference = FirebaseDatabase.getInstance().getReference("cardview").child(online_user);
            UsersReference=FirebaseDatabase.getInstance().getReference("Users").child(online_user).child("Time");
        }
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // FirebaseAuth.getInstance().signOut();
        setSupportActionBar(toolbar);*/

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        transaction=manager.beginTransaction();
        transaction.replace(R.id.traderFrame, _first_pageOfLoginUser);
        transaction.commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_viewtrader);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        profileImage =  header.findViewById(R.id.profileimageView);
        name = header.findViewById(R.id.username);
        phoneNum = header.findViewById(R.id.textView);
        farmer_signup_infor=new Farmer_signup_infor();
        String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference personalReference= FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("personal");
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
                            Picasso.with(TraderLoggedIn.this)
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

    /*@Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof FarmerChoiceDone) {
            farmerChoiceDone = (FarmerChoiceDone) fragment;
          //  traderChoicedone=(traderChoiceDone)fragment;
        }
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trader_logged_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

      /*  if (id == R.id.search) {
            transaction=manager.beginTransaction();
            transaction.add(R.id.content_main, _first_pageOfLoginUser);
            transaction.commit();
        }  else*/ if (id == R.id.inbox) {

            Friends friends=new Friends();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.traderFrame,friends);
            transaction.commit();


        }else if(id==R.id.traderRequest)
        {
            receivedOrSentRequests recvOrSent=new receivedOrSentRequests();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.traderFrame,recvOrSent);
            transaction.commit();
        } else if (id == R.id.bookmarks) {

            BookMarkedUsers bookMarkedUsers=new BookMarkedUsers();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.traderFrame,bookMarkedUsers);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.logout) {

            if(currentUser!=null)
            {
                UsersReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
                UsersReference.child("online").setValue("false");
            }

            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(TraderLoggedIn.this,LoginActivity.class);
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
            transaction.replace(R.id.traderFrame,first_page_of_login);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id==R.id.profile)
        {
            TraderProfile traderProductList=new TraderProfile();
            FragmentTransaction transaction;
            FragmentManager manager=getFragmentManager();
            transaction=manager.beginTransaction();
            transaction.replace(R.id.traderFrame,traderProductList);
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
        Intent intent=new Intent(TraderLoggedIn.this,LoginActivity.class);
        startActivity(intent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.removeValue();
        DatabaseReference cardRefrence=FirebaseDatabase.getInstance().getReference("cardview").child(uid);
        cardRefrence.removeValue();

        //delete from authentication
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(TraderLoggedIn.this,"account deleted",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        /*Intent loginPage = new Intent(Main2Activity.this, Main2Activity.class);
        startActivity(loginPage);*/
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent startpageIntent = new Intent(TraderLoggedIn.this, LoginActivity.class);
            //    startpageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //user will be unable to press back button
            startActivity(startpageIntent);
            finish();
        } else {
            UsersReference.child("timeStamp").setValue(ServerValue.TIMESTAMP);
            UsersReference.child("online").setValue("true");

        }


    }

   /* public void trader_camera(View view) {
        Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
        traderChoicedone.SendData("camera");
    }

    public void trader_gallery(View view) {
        traderChoicedone.SendData("gallery");
        Toast.makeText(this, "gallery", Toast.LENGTH_SHORT).show();
    }*/

    /*public void send_request(View view) {
        farmerChoiceDone.sendData("send");
    }
    public void cancel_request(View view)
    {
        farmerChoiceDone.sendData("cancel");
    }*/
}
