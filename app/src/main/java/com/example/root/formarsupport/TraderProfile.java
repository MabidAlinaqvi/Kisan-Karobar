package com.example.root.formarsupport;


import android.*;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.MapFragmentAfterLogin.Refresh;
import static com.example.root.formarsupport.MapFragmentAfterLogin.lato;
import static com.example.root.formarsupport.MapFragmentAfterLogin.longo;
import static com.example.root.formarsupport.trader_add_crop_after_login.crop_latitu;
import static com.example.root.formarsupport.trader_add_crop_after_login.crop_longitu;
import static com.example.root.formarsupport.trader_add_fruit_after_login.fruit_latitu;
import static com.example.root.formarsupport.trader_add_fruit_after_login.fruit_longitu;


/**
 * A simple {@link Fragment} subclass.
 */
public class TraderProfile extends Fragment implements View.OnClickListener{
    private String nameis, phoneis, productName, productType, cityis, TEHILis, DISTTis, areaIs, sack_priceIs, quantityis, area_priceis, plantsis, harvestis, harvestfriutis;
    private EditText Product_name, Product_type, city, TEHIL, DISTT, area, sack_price, quantity, HarvestTime, area_price, plants, personname, phone_num, harvest;
    private String uids, picimageUrl, imageUrls, DataOf, fieldIs;
    private String profileImageUrl="";
    static String trader_field, trader_id,setcardImage,namePerson;
    private CircleImageView ProfileImage;
    private ImageButton Picture_with_cam;
    private Button saveData, From_Gallery;
    private ImageView EditProfile, changeImage;
    private Toolbar mToolbar;
    private StorageReference storageReference;
    static Double trader_latitude, trader_longitude;
    private TextView uploadpicture, fieldinfois;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference personaldatabase, fieldInfo, databaseReference, databaseReferenceCard;
    FirebaseAuth mAuth;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    trader_fruit_infor trader_fruit_model;
    trader_crop_infor trader_crop_model;
    trader_signUp_infor trader_personal;
    trader_fruit_infor trader_fruit_model2;
    trader_crop_infor trader_crop_model2;
    trader_signUp_infor trader_personal2;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Uri picphotoUri, picimageUri;
    Uri profilephotoUri, profileimageUri;
    GridView gridview;
    SharedPreferences preferences;
    private static final int PRO_GAL_IMAGE = 1;
    private static final int PRO_CAM_IMAGE = 2;
    private RelativeLayout farmercroplayout, farmerfruitlayout, fieldsLayout;
    private FloatingActionButton floatingButton;
    ProgressDialog progressDialog;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_trader_profile, container, false);
        mToolbar=view.findViewById(R.id.trader_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.farmerProfile);
        gettingIds();
        if (Refresh.equals("not_refresh")) {
            retrevingFShared();
        }
        if (lato != 0.0 && longo != 0.0) {
            Log.d("gettingCoordinate", lato + "   lato");
            trader_latitude = lato;
            trader_longitude = longo;
        } else if (crop_latitu != null) {
            Log.d("gettingCoordinate", crop_latitu + "   crop_latitu");
            trader_latitude = crop_latitu;
            trader_longitude = crop_longitu;
        } else if (fruit_latitu != null) {
            Log.d("gettingCoordinate", fruit_latitu + "   fruit_lati");
            trader_latitude = fruit_latitu;
            trader_longitude = fruit_longitu;
        }
        trader_personal = new trader_signUp_infor();
        trader_crop_model = new trader_crop_infor();
        trader_fruit_model = new trader_fruit_infor();
        trader_personal2 = new trader_signUp_infor();
        trader_crop_model2 = new trader_crop_infor();
        trader_fruit_model2 = new trader_fruit_infor();
        progressDialog = new ProgressDialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uids = mAuth.getCurrentUser().getUid();
        retrevingDataFromFirebase();
        EditProfile.setOnClickListener(this);
        changeImage.setOnClickListener(this);
        saveData.setOnClickListener(this);
        floatingButton.setOnClickListener(this);
            return view;
    }

    private void retrevingFShared() {
      //  Log.d("sharedNaqvi", "retrieving data from shared");
        preferences = getActivity().getSharedPreferences("traderProfile_crop", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("username", "");   //will return false if doesn't exist "user_name"
        //  Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {

            String usernameis = preferences.getString("username", "");
            String userphoneis = preferences.getString("userphone", "");
            String userproduct = preferences.getString("userproduct", "");
            String usertype = preferences.getString("usertype", "");
            String usercity = preferences.getString("usercity", "");
            String usertehsil = preferences.getString("usertehsil", "");
            String userdistric = preferences.getString("userdistric", "");
            String userquantity = preferences.getString("userquantity", "");
            String userlati = preferences.getString("userlati", "");
            String userlongi = preferences.getString("userlongi", "");
            if (userlati != null && userlongi != null) {
                if (trader_field.equals("crop")) {
                    String usersack = preferences.getString("usersack", "");
                    String userarea = preferences.getString("croparea", "");
                    trader_latitude = Double.parseDouble(userlati);
                    trader_longitude = Double.parseDouble(userlongi);
                    area.setText(userarea);
                    sack_price.setText(usersack);
                } else if (trader_field.equals("fruit")) {
                    String userfruitarea = preferences.getString("fruitarea", "");
                    String userplants = preferences.getString("userplants", "");
                    trader_latitude = Double.parseDouble(userlati);
                    trader_longitude = Double.parseDouble(userlongi);
                    area_price.setText(userfruitarea);
                    plants.setText(userplants);
                }

            }
            personname.setText(usernameis);
            phone_num.setText(userphoneis);
            Product_name.setText(userproduct);
            Product_type.setText(usertype);
            city.setText(usercity);
            TEHIL.setText(usertehsil);
            DISTT.setText(userdistric);
            quantity.setText(userquantity);
        } else {
            Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
    }
    private void retrevingDataFromFirebase() {
        personaldatabase = firebaseDatabase.getReference("Users").child(uids).child("personal");
        personaldatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trader_personal = dataSnapshot.getValue(trader_signUp_infor.class);
                trader_field = trader_personal.getField();
                Log.d("FieldValueIs", trader_field);
                trader_id = trader_personal.getId();
               // Log.d("UserIs", id);
                if (!trader_field.equals("")) {
                    fieldsLayout.setVisibility(View.VISIBLE);
                    floatingButton.setVisibility(View.GONE);
                    fieldinfois.setVisibility(View.GONE);
                    if (trader_field.equals("crop")) {
                        farmercroplayout.setVisibility(View.VISIBLE);
                        farmerfruitlayout.setVisibility(View.GONE);
                        fieldinfois.setVisibility(View.GONE);
                        Product_type.setVisibility(View.GONE);
                        cropfieldData();
                    } else if (trader_field.equals("fruit")) {
                        Log.d("ArrayListIs", uids + "    uids");
                        Log.d("ArrayListIs", trader_field + "    field");
                        farmerfruitlayout.setVisibility(View.VISIBLE);
                        farmercroplayout.setVisibility(View.GONE);
                        Product_type.setVisibility(View.VISIBLE);
                        plants.setVisibility(View.GONE);
                        fruitfieldData();
                    }
                }
                PersonalValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void PersonalValues() {
        personname.setText(trader_personal.getNamee());
        namePerson=trader_personal.getNamee();
        phone_num.setText(trader_personal.getPhonee());

        if (!trader_personal.getProfile().equals("")) {
            profileImageUrl=trader_personal.getProfile();
            setcardImage=trader_personal.getProfile();
            Picasso.with(getActivity())
                    .load(trader_personal.getProfile())
                    .placeholder(R.drawable.profile1)
                    .into(ProfileImage);
        } else {
            ProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
        }

        }

    private void fruitfieldData() {
        fieldInfo = firebaseDatabase.getReference("Users").child(uids).child(trader_field);
        fieldInfo.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trader_fruit_model = dataSnapshot.getValue(trader_fruit_infor.class);
                FruitFieldValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void FruitFieldValues() {

        if (Refresh.equals("refresh")) {
            Log.d("retrieveOrnot", "retreiving from database");
            Product_name.setText(trader_fruit_model.getPro_name());
            DISTT.setText(trader_fruit_model.getDistt());
            city.setText(trader_fruit_model.getCity());
            TEHIL.setText(trader_fruit_model.getTehsil());
            Product_type.setText(trader_fruit_model.getPro_type());
            area_price.setText(trader_fruit_model.getArea_price());
            if (lato == 0.0 && longo == 0.0) {
                trader_latitude = Double.parseDouble(trader_fruit_model.getLatitude());
                trader_longitude = Double.parseDouble(trader_fruit_model.getLongitude());
            }
        }
        initializingMaps();
    }

    private void cropfieldData() {
        fieldInfo = firebaseDatabase.getReference("Users").child(uids).child(trader_field);
        fieldInfo.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("datasnapShotValue", dataSnapshot+"");
                trader_crop_model = dataSnapshot.getValue(trader_crop_infor.class);
                CropFieldValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CropFieldValues() {
        if (Refresh.equals("refresh")) {
            Product_name.setText(trader_crop_model.getProduct_name());
        //    Product_type.setText(trader_crop_model.getProduct_type());
            city.setText(trader_crop_model.getCity());
            DISTT.setText(trader_crop_model.getDISTT());
            TEHIL.setText(trader_crop_model.getTEHSIL());
            quantity.setText(trader_crop_model.getQuantity());
            sack_price.setText(trader_crop_model.getSack_price());
           // area.setText(trader_crop_model.get);
            //harvest.setText(trader_crop_model.get);
            if (lato == 0.0 && longo == 0.0) {
                trader_latitude = Double.parseDouble(trader_crop_model.getLatitude());
                trader_longitude = Double.parseDouble(trader_crop_model.getLongitude());
            }
        }
        initializingMaps();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initializingMaps() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        /**
         * it is showing the google map
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         */
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.personmap);
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
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    return;
                } else {
                    Log.d("locationValue", trader_latitude + "   lati");
                    Log.d("locationValue", trader_longitude + "  logni");
                    LatLng selected = new LatLng(trader_latitude,trader_longitude);
                    if (marker != null) {
                        marker.remove();
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected, 17));
                    marker = mMap.addMarker(new MarkerOptions().position(selected).title("marked"));
                }
                mMap.setMyLocationEnabled(true);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MapFragmentAfterLogin mapsFragment = new MapFragmentAfterLogin();
                        FragmentTransaction transaction;
                        FragmentManager manager = getFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.traderFrame, mapsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInShared();
    }

    /**
     * saveing values in file
     * https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
     */

    private void saveInShared() {
        Log.d("sharedNaqvi", "saving data to shared");
        preferences = getActivity().getSharedPreferences("traderProfile_crop", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        gettingValues();
        nameis = personname.getText().toString();
        phoneis = phone_num.getText().toString();
        editor.putString("username", nameis);
        editor.putString("userphone", phoneis);
        editor.putString("userproduct", productName);
        editor.putString("usertype", productType);
        editor.putString("usercity", cityis);
        editor.putString("usertehsil", TEHILis);
        editor.putString("userdistric", DISTTis);
        editor.putString("croparea", areaIs);
        editor.putString("fruitarea", area_priceis);
        editor.putString("usersack", sack_priceIs);
        editor.putString("userquantity", quantityis);
        editor.putString("userlati", trader_latitude + "");
        editor.putString("userlongi", trader_longitude + "");
        editor.commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                }
                else {

                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void gettingValues() {

        productName=Product_name.getText().toString();
        productType=Product_type.getText().toString();
        TEHILis=TEHIL.getText().toString();
        DISTTis=DISTT.getText().toString();
        areaIs=area.getText().toString();
        sack_priceIs=sack_price.getText().toString();
        quantityis=quantity.getText().toString();
        cityis=city.getText().toString();
        area_priceis=area_price.getText().toString();
        harvestis=harvest.getText().toString();
        harvestfriutis=HarvestTime.getText().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == PRO_CAM_IMAGE || requestCode == PRO_GAL_IMAGE)) {

            if(requestCode==PRO_CAM_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap photo = (Bitmap) bundle.get("data");
                profilephotoUri = getImageUri(getActivity(), photo);
                Log.d("GalAndCam", profilephotoUri+"       from camera");
                Picasso.with(getActivity())
                        .load(profilephotoUri)
                        .resize(100, 100)
                        .centerCrop()
                        .into(ProfileImage);

            }

            else if (requestCode == PRO_GAL_IMAGE) {

                if (data.getData() != null) {

                    profileimageUri = data.getData();
                    Log.d("GalAndCam", profileimageUri+"    from gallery");
                    Picasso.with(getActivity())
                            .load(profileimageUri)
                            .resize(100, 100)
                            .centerCrop()
                            .into(ProfileImage);
                }
            }
        }

    }


    private void gettingIds() {

        Product_name=view.findViewById(R.id.personfarmername);
        Product_type=view.findViewById(R.id.personfarmerproduct_type);
        city=view.findViewById(R.id.personfarmercity);
        TEHIL=view.findViewById(R.id.personfarmerThsil);
        DISTT=view.findViewById(R.id.personfarmerDistrict);
        area=view.findViewById(R.id.personfarmerarea);
        area_price=view.findViewById(R.id.personfarmerarea_price);
        sack_price=view.findViewById(R.id.personfarmersack);
        quantity=view.findViewById(R.id.personfarmerquantity);
        HarvestTime=view.findViewById(R.id.personfarmerfruitharvest);
        harvest=view.findViewById(R.id.personfarmerharvest);
        plants=view.findViewById(R.id.personfarmerPlants);
        personname=view.findViewById(R.id.personfarmerpersonname);
        phone_num=view.findViewById(R.id.personfarmerphonenum);
        ProfileImage=view.findViewById(R.id.personfarmerProfileIs);
        EditProfile=view.findViewById(R.id.personfarmeredit);
        changeImage=view.findViewById(R.id.change_personprofile);
        farmercroplayout=view.findViewById(R.id.personfarmercrop_layout);
        farmerfruitlayout=view.findViewById(R.id.personfarmerfruit_layout);
        saveData=view.findViewById(R.id.personbutton2);
        uploadpicture=view.findViewById(R.id.uploadpic);
        gridview = view.findViewById(R.id.persongridview);
        From_Gallery=view.findViewById(R.id.personChose_gallery);
        Picture_with_cam=view.findViewById(R.id.persontake_picture);
        fieldsLayout=view.findViewById(R.id.fieldinfo);
        floatingButton=view.findViewById(R.id.floatBtn);
        fieldinfois=view.findViewById(R.id.floattext);

    }

    @Override
    public void onClick(View view) {

        if (view == EditProfile) {

            Product_name.setFocusableInTouchMode(true);
            Product_name.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

            Product_type.setFocusableInTouchMode(true);
            Product_type.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);


            city.setFocusableInTouchMode(true);
            city.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            TEHIL.setFocusableInTouchMode(true);
            TEHIL.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            DISTT.setFocusableInTouchMode(true);
            DISTT.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            area.setFocusableInTouchMode(true);
            area.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            sack_price.setFocusableInTouchMode(true);
            sack_price.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            quantity.setFocusableInTouchMode(true);
            quantity.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            HarvestTime.setFocusableInTouchMode(true);
            HarvestTime.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            area_price.setFocusableInTouchMode(true);
            area_price.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            plants.setFocusableInTouchMode(true);
            plants.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            personname.setFocusableInTouchMode(true);
            personname.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            harvest.setFocusableInTouchMode(true);
            harvest.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

            changeImage.setVisibility(View.VISIBLE);
            uploadpicture.setVisibility(View.VISIBLE);
        }
        else if (view == changeImage) {
            UploadImageFrom();
        }
        else if (view == saveData) {
            String name=trader_personal.getNamee();
            if(!TextUtils.isEmpty(name)) {
                progressDialog.setMessage("please wait");
                progressDialog.show();
                SendToFirebase();
            }
            else
            {
                Toast.makeText(getActivity(),getResources().getString(R.string.WriteName),Toast.LENGTH_LONG).show();
            }

        }
        else if(view==floatingButton)
        {
            showDialogueBox(view);
        }

    }

    private void SendToFirebase() {

        nameis=personname.getText().toString();
        phoneis=phone_num.getText().toString();
        databaseReference = firebaseDatabase.getReference("Users").child(uids);
        databaseReferenceCard=firebaseDatabase.getReference("cardview").child(uids);
        if (trader_field == null) {
            if (profileimageUri== null && profilephotoUri == null) {
                profileImageUrl = "";
                if(!trader_personal.getProfile().equals("")) {
                    /*String namee, String phonee, String ntnNum, String id,String profile,String field*/
                    trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id,trader_personal.getProfile(), trader_field);
                    cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", "", "", "0", "", "0", trader_personal.getProfile(), uids, "1");
                    databaseReference.child("personal").setValue(info);
                    databaseReferenceCard.setValue(cardinfo);
                }
                else
                {
                    trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, profileImageUrl, trader_field);
                    cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", "", "", "0", "", "0", profileImageUrl, uids, "1");
                    databaseReference.child("personal").setValue(info);
                    databaseReferenceCard.setValue(cardinfo);
                }
                progressDialog.dismiss();
                SignedIn();
            }
            else{
                if (profilephotoUri == null) {
                    UploadImage(profileimageUri);

                }
                if (profileimageUri == null) {
                    UploadImage(profilephotoUri);
                }
                if(!trader_personal.getProfile().equals("")) {
                    Log.d("CropOrFruit", "if of profile");
                    trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id,trader_personal.getProfile(), trader_field);
                    cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", "", "", "0", "", "0", trader_personal.getProfile(), uids, "1");
                    databaseReference.child("personal").setValue(info);
                    databaseReferenceCard.setValue(cardinfo);
                }
                else
                {
                    Log.d("CropOrFruit", "else of profile");
                    trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, profileImageUrl, trader_field);
                    cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", "", "", "0", "", "0", profileImageUrl, uids, "1");
                    databaseReference.child("personal").setValue(info);
                    databaseReferenceCard.setValue(cardinfo);
                }

            }
        }
        else if(trader_field.equals("crop"))
        {
            Log.d("CropOrFruit", "crop");
            DataOf=trader_field;
            fieldIs=trader_field;
            SaveDataToFirebase();
        }
        else {
            Log.d("CropOrFruit", "fruit");
         //   Log.d("ProfileImage", field+"    fieldis");
            DataOf=trader_field;
            fieldIs=trader_field;
            SaveDataToFirebase();
        }

    }

    private void SaveDataToFirebase() {

        if(DataOf.equals("crop")) {
            CropDataToFirebase();
            if(profileimageUri == null && profilephotoUri == null)
            {
                progressDialog.dismiss();
                Log.d("SignedInToFrag", "2");
                SignedIn();
            }
        }
        else{
            Log.d("ProfileImage", DataOf+"    savetofirebase");
            FruitDataToFirebase();
            if(profileimageUri == null && profilephotoUri == null)
            {
                progressDialog.dismiss();
                Log.d("SignedInToFrag", "SaveDataToFirebase: 1");
                SignedIn();
            }
        }
    }

    private void FruitDataToFirebase() {

        Log.d("ProfileImage", "fruitdatatofirebase");
        if (profileimageUri == null && profilephotoUri == null) {
            profileImageUrl = "";

            //String product_name, String product_type, String city, String TEHIL, String DISTT,
            //  String area_price, String plants, String harvest, String latitude, String longitude
            gettingValues();
            //productName,productType,cityis,TEHILis,DISTTis,areaIs,sack_priceIs,quantityis,area_priceis,plantsis
            /*String pro_name, String pro_type, String city,
                              String tehsil, String distt, String area_price, String latitude, String longitude*/
            trader_fruit_infor fruitinfo=new trader_fruit_infor(productName,productType,cityis,TEHILis,DISTTis,area_priceis,trader_latitude+"",trader_longitude+"");

            databaseReference.child(DataOf).setValue(fruitinfo);
            if(!trader_personal.getProfile().equals("")) {
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, trader_personal.getProfile(),trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", cityis, productName, "0", DISTTis, "0", trader_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else{
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, profileImageUrl,trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
        } else {
            if (profilephotoUri == null) {
                //Log.d("ProfileImage", profileimageUri+ "image uri is");
              //  Log.d("NaqviProfileImage",profileimageUri+"    3");
                UploadImage(profileimageUri);
            }
            if (profileimageUri == null) {
               // Log.d("NaqviProfileImage",profilephotoUri+"    4");
                UploadImage(profilephotoUri);
            }
//            Log.d("ProfileImages", profileImageUrl);
            Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
            trader_fruit_infor fruitinfo2=new trader_fruit_infor(productName,productType,cityis,TEHILis,DISTTis,area_priceis,trader_latitude+"",trader_longitude+"");
            databaseReference.child(DataOf).setValue(fruitinfo2);
            if(!trader_personal.getProfile().equals("")) {
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id,trader_personal.getProfile(), trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(productName, "آڑتی", cityis, productName, "0", DISTTis, "0", trader_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else
            {
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, profileImageUrl, trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
        }

    }

    private void CropDataToFirebase() {
        Log.d("ProfileImage", "cropdatatofirebase");
        if (profileimageUri == null && profilephotoUri == null) {
            trader_crop_infor cropinfo=new trader_crop_infor(productName,cityis,TEHILis,DISTTis,sack_priceIs,quantityis,trader_latitude+"",trader_longitude+"");
            databaseReference.child(DataOf).setValue(cropinfo);
            if(!trader_personal.getProfile().equals("")) {
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, trader_personal.getProfile(), trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", cityis, productName, "0", DISTTis, "0", trader_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else
            {
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, profileImageUrl, trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
        } else {
            if (profilephotoUri == null) {
                UploadImage(profileimageUri);
            }
            if (profileimageUri == null) {
                UploadImage(profilephotoUri);
            }
            Toast.makeText(getActivity(), getResources().getString(R.string.data_saving), Toast.LENGTH_SHORT).show();

            trader_crop_infor cropinfo2=new trader_crop_infor(productName,cityis,TEHILis,DISTTis,sack_priceIs,quantityis,trader_latitude+"",trader_longitude+"");
            databaseReference.child(DataOf).setValue(cropinfo2);
            if(!trader_personal.getProfile().equals("")) {
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, trader_personal.getProfile(), trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", cityis, productName, "0", DISTTis, "0", trader_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else
            {
                trader_signUp_infor info = new trader_signUp_infor(nameis, phoneis,"123456789", trader_id, profileImageUrl, trader_field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "آڑتی", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
        }
    }
ImageView cam,gal;
    private void UploadImageFrom() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.profileimageafterlogin,null);
        gal=view.findViewById(R.id.user_gallery);
        cam=view.findViewById(R.id.use_camera);
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "gal", Toast.LENGTH_SHORT).show();
                TakePhotoOrGallery("gallery");
               // alertDialog.dismiss();
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(), "cam", Toast.LENGTH_SHORT).show();
                TakePhotoOrGallery("camera");
               // alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog = builder.show();
    }
RadioButton radio_crop,radio_fruit;
    Button lockchoice;
    String Former_choice=null;
    private void showDialogueBox(View view) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view3=inflater.inflate(R.layout.farmer_choose_field_after_login,null);
        builder.setView(view3);
        radio_crop=view3.findViewById(R.id.R_cropinfo);
        radio_fruit=view3.findViewById(R.id.R_fruitinfo);
        lockchoice=view3.findViewById(R.id.farmermove);
        radio_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Former_choice="crop";
            }
        });
        radio_fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Former_choice="fruit";
            }
        });
        lockchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Former_choice==null)
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.atleastOne), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(Former_choice.equals("crop"))
                    {
                        Toast.makeText(getActivity(),"crop", Toast.LENGTH_LONG).show();
                        Add_crop_Product();
                        alertDialog.dismiss();
                    }
                    else{
                        Toast.makeText(getActivity(),"fruit", Toast.LENGTH_LONG).show();
                        Add_fruit_product();
                        alertDialog.dismiss();
                    }
                }
            }
        });
        alertDialog=builder.show();
    }


    private void Add_fruit_product() {
        trader_add_fruit_after_login farmerlogin=new trader_add_fruit_after_login();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.traderFrame,farmerlogin);
        transaction.commit();
    }

    private void Add_crop_Product() {
        trader_add_crop_after_login farmerlogin=new trader_add_crop_after_login();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.traderFrame,farmerlogin);
        transaction.commit();
    }

    private void TakePhotoOrGallery(String farmer_choice) {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(getActivity(),"no permission",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.MANAGE_DOCUMENTS},1);
            return;
        }
        if(farmer_choice.equals("camera"))
        {
            // ProfileImage.setImageDrawable(R.drawable.profile2);
            //ProfileImage.setImageURI();
            //   checkPermissionREAD_EXTERNAL_STORAGE(getActivity());
            OpenCamera();
            alertDialog.dismiss();
        }
        else{
            //     checkPermissionREAD_EXTERNAL_STORAGE(getActivity());
            OpenGallery();
            alertDialog.dismiss();
        }

    }

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.select_pic)), PRO_GAL_IMAGE);
    }

    private void OpenCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,PRO_CAM_IMAGE);
    }

    private void UploadImage(Uri imageUri) {

     //   Log.d("ProfileImages", " out of if");
     //   Log.d("ProfileImages", imageUri+" out of if");

        if(!imageUri.equals(""))
        {
          //  Log.d("NaqviProfileImage",imageUri+"    5");
       //     Log.d("ProfileImages", " in of if");
            storageReference= FirebaseStorage.getInstance().getReference().child("/images/"+System.currentTimeMillis()+"."+getImageExt(imageUri));
            storageReference.putFile(imageUri)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //    Log.d("NaqviProfileImage",profileImageUrl);
                            profileImageUrl=taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("personal").child("profile").setValue(profileImageUrl);
                            databaseReferenceCard.child("profile").setValue(profileImageUrl);
                                progressDialog.dismiss();
                                Log.d("SignedInToFrag", "SaveDataToFirebase: 5");
                                SignedIn();
                        }
                    });

        }
        else{
            //  Toast.makeText(getActivity(),"image uri doesn't found",Toast.LENGTH_SHORT).show();
        }
    }

    private void SignedIn() {
        Toast.makeText(getActivity(),getResources().getString(R.string.data_saving),Toast.LENGTH_LONG).show();
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, getResources().getString(R.string.imagePath), null);
        return Uri.parse(path);
    }


 /*   @Override
    public void SendData(String trader_choice) {



    }*/
}
