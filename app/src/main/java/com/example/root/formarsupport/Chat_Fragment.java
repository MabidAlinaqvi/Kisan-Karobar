package com.example.root.formarsupport;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.root.formarsupport.Click_to_visit_brokerProfile.brokername;
import static com.example.root.formarsupport.Click_to_visit_brokerProfile.toOpenCommentbroker;
import static com.example.root.formarsupport.Click_to_visit_farmerProfile.farmername;
import static com.example.root.formarsupport.Click_to_visit_farmerProfile.toOpenCommentFarmer;
import static com.example.root.formarsupport.Click_to_visit_traderProfile.toOpenComment;
import static com.example.root.formarsupport.Click_to_visit_traderProfile.tradername;
import static com.example.root.formarsupport.Friends.listuserid;
import static com.example.root.formarsupport.Friends.name;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_Fragment extends Fragment {
    private Toolbar chatToolbar;
    private TextView user_nameTitle,user_last_seen;
    private CircleImageView userImageProfile;
    private DatabaseReference rootReference,databaseReference,rootRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private View view;
    private ImageButton sendBtn,CameraBtn;
    private EditText writeMessage;
    private RecyclerView user_message_list;
    private final List<Messages> messageslist=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private StorageReference storageReference;
    private String imageUrl,message;
    private ProgressDialog loadingBar;
    private String user_id,user_name,sms_profile;
    String server_url = "http://"+ LoginActivity.server_ip+"/firebase.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_chat_, container, false);
        chatToolbar=view.findViewById(R.id.chat_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(chatToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.friends));
        loadingBar=new ProgressDialog(getActivity());
        rootReference= FirebaseDatabase.getInstance().getReference("Users");
        //user_id=getIntent().getExtras().get("visit_id").toString();
        if(toOpenComment==null && toOpenCommentbroker==null && toOpenCommentFarmer==null)
        {
            user_id=listuserid;
            user_name=name;
            Log.d("UsersProfile", user_id+"        "+user_name);
        }
       else if(toOpenComment==null && toOpenCommentbroker==null) {
            user_id=toOpenCommentFarmer;
            user_name=farmername;
        }
        else if(toOpenCommentFarmer==null && toOpenCommentbroker==null) {
            user_id = toOpenComment;
            user_name=tradername;
        }
        else if(toOpenComment==null && toOpenCommentFarmer==null)
        {
            user_id=toOpenCommentbroker;
            user_name=brokername;
        }
        sendBtn=view.findViewById(R.id.send_message);
        writeMessage=view.findViewById(R.id.input_message);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater=(LayoutInflater)((AppCompatActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(action_bar_view);
        user_nameTitle=view.findViewById(R.id.user_namesss);
        user_last_seen=view.findViewById(R.id.last_seen);
        userImageProfile=view.findViewById(R.id.user_profile);
        //messageAdapter=new MessageAdapter(messageslist);
        user_message_list=view.findViewById(R.id.messageList);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        user_message_list.setHasFixedSize(true);
        user_message_list.setLayoutManager(linearLayoutManager);
        //  user_message_list.setAdapter(messageAdapter);
        rootRef=FirebaseDatabase.getInstance().getReference();
        FetchMessages();
        user_nameTitle.setText(user_name);
        rootReference.child(user_id).child("personal").child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               try {
                   final String profileImage = dataSnapshot.child("profile").getValue().toString();
                   if (!profileImage.equals("")) {
                       Picasso.with(getActivity())
                               .load(profileImage)
                               .placeholder(R.drawable.profile1)
                               .into(userImageProfile);
                   } else {
                       userImageProfile.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
                   }
               }
               catch (Exception e)
               {
                   userImageProfile.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rootReference.child(user_id).child("Time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onlineSatatus", dataSnapshot+"");
                final String time_Stamp=dataSnapshot.child("timeStamp").getValue().toString();
                final String online=dataSnapshot.child("online").getValue().toString();


                if(online.equals("true"))
                {
                    user_last_seen.setText("online");
                }
                else
                {
                    Log.d("TimStampIs", time_Stamp);
                    UserLatSeenTime getTime=new UserLatSeenTime();
                    long last_seen=Long.parseLong(time_Stamp);
                    try {
                        String lastSeenDisplayTime = getTime.getTimeAgo(last_seen, ((AppCompatActivity) getActivity()).getApplicationContext()).toString();
                        user_last_seen.setText(lastSeenDisplayTime);
                    }
                    catch (Exception e)
                    {

                    }

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                sendData(message, currentUser.getUid());

            }
        });

        return view;
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
                params.put("receive",user_id);
                return params;
            }

        };
        Make_Request_Queue.getInstance(getActivity()).addtorequestque(request);
    }



    private void FetchMessages() {
        String sender=currentUser.getUid();
        rootReference.child(sender).child("personal").child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    sms_profile=dataSnapshot.getValue().toString();
                }
                else
                {
                    sms_profile="";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        rootRef.child("Messages").child(sender).child(user_id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //  Log.d("MessagesData", dataSnapshot+"");
                        Messages messages=dataSnapshot.getValue(Messages.class);
                        //  Log.d("MessagesDatas", messages.getMessages());
                        messageslist.add(messages);
                        messageAdapter=new MessageAdapter(messageslist,getActivity());
                        user_message_list.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();
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
    }


    private void sendMessage() {
        message=writeMessage.getText().toString();
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(getActivity(),"please write message",Toast.LENGTH_LONG).show();
        }
        else
        {
            String MessageSenderId=currentUser.getUid();
            String message_sender_ref="Messages/"+MessageSenderId+"/"+user_id;
            String message_receiver_ref="Messages/"+user_id+"/"+MessageSenderId;

            DatabaseReference user_message_key=FirebaseDatabase.getInstance().getReference().child("Messages").child(MessageSenderId).child(user_id).push();
            String message_push_id=user_message_key.getKey();
            Map messageTextBody=new HashMap();
            messageTextBody.put("messages",message);
            messageTextBody.put("seen",false);
            messageTextBody.put("type","text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from",MessageSenderId);
            messageTextBody.put("profile",sms_profile);

            Map messageBodyDetails=new HashMap();
            messageBodyDetails.put(message_sender_ref+ "/" +message_push_id,messageTextBody);
            messageBodyDetails.put(message_receiver_ref+ "/" +message_push_id,messageTextBody);

            rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!=null)
                    {
                        Log.d("Chat_Log", databaseError.getMessage().toString());

                    }
                    writeMessage.setText("");
                    loadingBar.dismiss();
                }
            });
        }
    }

}
