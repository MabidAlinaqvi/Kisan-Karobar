package com.example.root.formarsupport;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class receivedOrSentRequests extends Fragment {
    RecyclerView users_request_list;
    DatabaseReference request_Reference,user_Reference,FriendsDatabaseRef,RequestDatabaseRef;
    FirebaseAuth mAuth;
    String online_user;
    String profile,name,id,listuserId;
    private Toolbar mToolbar;
    View view;
    String server_url = "http://"+ LoginActivity.server_ip+"/firebase.php";
    String title,identi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for is fragment
        view=inflater.inflate(R.layout.fragment_received_or_sent_requests, container, false);
        mToolbar=view.findViewById(R.id.send_requests_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.Requests));
        mAuth=FirebaseAuth.getInstance();
        online_user=mAuth.getCurrentUser().getUid();
        request_Reference= FirebaseDatabase.getInstance().getReference().child("friend_request").child(online_user);
        user_Reference=FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Friends");
        RequestDatabaseRef=FirebaseDatabase.getInstance().getReference().child("friend_request");
        users_request_list=view.findViewById(R.id.request_list_fragment);
        users_request_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        users_request_list.setLayoutManager(linearLayoutManager);


        user_Reference.child(online_user).child("personal").child("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("NaqviID", dataSnapshot+"");
                identi=dataSnapshot.getValue().toString();
                Log.d("NaqviID", identi);
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
        FirebaseRecyclerAdapter<UserRequest,RequestViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<UserRequest, RequestViewHolder>
                (
                        UserRequest.class,
                        R.layout.request_all_user_layout,
                        receivedOrSentRequests.RequestViewHolder.class,
                        request_Reference
                )
        {

            @Override
            protected void populateViewHolder(final RequestViewHolder viewHolder, UserRequest model, final int position) {
                Log.d("RequestValue", model.getRequest_type());
                final String list_user_id=getRef(position).getKey();
                listuserId=list_user_id;
                DatabaseReference get_type_ref=getRef(position).child("request_type").getRef();
                get_type_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String request_type=dataSnapshot.getValue().toString();
                            if(request_type.equals("received"))
                            {
                                viewHolder.acceptBtn.setVisibility(View.VISIBLE);
                                user_Reference.child(list_user_id).child("personal").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        id=dataSnapshot.child("id").getValue().toString();
                                        if(id.equals("farmer"))
                                        {
                                            title="کسان";
                                            name = dataSnapshot.child("namee").getValue().toString();
                                        }
                                        else if(id.equals("trader"))
                                        {
                                            title="آرھتی";
                                            name = dataSnapshot.child("namee").getValue().toString();
                                        }
                                        else if(id.equals("broker"))
                                        {
                                            title="بروکر";
                                            name = dataSnapshot.child("name").getValue().toString();
                                        }
                                        else{
                                            title="ادویات فروش";
                                            name = dataSnapshot.child("namee").getValue().toString();

                                        }
                                       /* if(!id.equals("broker")) {
                                            name = dataSnapshot.child("namee").getValue().toString();
                                        }*/
                                        profile=dataSnapshot.child("profile").getValue().toString();
                                        viewHolder.ID.setText(title);
                                        viewHolder.NAME.setText(name);
                                        if(!profile.equals(""))
                                        {
                                            Picasso.with(getActivity())
                                                    .load(profile)
                                                    .placeholder(R.drawable.profile1)
                                                    .into(viewHolder.imageView);
                                        }
                                        else
                                        {
                                            viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
                                        }

                                        viewHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                listuserId=getRef(position).getKey();
                                                AcceptFrientRequest();
                                                sendData(getResources().getString(R.string.accepted),online_user);
                                            }
                                        });

                                        viewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                listuserId=getRef(position).getKey();
                                                CancelFriendRequest();
                                                sendData(getResources().getString(R.string.cancelled),online_user);
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else if(request_type.equals("sent"))
                            {
                                viewHolder.acceptBtn.setVisibility(View.INVISIBLE);
                                //  viewHolder.cancelBtn.setVisibility(View.INVISIBLE);

                                user_Reference.child(list_user_id).child("personal").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("LatestData", dataSnapshot+"");
                                        id=dataSnapshot.child("id").getValue().toString();
                                        if(id.equals("farmer"))
                                        {
                                            title="کسان";
                                            name = dataSnapshot.child("namee").getValue().toString();
                                        }
                                        else if(id.equals("trader"))
                                        {
                                            title="آرھتی";
                                            name = dataSnapshot.child("namee").getValue().toString();
                                        }
                                        else if(id.equals("broker"))
                                        {
                                            title="بروکر";
                                            name = dataSnapshot.child("name").getValue().toString();
                                        }
                                        else{
                                            title="ادویات فروش";
                                            name = dataSnapshot.child("namee").getValue().toString();

                                        }
                                      /*  if(!id.equals("broker")) {
                                            name = dataSnapshot.child("namee").getValue().toString();
                                        }*/
                                        profile=dataSnapshot.child("profile").getValue().toString();
                                        viewHolder.ID.setText(id);
                                        viewHolder.NAME.setText(name);
                                        if(!profile.equals(""))
                                        {
                                            Picasso.with(getActivity())
                                                    .load(profile)
                                                    .placeholder(R.drawable.profile1)
                                                    .into(viewHolder.imageView);
                                        }
                                        else
                                        {
                                            viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
                                        }
                                        viewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                listuserId=getRef(position).getKey();
                                                CancelFriendRequest();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /*profile=model.getProfile();
                    viewHolder.ID.setText(model.getId());
                    viewHolder.NAME.setText(model.getName());

*/


            }
        };
        users_request_list.setAdapter(firebaseRecyclerAdapter);
    }

    private void RefreshFragment() {
        receivedOrSentRequests receivedOr=new receivedOrSentRequests();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        if(identi.equals("trader"))
        {
            transaction.replace(R.id.traderFrame,receivedOr);
        }
        else if(identi.equals("farmer"))
        {
            transaction.replace(R.id.frame3,receivedOr);
        }
        else if(identi.equals("broker")) {
            transaction.replace(R.id.brokerFrame, receivedOr);
        }
        else{
            transaction.replace(R.id.Fertilizer_Frame,receivedOr);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        private CircleImageView imageView;
        private TextView ID,NAME;
        private Button acceptBtn,cancelBtn;
        public RequestViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
            imageView=itemView.findViewById(R.id.requestProfile);
            ID=itemView.findViewById(R.id.title_user);
            NAME=itemView.findViewById(R.id.name_user);
            acceptBtn=itemView.findViewById(R.id.acception);
            cancelBtn=itemView.findViewById(R.id.rejection);
        }

    }

    private void AcceptFrientRequest() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate=simpleDateFormat.format(calendar.getTime());
        FriendsDatabaseRef.child(online_user).child(listuserId).child("date").setValue(saveCurrentDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FriendsDatabaseRef.child(listuserId)
                                .child(online_user)
                                .child("date")
                                .setValue(saveCurrentDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        BeFriend();
                                    }
                                });
                    }
                });


    }

    private void BeFriend() {
        RequestDatabaseRef.child(online_user).child(listuserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        RequestDatabaseRef.child(listuserId).child(online_user).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                         /*   SendRequest.setEnabled(true);
                                            CURRENT_STATE="not_friends";
                                            SendRequest.setText("Un Friend");*/
                                            Toast.makeText(getActivity(),"friend request accepted successfully",Toast.LENGTH_SHORT).show();
                                            RefreshFragment();
                                        }
                                    }
                                });
                    }
                });
    }

    private void CancelFriendRequest() {
        RequestDatabaseRef.child(online_user).child(listuserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        RequestDatabaseRef.child(listuserId).child(online_user).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            RefreshFragment();
                                            Toast.makeText(getActivity(),"request rejected",Toast.LENGTH_SHORT).show();
                                           /* SendRequest.setEnabled(true);
                                            CURRENT_STATE="not_friends";
                                            SendRequest.setText("Send Request");*/
                                        }
                                    }
                                });
                    }
                });
    }


    private void sendData(final String selected, final String getuser) {
        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("out"))
                        {
                            Toast.makeText(getActivity(),"Out Of Stock",Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("selected", selected);
                params.put("user", getuser);
                params.put("receive",listuserId);
                return params;
            }

        };
        Make_Request_Queue.getInstance(getActivity()).addtorequestque(request);
    }

}
