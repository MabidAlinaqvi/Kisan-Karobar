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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.root.formarsupport.card_view_adapter_farmer_firstpage.visitData;


/**
 * A simple {@link Fragment} subclass.
 */
public class Click_to_visit_fertilizerProfile extends Fragment {
    private TextView comment,commentNo,pest_name,PersonName,pro_name,city,thsil,distt,medic_name,userPerAkr,medic_quanti,way_to_use,regisNo;
    private String uids,field;
    private RatingBar ratingBar;
    private CircleImageView imageView;
    private ImageView Send_message;
    private Double latitude,longitude;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    private Toolbar mToolbar;
    static String toOpenCommentFertilizer,fertilizername;
    private String current_user,user_id;
    DatabaseReference personaldatabase,fieldInfo,current_id_reference,FriendRequestReference,NotificationRefrence;
    fertilizer_pestcide_model fertlizer_medic_model;
    fertilizer_signUp_infor fertilizer_personal;
    imagesPagerAdapter pagerAdapter;
    ViewPager viewPager;
    ArrayList<String> imagesUrl;
    String server_url = "http://"+ LoginActivity.server_ip+"/firebase.php";
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_click_to_visit_fertilizer_profile, container, false);
        mToolbar=view.findViewById(R.id.fertilizer_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ادویات فروش");
        fertilizer_personal=new fertilizer_signUp_infor();
        fertlizer_medic_model =new fertilizer_pestcide_model();
        imagesUrl=new ArrayList<>();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();
        FriendRequestReference=FirebaseDatabase.getInstance().getReference().child("friend_request");
        NotificationRefrence=FirebaseDatabase.getInstance().getReference().child("Notification");
        gettingIds();
        current_user=mAuth.getCurrentUser().getUid();
        current_id_reference=FirebaseDatabase.getInstance().getReference("Users").child(current_user).child("personal").child("id");
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
                    Toast.makeText(getActivity(),"broker",Toast.LENGTH_SHORT).show();
                    transaction.replace(R.id.brokerFrame,commentlist);
                }
                else if(user_id.equals("fertilizer"))
                {
                    transaction.replace(R.id.Fertilizer_Frame,commentlist);
                }
                else {
                    transaction.replace(R.id.frame3,commentlist);
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        retrevingDataFromFirebase();
        Send_message.setOnClickListener(new View.OnClickListener() {
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
                            if(dataSnapshot.hasChild(toOpenCommentFertilizer))
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
                                Toast.makeText(getActivity(), "friend", Toast.LENGTH_SHORT).show();
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

    private void CancelFriendRequest() {

        FriendRequestReference.child(current_user).child(toOpenCommentFertilizer).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FriendRequestReference.child(toOpenCommentFertilizer).child(current_user).removeValue()
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

    private void sendData(final String message, final String current_user) {

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
                params.put("selected", message);
                params.put("user", current_user);
                params.put("receive",toOpenCommentFertilizer);
                return params;
            }

        };
        Make_Request_Queue.getInstance(getActivity()).addtorequestque(request);

    }

    private void SendRequestToFriend() {

        FriendRequestReference
                .child(current_user)
                .child(toOpenCommentFertilizer)
                .child("request_type")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            FriendRequestReference
                                    .child(toOpenCommentFertilizer)
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
                                                        .child(toOpenCommentFertilizer)
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

    private void retrevingDataFromFirebase() {
        try {
            toOpenCommentFertilizer=visitData.getPhone();
            fertilizername=visitData.getName();
            uids = visitData.getPhone();
            ratingBar.setRating(Float.parseFloat(visitData.getRating()));
            commentNo.setText(visitData.getComment());
        }
        catch(Exception e){
            toOpenCommentFertilizer=bookmarked_adapter.visitdata.getPhone();
            uids=bookmarked_adapter.visitdata.getPhone();
            ratingBar.setRating(Float.parseFloat(bookmarked_adapter.visitdata.getRating()));
            commentNo.setText(bookmarked_adapter.visitdata.getComment());
        }
        personaldatabase=firebaseDatabase.getReference("Users").child(uids).child("personal");
        cropfieldData();
        RetrievingImages();
        PersonalValues();
    }

    private void PersonalValues() {
personaldatabase.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        PersonName.setText(dataSnapshot.child("namee").getValue().toString());
        regisNo.setText(dataSnapshot.child("ntnNum").getValue().toString());
//        Log.d("PutChecked", fertilizer_personal.getProfile());
      /*  if(!fertilizer_personal.getProfile().equals("")) {
            Picasso.with(getActivity())
                    .load(fertilizer_personal.getProfile())
                    .placeholder(R.drawable.profile1)
                    .into(imageView);
        }*/
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

    }

    private void RetrievingImages() {
        DatabaseReference imagesReference=firebaseDatabase.getReference("Users").child(uids).child("images");
        imagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagesUrl=(ArrayList<String>) dataSnapshot.getValue();
                //   Log.d("ArrayListIs", imagesUrl+"");
                if(imagesUrl!=null) {
                    pagerAdapter = new imagesPagerAdapter(getActivity(), imagesUrl);
                    viewPager.setAdapter(pagerAdapter);
                }
                else
                {
                    viewPager.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//fertilizer
    private void cropfieldData() {
        fieldInfo=firebaseDatabase.getReference("Users").child(uids).child("fertilizer");
        fieldInfo.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("datasnapShotValue", dataSnapshot+"");
                fertlizer_medic_model =dataSnapshot.getValue(fertilizer_pestcide_model.class);
                CropFieldValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CropFieldValues() {

        pro_name.setText(fertlizer_medic_model.getProduct_name());
        city.setText(fertlizer_medic_model.getCity());
        distt.setText(fertlizer_medic_model.getDISTT());
        thsil.setText(fertlizer_medic_model.getTEHIL());
        // protype.setText(fertlizer_medic_model.getProduct_name());
        /*ratingBar.setRating(Float.parseFloat(visitData.getRating()));
        commentNo.setText(visitData.getComment());*/
        medic_quanti.setText(fertlizer_medic_model.getQuantity());
        medic_name.setText(fertlizer_medic_model.getMedic_name());
        way_to_use.setText(fertlizer_medic_model.getUsage());
        userPerAkr.setText(fertlizer_medic_model.getRateUse());
//        if(!fertlizer_medic_model.getLatitude().equals("0.0") && !fertlizer_medic_model.getLongitude().equals("0.0")) {
        latitude=Double.parseDouble(fertlizer_medic_model.getLati());
        longitude=Double.parseDouble(fertlizer_medic_model.getLongi());
        initializingMaps();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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

        pest_name=view.findViewById(R.id.Pest_name2);
        medic_name=view.findViewById(R.id.Medic_name2);
        medic_quanti=view.findViewById(R.id.medic_quant2);
        userPerAkr=view.findViewById(R.id.useRate2);
        way_to_use=view.findViewById(R.id.User_meth2);
        regisNo=view.findViewById(R.id.registration2);

        imageView=view.findViewById(R.id.fertiProfileIs);
        comment=view.findViewById(R.id.farmercomment);
        viewPager=view.findViewById(R.id.viewPageris);
        Send_message=view.findViewById(R.id.fertimessage);
        ratingBar=view.findViewById(R.id.farmerrateit);
        commentNo=view.findViewById(R.id.farmercommentNo);
        city=view.findViewById(R.id.farmercity2);
        thsil=view.findViewById(R.id.farmerThsil2);
        distt=view.findViewById(R.id.farmerDistrict2);
        pro_name=view.findViewById(R.id.farmername2);
        PersonName=view.findViewById(R.id.farmerpersonname2);
    }

}
