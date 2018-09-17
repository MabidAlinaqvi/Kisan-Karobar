package com.example.root.formarsupport;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookMarkedUsers extends Fragment {
    View view;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    cardview_model_farmer_firstpage model_data;
    private Toolbar mToolbar;
    ArrayList<cardview_model_farmer_firstpage> model_array;
    bookmarked_adapter adapter;
    RecyclerView recyclerView;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_book_marked_users, container, false);
        mToolbar=view.findViewById(R.id.book_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.bookmarks));
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        model_data=new cardview_model_farmer_firstpage();
        recyclerView=view.findViewById(R.id.recycler_view);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users").child(uid).child("bookmarks");
        model_array=new ArrayList<cardview_model_farmer_firstpage>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                model_data=dataSnapshot.getValue(cardview_model_farmer_firstpage.class);
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String modeluid=model_data.getPhone();
                if(!uid.equals(modeluid)) {
                    model_array.add(model_data);
                    adapter = new bookmarked_adapter(getActivity(), model_array);
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
        return view;
    }

}
