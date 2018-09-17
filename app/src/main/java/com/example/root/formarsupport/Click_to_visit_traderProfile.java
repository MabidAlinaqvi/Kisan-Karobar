package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.root.formarsupport.MapsFragment.lati;
import static com.example.root.formarsupport.MapsFragment.longi;
import static com.example.root.formarsupport.card_view_adapter_farmer_firstpage.visitData;


/**
 * A simple {@link Fragment} subclass.
 */
public class Click_to_visit_traderProfile extends Fragment{
View view;
private TextView name,ntnnum,city,distt,thsil,loan,protype,area_price,quant,sack_price,commentNo,PersonName,comment;
private String nameis,ntnnumis,cityis,disttis,thsilis,loanis,protypeis,area_priceis,quantis,sack_priceis,uid,field;
private String user_id,current_user;
private RatingBar ratingBar;
private RelativeLayout croplayout,fruitlayout;
private CircleImageView imageView;
private Double latitude,longitude;
private ImageView chatBtn;
private Toolbar mToolbar;
FirebaseAuth mAuth;
FirebaseDatabase firebaseDatabase;
DatabaseReference fieldInfo,current_id_reference,FriendRequestReference,NotificationRefrence;
DatabaseReference personalInfo;
Click_to_visit_traderCrop_model trader_crop_model;
trader_personalInfo_model trader_personal;
Click_to_visit_traderFruit_model trader_fruit_model;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    static String toOpenComment,tradername;
    String server_url = "http://"+ LoginActivity.server_ip+"/firebase.php";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_click_to_visit_trader_profile, container, false);
        mToolbar=view.findViewById(R.id.trader_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.trader));
        trader_crop_model =new Click_to_visit_traderCrop_model();
        trader_personal=new trader_personalInfo_model();
        trader_fruit_model=new Click_to_visit_traderFruit_model();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        gettingIds();
        current_user=mAuth.getCurrentUser().getUid();
        FriendRequestReference=FirebaseDatabase.getInstance().getReference().child("friend_request");
        NotificationRefrence=FirebaseDatabase.getInstance().getReference().child("Notification");
        current_id_reference=FirebaseDatabase.getInstance().getReference("Users").child(current_user).child("personal").child("id");
        current_id_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //Log.d("SingleValue", dataSnapshot+"");
                user_id=(String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
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
                            if(dataSnapshot.hasChild(toOpenComment))
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
                                    transaction.replace(R.id.brokerFrame,chat_fragment);
                                }
                                else{
                                    transaction.replace(R.id.Fertilizer_Frame,chat_fragment);
                                }
                                transaction.commit();

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
                    //Toast.makeText(getActivity(), getResources().getString(R.string.sendRequest), Toast.LENGTH_SHORT).show();
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
                params.put("receive",toOpenComment);
                return params;
            }

        };
        Make_Request_Queue.getInstance(getActivity()).addtorequestque(request);
    }



    private void retrevingDataFromFirebase() {
        try {
            toOpenComment=visitData.getPhone();
            tradername=visitData.getName();
            uid = visitData.getPhone();
            ratingBar.setRating(Float.parseFloat(visitData.getRating()));
            commentNo.setText(visitData.getComment());
        }
        catch(Exception e){
            toOpenComment=bookmarked_adapter.visitdata.getPhone();
            uid=bookmarked_adapter.visitdata.getPhone();
            ratingBar.setRating(Float.parseFloat(bookmarked_adapter.visitdata.getRating()));
            commentNo.setText(bookmarked_adapter.visitdata.getComment());
        }
        personalInfo=firebaseDatabase.getReference("Users").child(uid).child("personal");
        personalInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trader_personal=dataSnapshot.getValue(trader_personalInfo_model.class);
                field=trader_personal.getField();
                if(field.equals("crop"))
                {
                    croplayout.setVisibility(View.VISIBLE);
                    cropfieldData();
                }
                else if(field.equals("fruit"))
                {
                    fruitlayout.setVisibility(View.VISIBLE);
                    fruitfieldData();
                    
                }
                PersonalValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fruitfieldData() {
        fieldInfo=firebaseDatabase.getReference("Users").child(uid).child(field);
        fieldInfo.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trader_fruit_model=dataSnapshot.getValue(Click_to_visit_traderFruit_model.class);
                FruitFieldValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void FruitFieldValues() {

        name.setText(trader_fruit_model.getPro_name());
        distt.setText(trader_fruit_model.getDistt());
        city.setText(trader_fruit_model.getCity());
        thsil.setText(trader_fruit_model.getTehsil());
        /*ratingBar.setRating(Float.parseFloat(visitData.getRating()));
        commentNo.setText(visitData.getComment());*/
        protype.setText(trader_fruit_model.getPro_type());
        area_price.setText(trader_fruit_model.getArea_price());
   //     if(!trader_fruit_model.getLatitude().equals("0.0") && !trader_fruit_model.getLongitude().equals("0.0")) {
            latitude = Double.parseDouble(trader_fruit_model.getLatitude());
            longitude = Double.parseDouble(trader_fruit_model.getLongitude());
            initializingMaps();
   //     }



    }


    private void cropfieldData() {
        fieldInfo=firebaseDatabase.getReference("Users").child(uid).child(field);
        fieldInfo.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("datasnapShotValue", dataSnapshot+"");
                trader_crop_model =dataSnapshot.getValue(Click_to_visit_traderCrop_model.class);
                CropFieldValues();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void PersonalValues() {
        PersonName.setText(trader_personal.getNamee());
        ntnnum.setText(trader_personal.getNtnNum());
        if(!trader_personal.getProfile().equals("")) {
            Picasso.with(getActivity())
                    .load(trader_personal.getProfile())
                    .placeholder(R.drawable.profile1)
                    .into(imageView);
        }
        /*Log.d("model_valueIs", trader_personal.getNtnNum());
        Log.d("model_valueIs", trader_personal.getId());
        Log.d("model_valueIs", trader_personal.getNamee());
        Log.d("model_valueIs", trader_personal.getPhonee());
        Log.d("model_valueIs", trader_personal.getProfile());
        Log.d("model_valueIs", trader_personal.getField());*/

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CropFieldValues(){
       //city,distt,thsil,loan,commentNo
        name.setText(trader_crop_model.getProduct_name());
        city.setText(trader_crop_model.getCity());
        distt.setText(trader_crop_model.getDISTT());
        thsil.setText(trader_crop_model.getTEHSIL());
       // protype.setText(trader_crop_model.getProduct_name());
        /*ratingBar.setRating(Float.parseFloat(visitData.getRating()));
        commentNo.setText(visitData.getComment());*/
        quant.setText(trader_crop_model.getQuantity());
        sack_price.setText(trader_crop_model.getSack_price());
//        if(!trader_crop_model.getLatitude().equals("0.0") && !trader_crop_model.getLongitude().equals("0.0")) {
        latitude=Double.parseDouble(trader_crop_model.getLatitude());
        longitude=Double.parseDouble(trader_crop_model.getLongitude());
            initializingMaps();
 //       }
    }

    private void gettingIds() {
        name=view.findViewById(R.id.name2);
        ntnnum=view.findViewById(R.id.ntn2);
        city=view.findViewById(R.id.city2);
        thsil=view.findViewById(R.id.Thsil2);
        distt=view.findViewById(R.id.District2);
        loan=view.findViewById(R.id.loan2);
        protype=view.findViewById(R.id.product_type2);
        area_price=view.findViewById(R.id.area_price2);
        quant=view.findViewById(R.id.quantity2);
        sack_price=view.findViewById(R.id.sack2);
        commentNo=view.findViewById(R.id.commentNo);
        ratingBar=view.findViewById(R.id.rateit);
        croplayout=view.findViewById(R.id.crop_layout);
        fruitlayout=view.findViewById(R.id.fruit_layout);
        imageView=view.findViewById(R.id.ProfileIs);
        PersonName=view.findViewById(R.id.personname2);
        comment=view.findViewById(R.id.comment);
        chatBtn=view.findViewById(R.id.tradermessage);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void CancelFriendRequest() {
        FriendRequestReference.child(current_user).child(toOpenComment).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FriendRequestReference.child(toOpenComment).child(current_user).removeValue()
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
                .child(toOpenComment)
                .child("request_type")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            FriendRequestReference
                                    .child(toOpenComment)
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
                                                        .child(toOpenComment)
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
