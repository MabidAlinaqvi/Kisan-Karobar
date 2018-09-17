package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.root.formarsupport.card_view_adapter_farmer_firstpage.visitData;


/**
 * A simple {@link Fragment} subclass.
 */
public class Click_to_visit_brokerProfile extends Fragment {
    private TextView name,NTN,product_name,city,TEHSIL,DISTT,product_quant,comment,commentNois;
    private String uids,field,current_user,user_id;
    private RatingBar ratingBar;
    private ImageView chatBtn;
    private CircleImageView imageView;
    private Double latitude,longitude;
    private Toolbar mtoolbar;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    static String toOpenCommentbroker,brokername;
    DatabaseReference personaldatabase,current_id_reference,FriendRequestReference,NotificationRefrence;
    broker_infor brokerinfo;
    View view;
    String server_url = "http://"+ LoginActivity.server_ip+"/firebase.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     view=inflater.inflate(R.layout.fragment_click_to_visit_broker_profile, container, false);
        mtoolbar=view.findViewById(R.id.broker_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.broker));
     brokerinfo=new broker_infor();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();
        gettingIds();
        current_user=mAuth.getCurrentUser().getUid();
        current_id_reference=FirebaseDatabase.getInstance().getReference("Users").child(current_user).child("personal").child("id");
        FriendRequestReference=FirebaseDatabase.getInstance().getReference().child("friend_request");
        NotificationRefrence=FirebaseDatabase.getInstance().getReference().child("Notification");
        current_id_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("SingleValue", dataSnapshot+"");
                user_id=(String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentList commentlist=new commentList();
                FragmentTransaction transaction;
                FragmentManager manager=getFragmentManager();
                transaction=manager.beginTransaction();
                if(user_id.equals("trader"))
                {
                    transaction.replace(R.id.traderFrame,commentlist);
                }
                else if(user_id.equals("broker"))
                {
                    transaction.replace(R.id.brokerFrame,commentlist);
                }
                else if(user_id.equals("fertilizer"))
                {
                    transaction.replace(R.id.Fertilizer_Frame,commentlist);
                }
                else{
                    transaction.replace(R.id.frame3,commentlist);
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        retrevingDataFromFirebase();

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                checkFriendOrNot();
            }
        });

        return view;
    }


    private void checkFriendOrNot() {
        final DatabaseReference friendReference=FirebaseDatabase.getInstance().getReference("Friends");
        friendReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    friendReference.child(current_user).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(toOpenCommentbroker))
                            {
                                Chat_Fragment chat_fragment=new Chat_Fragment();
                                FragmentTransaction transaction;
                                FragmentManager manager=getFragmentManager();
                                transaction=manager.beginTransaction();
                                if(user_id.equals("trader"))
                                {
                                    transaction.replace(R.id.traderFrame,chat_fragment);
                                }
                                else if(user_id.equals("farmer"))
                                {
                                    transaction.replace(R.id.frame3,chat_fragment);
                                }
                                else if(user_id.equals("broker"))
                                {
                                    Toast.makeText(getActivity(), "broker", Toast.LENGTH_SHORT).show();
                                    transaction.replace(R.id.brokerFrame,chat_fragment);
                                }
                                else{
                                    transaction.replace(R.id.Fertilizer_Frame,chat_fragment);
                                }
                                transaction.commit();

                                //chatFragment
                               // Toast.makeText(getActivity(), "we are friend buddy", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                showDialogue();
                                Toast.makeText(getActivity(),"not friend",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(getActivity(), "exist", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showDialogue();
                    Toast.makeText(getActivity(), "not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void showDialogue() {

        CharSequence options[]=new CharSequence[]
                {
                        getResources().getString(R.string.sendRequest),
                        getResources().getString(R.string.cancelRequest)
                };
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.friendRequestTitle));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0)
                {
                    String message=getResources().getString(R.string.friendRequest);
                    SendRequestToFriend();
                    sendData(message, current_user);
                  //  Toast.makeText(getActivity(), getResources().getString(R.string.sendRequest), Toast.LENGTH_SHORT).show();
                }
                if(i==1)
                {
                    CancelFriendRequest();
                    Toast.makeText(getActivity(), getResources().getString(R.string.cancelRequest), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();

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
                params.put("receive",toOpenCommentbroker);
                return params;
            }

        };
        Make_Request_Queue.getInstance(getActivity()).addtorequestque(request);
    }


    private void retrevingDataFromFirebase() {
        try {
            Log.d("toOpenCommentbroker", visitData.getComment());
            toOpenCommentbroker=visitData.getPhone();
            brokername=visitData.getName();
        //    Log.d("toOpenCommentbroker", toOpenCommentbroker);
            uids = visitData.getPhone();
        }
        catch(Exception e){
          //  Log.d(toOpenCommentbroker, e+"");
            toOpenCommentbroker=bookmarked_adapter.visitdata.getPhone();
            uids=bookmarked_adapter.visitdata.getPhone();
            ratingBar.setRating(Float.parseFloat(bookmarked_adapter.visitdata.getRating()));
           // commentNo.setText(bookmarked_adapter.visitdata.getComment());
        }
        personaldatabase=firebaseDatabase.getReference("Users").child(uids).child("personal");
        personaldatabase.addValueEventListener(new ValueEventListener() {
            @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                brokerinfo=dataSnapshot.getValue(broker_infor.class);
                personalValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void personalValues() {
        //name,NTN,phone_num,pass,product_name,city,TEHSIL,DISTT,product_quant,comment,commentNo
        name.setText(brokerinfo.getName());
        NTN.setText(brokerinfo.getNTN());
        product_name.setText(brokerinfo.getProduct_name());
        city.setText(brokerinfo.getCity());
        TEHSIL.setText(brokerinfo.getTEHSIL());
        product_quant.setText(brokerinfo.getProduct_quant());
        latitude=Double.parseDouble(brokerinfo.getLatitude());
        longitude=Double.parseDouble(brokerinfo.getLongitude());
//        ratingBar.setRating(Float.parseFloat(visitData.getRating()));
        commentNois.setText(visitData.getComment());
        initializingMaps();
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initializingMaps() {

        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        /**
         * it is showing the google map
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         */
        mapFragment= (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //    Log.d(TAG, mapFragment+"");
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(getActivity(),new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    return;
                }
               /* else if(trader_crop_model.getLatitude()==null && trader_crop_model.getLongitude() == null) {
                    //Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                 *//*   Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude=location.getLatitude();
                        longitude=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }*//*
                }*/
                else {
                    LatLng selected = new LatLng(latitude, longitude);
                    if (marker != null) {
                        marker.remove();
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected, 17));
                    marker = mMap.addMarker(new MarkerOptions().position(selected).title("marked"));
                }
                mMap.setMyLocationEnabled(true);

                /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MapsFragment mapsFragment=new MapsFragment();
                        FragmentTransaction transaction;
                        FragmentManager manager=getFragmentManager();
                        transaction=manager.beginTransaction();
                        transaction.replace(R.id.frame,mapsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });*/
            }

        });

    }

    private void gettingIds() {

        name=view.findViewById(R.id.brokerpersonname2);
        NTN=view.findViewById(R.id.brokerntn2);
        product_name=view.findViewById(R.id.brokername2);
        city=view.findViewById(R.id.brokercity2);
        TEHSIL=view.findViewById(R.id.brokerThsil2);
        DISTT=view.findViewById(R.id.brokerDistrict2);
        product_quant=view.findViewById(R.id.brokerquantity2);
        imageView=view.findViewById(R.id.brokerProfileIs);
        comment=view.findViewById(R.id.brokercomment);
        commentNois=view.findViewById(R.id.brokercommentNo);
        ratingBar=view.findViewById(R.id.brokerrateit);
        chatBtn=view.findViewById(R.id.brokermessage);


    }


    private void CancelFriendRequest() {
        FriendRequestReference.child(current_user).child(toOpenCommentbroker).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FriendRequestReference.child(toOpenCommentbroker).child(current_user).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getActivity(), "Successfully Cancelled", Toast.LENGTH_SHORT).show();
                                           /* SendRequest.setEnabled(true);
                                            CURRENT_STATE="not_friends";
                                            SendRequest.setText("Send Request");*/
                                        }
                                    }
                                });
                    }
                });
    }

    private void SendRequestToFriend() {
        FriendRequestReference
                .child(current_user)
                .child(toOpenCommentbroker)
                .child("request_type")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            FriendRequestReference
                                    .child(toOpenCommentbroker)
                                    .child(current_user)
                                    .child("request_type")
                                    .setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getActivity(),getResources().getString(R.string.succesfulSent),Toast.LENGTH_LONG).show();
                                                HashMap<String, String> notificationData=new HashMap<String, String>();
                                                notificationData.put("from",current_user);
                                                notificationData.put("type","request");
                                                NotificationRefrence
                                                        .child(toOpenCommentbroker)
                                                        .push()
                                                        .setValue(notificationData)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //   Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                                               /* SendRequest.setEnabled(true);
                                                                CURRENT_STATE="request_send";
                                                                SendRequest.setText("Cancel Request");*/
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }



}
