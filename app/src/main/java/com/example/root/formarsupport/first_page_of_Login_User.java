package com.example.root.formarsupport;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class first_page_of_Login_User extends Fragment {

    View view;
    DatabaseReference databaseReference,bookOrnotReference;
    FirebaseDatabase firebaseDatabase;
    cardview_model_farmer_firstpage model_data,bookOrNot;
    ArrayList<cardview_model_farmer_firstpage> model_array;
    card_view_adapter_farmer_firstpage adapter;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    private Toolbar mToolbar;
    ImageView bookmark;
   // private DrawerLayout drawer;
    String uid;
    private int h=0;
  //  ArrayList<String> bookedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.first_page_of_login_user, container, false);
        /*LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View farmerLoggedIn=layoutInflater.inflate(R.layout.app_bar_farmer_logged__in,null);*/
        mToolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.userList));
        /*drawer = view.findViewById(R.id.drawer_layoutwas);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(h==0) {
                    drawer.openDrawer(GravityCompat.START);
                    h=h+1;
                }
                else
                {
                    drawer.closeDrawer(GravityCompat.START);
                    h=0;
                }
            }
        });
*/

       /* bookedUser=new ArrayList<>();
        itemView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.cardview_farmer_first_page, container, false);
        bookmark=itemView.findViewById(R.id.bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"bookmarked",Toast.LENGTH_SHORT).show();
            }
        });*/

        model_data=new cardview_model_farmer_firstpage();
        recyclerView=view.findViewById(R.id.recycler_view);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("cardview");
        model_array=new ArrayList<cardview_model_farmer_firstpage>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                model_data=dataSnapshot.getValue(cardview_model_farmer_firstpage.class);
                uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                String modeluid=model_data.getPhone();
                if(!uid.equals(modeluid)) {
                    model_array.add(model_data);
                    adapter = new card_view_adapter_farmer_firstpage(getActivity(), model_array);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
                //}

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*for(int i=0;i<model_array.size();i++) {
            Log.d("bookTimes", model_array.size()+"");
            bookOrNot = new cardview_model_farmer_firstpage();
            bookOrNot = model_array.get(i);
            Log.d("bookTimes", bookOrNot.getPhone());
            bookOrnotReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("bookmarks");
            bookOrnotReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("bookTimes", bookOrNot.getPhone());
                    if (dataSnapshot.hasChild(bookOrNot.getPhone())) {
                        bookedUser.add(bookOrNot.getPhone());
                        Log.d("bookTimes", bookOrNot.getPhone());
                        //holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarked));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
        return view;
    }

}
