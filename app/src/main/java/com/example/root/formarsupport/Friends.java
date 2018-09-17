package com.example.root.formarsupport;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {

    View view;
    DatabaseReference friendsReference,UserReference,userIdReference,OnlineUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView friendsList;
    FirebaseAuth mAuth;
    String online_user_id,id,user_id;
    static String name,listuserid;
    private Toolbar mToolbar;
    cardview_model_farmer_firstpage user_card;
    String profile,title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_friends, container, false);
        mToolbar=view.findViewById(R.id.friends_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.friends));
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        friendsList=view.findViewById(R.id.friends_list);
        online_user_id=mAuth.getCurrentUser().getUid();
        friendsReference=firebaseDatabase.getReference().child("Friends").child(online_user_id);
        UserReference=firebaseDatabase.getReference("Users");
        userIdReference=firebaseDatabase.getReference("Users").child(online_user_id);
        friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        userIdReference.child("personal").child("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_id=dataSnapshot.getValue().toString();
                Log.d("user_idWas", user_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Friends_model,FriendsViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Friends_model, FriendsViewHolder>
                (
                        Friends_model.class,
                        R.layout.all_users_display_layout,
                        FriendsViewHolder.class,
                        friendsReference

                ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends_model model, int position) {
                viewHolder.date.setText(getResources().getString(R.string.friendsdate));
                viewHolder.dateis.setText(model.getDate());
                final String list_user_id=getRef(position).getKey();
                listuserid=list_user_id;
                Log.d("list_user_id", list_user_id+"");
                UserReference=UserReference.child(list_user_id).child("personal");
                UserReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("HasChild", dataSnapshot+"");
                        id=dataSnapshot.child("id").getValue().toString();
                            if(id.equals("broker"))
                            {
                                name = dataSnapshot.child("name").getValue().toString();
                                title="بروکر";
                            }
                            else if(id.equals("trader")) {
                                name = dataSnapshot.child("namee").getValue().toString();
                                title="آڑھتی";
                            }
                            else if(id.equals("farmer"))
                            {
                                name = dataSnapshot.child("namee").getValue().toString();
                                title="کسان";
                            }
                            else{
                                name = dataSnapshot.child("namee").getValue().toString();
                                title="کھاد فروش";
                            }
                            profile = dataSnapshot.child("profile").getValue().toString();
                            OnlineUser=FirebaseDatabase.getInstance().getReference("Users").child(list_user_id).child("Time").child("online");
                      OnlineUser.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                              String online_status=dataSnapshot.getValue().toString();

                              if(online_status.equals("true")) {
                                  Log.d("HasChild", "true");
                                  viewHolder.user_status.setVisibility(View.VISIBLE);
                              }
                              else if(online_status.equals("false"))
                              {
                                  Log.d("HasChild", "false");
                                  viewHolder.user_status.setVisibility(View.GONE);
                              }
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });

                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                                    Chat_Fragment chatfrag=new Chat_Fragment();
                                                    FragmentTransaction transaction;
                                                    FragmentManager manager=getFragmentManager();
                                                    transaction=manager.beginTransaction();
                                    if(user_id.equals("trader"))
                                    {
                                        transaction.replace(R.id.traderFrame,chatfrag);
                                    }
                                    else if(user_id.equals("farmer"))
                                    {
                                        transaction.replace(R.id.frame3,chatfrag);
                                    }
                                    else if(user_id.equals("broker"))
                                    {
                                        transaction.replace(R.id.brokerFrame,chatfrag);
                                    }
                                    else{
                                        transaction.replace(R.id.Fertilizer_Frame,chatfrag);
                                    }
                                    transaction.addToBackStack(null);
                                    transaction.commit();

                                                }
                            });
                        viewHolder.nameis.setText(name);
                        viewHolder.title.setText(title);
                        if(profile.equals("")) {

                            viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile1));

                        }
                        else
                        {
                            Picasso.with(getActivity())
                                    .load(profile)
                                    .placeholder(R.drawable.profile1)
                                    .into(viewHolder.imageView);
                            //    Log.d("profileImage", profileImage+"   asdfsd");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        friendsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        private TextView dateis,date,nameis,title;
        private CircleImageView imageView;
        private ImageView user_status;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            dateis=itemView.findViewById(R.id.product2);
            date=itemView.findViewById(R.id.product);
            nameis=itemView.findViewById(R.id.name2);
            title=itemView.findViewById(R.id.Title);
            user_status=itemView.findViewById(R.id.status);
            imageView=itemView.findViewById(R.id.personImage);
        }
    }

}
