package com.example.root.formarsupport;


import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.MapFragmentAfterLogin.Refresh;
import static com.example.root.formarsupport.MapFragmentAfterLogin.lato;
import static com.example.root.formarsupport.MapFragmentAfterLogin.longo;
import static com.example.root.formarsupport.farmer_add_crop_after_login.crop_lati;
import static com.example.root.formarsupport.farmer_add_crop_after_login.crop_longi;
import static com.example.root.formarsupport.farmer_add_fruit_after_login.fruit_lati;
import static com.example.root.formarsupport.farmer_add_fruit_after_login.fruit_longi;


/**
 * A simple {@link Fragment} subclass.
 */
public class FarmerProfile extends Fragment implements View.OnClickListener,FarmerChoiceDone {
    private String nameis, phoneis, productName, productType, cityis, TEHILis, DISTTis, areaIs, sack_priceIs, quantityis, area_priceis, plantsis, harvestis, harvestfriutis;
    private EditText Product_name, Product_type, city, TEHIL, DISTT, area, sack_price, quantity, HarvestTime, area_price, plants, personname, phone_num, harvest;
    private String uids, picimageUrl, imageUrls, DataOf, fieldIs;
    private String profileImageUrl="";
    static String field, id,setcardImage,namePerson;
    private CircleImageView ProfileImage;
    private ImageButton Picture_with_cam;
    private Button saveData, From_Gallery;
    private ImageView EditProfile, changeImage;
    private StorageReference storageReference;
    static Double latitude, longitude;
    double latitu, longitu;
    private Toolbar mToolbar;
    private TextView uploadpicture, fieldinfois;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference personaldatabase, fieldInfo, databaseReference, databaseReferenceCard;
    FirebaseAuth mAuth;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    farmer_fruit_infor farmer_fruit_model;
    farmer_crop_infor farmer_crop_model;
    Farmer_signup_infor farmer_personal;
    farmer_fruit_infor farmer_fruit_model2;
    farmer_crop_infor farmer_crop_model2;
    Farmer_signup_infor farmer_personal2;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Uri picphotoUri, picimageUri;
    Uri profilephotoUri, profileimageUri;
    GridView gridview;
    SharedPreferences preferences;
    static ArrayList<Uri> mArrayfarmerUri;
    private static final int PRO_GAL_IMAGE = 1;
    private static final int PRO_CAM_IMAGE = 2;
    private static final int UPLOAD_FROM_GAL = 3;
    private static final int UPLOAD_FROM_CAM = 4;
    private RelativeLayout farmercroplayout, farmerfruitlayout, fieldsLayout;
    private ArrayList<String> imagesUrl;
    private ArrayList<String> SaveImages;
    private FloatingActionButton floatingButton;
    ProgressDialog progressDialog;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_farmer_profile, container, false);
        mToolbar=view.findViewById(R.id.farmer_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.farmerProfile));
        gettingIds();
        //      Log.d("retrieveOrnot", retrieve);
        if (Refresh.equals("not_refresh")) {
            retrevingFShared();
        }
        if (lato != 0.0 && longo != 0.0) {
            Log.d("gettingCoordinate", lato + "   lato");
            latitude = lato;
            longitude = longo;
        } else if (crop_lati != null) {
            Log.d("gettingCoordinate", crop_lati + "   crop_lati");
            latitude = crop_lati;
            longitude = crop_longi;
        } else if (fruit_lati != null) {
            Log.d("gettingCoordinate", fruit_lati + "   fruit_lati");
            latitude = fruit_lati;
            longitude = fruit_longi;
        }
        farmer_personal = new Farmer_signup_infor();
        farmer_crop_model = new farmer_crop_infor();
        farmer_fruit_model = new farmer_fruit_infor();
        farmer_personal2 = new Farmer_signup_infor();
        farmer_crop_model2 = new farmer_crop_infor();
        farmer_fruit_model2 = new farmer_fruit_infor();
        progressDialog = new ProgressDialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
        imagesUrl = new ArrayList<>();
        SaveImages = new ArrayList<>();
        mArrayfarmerUri = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uids = mAuth.getCurrentUser().getUid();
        Log.d("UserIsWas", Refresh);
        Log.d("UserIs", "into if statement");
        retrevingDataFromFirebase();
        EditProfile.setOnClickListener(this);
        changeImage.setOnClickListener(this);
        Picture_with_cam.setOnClickListener(this);
        From_Gallery.setOnClickListener(this);
        saveData.setOnClickListener(this);
        floatingButton.setOnClickListener(this);
        return view;
    }

    private void retrevingFShared() {
        Log.d("sharedNaqvi", "retrieving data from shared");
        preferences = getActivity().getSharedPreferences("farmerProfile_crop", Context.MODE_PRIVATE);
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
                if (field.equals("crop")) {
                    String usersack = preferences.getString("usersack", "");
                    String userarea = preferences.getString("croparea", "");
                    latitude = Double.parseDouble(userlati);
                    longitude = Double.parseDouble(userlongi);
                    area.setText(userarea);
                    sack_price.setText(usersack);
                } else if (field.equals("fruit")) {
                    String userfruitarea = preferences.getString("fruitarea", "");
                    String userplants = preferences.getString("userplants", "");
                    latitude = Double.parseDouble(userlati);
                    longitude = Double.parseDouble(userlongi);
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
                farmer_personal = dataSnapshot.getValue(Farmer_signup_infor.class);
                field = farmer_personal.getField();
                id = farmer_personal.getId();
                Log.d("UserIs", id);
                if (!field.equals("")) {
                    fieldsLayout.setVisibility(View.VISIBLE);
                    floatingButton.setVisibility(View.GONE);
                    fieldinfois.setVisibility(View.GONE);
                    if (field.equals("crop")) {
                        farmercroplayout.setVisibility(View.VISIBLE);
                        farmerfruitlayout.setVisibility(View.GONE);
                        fieldinfois.setVisibility(View.GONE);
                        cropfieldData();
                  //      RetrievingImages();
                    } else if (field.equals("fruit")) {
                        Log.d("ArrayListIs", uids + "    uids");
                        Log.d("ArrayListIs", field + "    field");
                        farmerfruitlayout.setVisibility(View.VISIBLE);
                        farmercroplayout.setVisibility(View.GONE);
                        fruitfieldData();
                 //       RetrievingImages();
                    }
                }
                PersonalValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void RetrievingImages() {
        DatabaseReference imagesReference = firebaseDatabase.getReference("Users").child(uids).child("imagesUri");
        imagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagesUrl = (ArrayList<String>) dataSnapshot.getValue();
                if (imagesUrl != null) {
                    for (int i = 0; i < imagesUrl.size(); i++) {
                        mArrayfarmerUri.add(Uri.parse(imagesUrl.get(i)));
                        //           Log.d("mArrayfarmerUri",imagesUrl.get(i));
                    }
                    //   gridview.setAdapter(new f_Cropgrid_logIn(getActivity()));
                    //     Log.d("mArrayfarmerUri","how many times");
                    //    gridview.setVisibility(View.VISIBLE);
                } else {
                    //   viewPager.setVisibility(View.GONE);
                    gridview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fruitfieldData() {

        fieldInfo = firebaseDatabase.getReference("Users").child(uids).child(field);
        fieldInfo.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                farmer_fruit_model = dataSnapshot.getValue(farmer_fruit_infor.class);
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
            Product_name.setText(farmer_fruit_model.getProduct_name());
            DISTT.setText(farmer_fruit_model.getDISTT());
            city.setText(farmer_fruit_model.getCity());
            TEHIL.setText(farmer_fruit_model.getTEHIL());
            Product_type.setText(farmer_fruit_model.getProduct_type());
            area_price.setText(farmer_fruit_model.getArea_price());
            plants.setText(farmer_fruit_model.getPlants());
            if (lato == 0.0 && longo == 0.0) {
                latitude = Double.parseDouble(farmer_fruit_model.getLatitude());
                longitude = Double.parseDouble(farmer_fruit_model.getLongitude());
            }
        }
        initializingMaps();
    }

    private void cropfieldData() {

        fieldInfo = firebaseDatabase.getReference("Users").child(uids).child(field);
        fieldInfo.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("datasnapShotValue", dataSnapshot+"");
                farmer_crop_model = dataSnapshot.getValue(farmer_crop_infor.class);
                CropFieldValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        preferences = getActivity().getSharedPreferences("farmerProfile_crop", Context.MODE_PRIVATE);
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
        editor.putString("userplants", plantsis);
        editor.putString("userquantity", quantityis);
        editor.putString("userlati", latitude + "");
        editor.putString("userlongi", longitude + "");
        editor.commit();
    }

    private void PersonalValues() {

        personname.setText(farmer_personal.getNamee());
        namePerson=farmer_personal.getNamee();
        phone_num.setText(farmer_personal.getPhonee());
//        Log.d("RefreshValueWas", farmer_personal.getProfile());
        if (!farmer_personal.getProfile().equals("")) {
            profileImageUrl=farmer_personal.getProfile();
            setcardImage=farmer_personal.getProfile();
            Picasso.with(getActivity())
                    .load(farmer_personal.getProfile())
                    .placeholder(R.drawable.profile1)
                    .into(ProfileImage);
        } else {
            ProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CropFieldValues() {
        if (Refresh.equals("refresh")) {
            Product_name.setText(farmer_crop_model.getProduct_name());
            Product_type.setText(farmer_crop_model.getProduct_type());
            city.setText(farmer_crop_model.getCity());
            DISTT.setText(farmer_crop_model.getDISTT());
            TEHIL.setText(farmer_crop_model.getTEHIL());
            quantity.setText(farmer_crop_model.getQuantity());
            sack_price.setText(farmer_crop_model.getSack_price());
            area.setText(farmer_crop_model.getArea());
            //harvest.setText(farmer_crop_model.get);
            if (lato == 0.0 && longo == 0.0) {
                latitude = Double.parseDouble(farmer_crop_model.getLati());
                longitude = Double.parseDouble(farmer_crop_model.getLongi());
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
                    //Toast.makeText(getActivity(),"no permission",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    return;
                } else {
                    Log.d("locationValue", latitude + "   lati");
                    Log.d("locationValue", longitude + "  logni");
                    LatLng selected = new LatLng(latitude, longitude);
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
                        transaction.replace(R.id.frame3, mapsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
        });
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
            /*case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                    Toast.makeText(getActivity(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                    }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);*/
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
        changeImage=view.findViewById(R.id.change_personprofile);
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
        } else if (view == From_Gallery) {
        //    checkPermissionREAD_EXTERNAL_STORAGE(getActivity());
            chooseFromGallery();
        } else if (view == Picture_with_cam) {
         //   checkPermissionREAD_EXTERNAL_STORAGE(getActivity());
            TakePhoto();
        } else if (view == saveData) {
            String name=farmer_personal.getNamee();
            if(!TextUtils.isEmpty(name)) {
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

    private void showDialogueBox(View view) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.farmer_choose_field_after_login,null));

        alertDialog=builder.show();
    }

    private void SendToFirebase() {
        nameis=personname.getText().toString();
        phoneis=phone_num.getText().toString();
        databaseReference = firebaseDatabase.getReference("Users").child(uids);
        databaseReferenceCard=firebaseDatabase.getReference("cardview").child(uids);
        if (field == null) {
            if (profileimageUri== null && profilephotoUri == null) {
                profileImageUrl = "";
                if(!farmer_personal.getProfile().equals("")) {
                    Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id,farmer_personal.getProfile(), field);
                    cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", "", "", "0", "", "0", farmer_personal.getProfile(), uids, "1");
                    databaseReference.child("personal").setValue(info);
                    databaseReferenceCard.setValue(cardinfo);
                }
                else
                {
                    Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, profileImageUrl, field);
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
                if(!farmer_personal.getProfile().equals("")) {
                    Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id,farmer_personal.getProfile(), field);
                    cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", "", "", "0", "", "0", farmer_personal.getProfile(), uids, "1");
                    databaseReference.child("personal").setValue(info);
                    databaseReferenceCard.setValue(cardinfo);
                }
                else
                {
                    Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, profileImageUrl, field);
                    cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", "", "", "0", "", "0", profileImageUrl, uids, "1");
                    databaseReference.child("personal").setValue(info);
                    databaseReferenceCard.setValue(cardinfo);
                }

            }
        }
        else if(field.equals("crop"))
        {
            DataOf=field;
            fieldIs=field;
            if(mArrayfarmerUri.isEmpty()!=true)
            {
                Log.d("ArrayUri", mArrayfarmerUri.size()+"");
                for(int j=0;j<mArrayfarmerUri.size();j++)
                {
                    UploadImages(mArrayfarmerUri.get(j));
                }
            }
            SaveDataToFirebase();
        }
        else {
            Log.d("ProfileImage", field+"    fieldis");
            DataOf=field;
            fieldIs=field;
            if (mArrayfarmerUri.isEmpty() != true) {
                    for (int j = 0; j < mArrayfarmerUri.size(); j++) {
                        Log.d("RefreshValueWas", "mArrayfarmerUri");
                        UploadImages(mArrayfarmerUri.get(j));
                    }
                    Log.d("ArrayUri", mArrayfarmerUri + "");
            }
            SaveDataToFirebase();
        }

    }

    private void SaveDataToFirebase() {
        if(DataOf.equals("crop")) {
            CropDataToFirebase();
            if(mArrayfarmerUri.isEmpty()==true && profileimageUri == null && profilephotoUri == null)
            {
                progressDialog.dismiss();
                Log.d("SignedInToFrag", "2");
                SignedIn();
            }
        }
        else{
            Log.d("ProfileImage", DataOf+"    savetofirebase");
            FruitDataToFirebase();
            if(mArrayfarmerUri.isEmpty()==true && profileimageUri == null && profilephotoUri == null)
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
            farmer_fruit_infor fruitinfo=new farmer_fruit_infor(productName,productType,cityis,TEHILis,DISTTis,area_priceis,plantsis,harvestfriutis,latitude+"",longitude+"");

            databaseReference.child(DataOf).setValue(fruitinfo);
            if(!farmer_personal.getProfile().equals("")) {
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, farmer_personal.getProfile(),field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", cityis, productName, "0", DISTTis, "0", farmer_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else{
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, profileImageUrl,field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
        } else {
            if (profilephotoUri == null) {
                Log.d("ProfileImage", profileimageUri+ "image uri is");
                UploadImage(profileimageUri);
            }
            if (profileimageUri == null) {
                UploadImage(profilephotoUri);
            }
//            Log.d("ProfileImages", profileImageUrl);
            Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
            farmer_fruit_infor fruitinfo2=new farmer_fruit_infor(productName,productType,cityis,TEHILis,DISTTis,area_priceis,plantsis,harvestfriutis,latitude+"",longitude+"");
            databaseReference.child(DataOf).setValue(fruitinfo2);
            if(!farmer_personal.getProfile().equals("")) {
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id,farmer_personal.getProfile(), field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(productName, "کسان", cityis, productName, "0", DISTTis, "0", farmer_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else
            {
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, profileImageUrl, field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
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
        plantsis=plants.getText().toString();
        harvestis=harvest.getText().toString();
        harvestfriutis=HarvestTime.getText().toString();

    }

    private void CropDataToFirebase() {
        Log.d("ProfileImage", "cropdatatofirebase");
        if (profileimageUri == null && profilephotoUri == null) {
            profileImageUrl = "";
            farmer_crop_infor cropinfo=new farmer_crop_infor(productName,productType,cityis,TEHILis,DISTTis,areaIs,sack_priceIs,quantityis,latitude+"",longitude+"");
            databaseReference.child(DataOf).setValue(cropinfo);
            if(!farmer_personal.getProfile().equals("")) {
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, farmer_personal.getProfile(), field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", cityis, productName, "0", DISTTis, "0", farmer_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else
            {
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, profileImageUrl, field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
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

            farmer_crop_infor cropinfo2=new farmer_crop_infor(productName,productType,cityis,TEHILis,DISTTis,areaIs,sack_priceIs,quantityis,latitude+"",longitude+"");
            databaseReference.child(DataOf).setValue(cropinfo2);
            if(!farmer_personal.getProfile().equals("")) {
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, farmer_personal.getProfile(), field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", cityis, productName, "0", DISTTis, "0", farmer_personal.getProfile(), uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
            else
            {
                Farmer_signup_infor info = new Farmer_signup_infor(nameis, phoneis, id, profileImageUrl, field);
                cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(nameis, "کسان", cityis, productName, "0", DISTTis, "0", profileImageUrl, uids, "1");
                databaseReferenceCard.setValue(cardinfo);
                databaseReference.child("personal").setValue(info);
            }
        }
    }

    private void UploadImages(Uri uri) {

        if(!uri.equals(""))
        {
            storageReference= FirebaseStorage.getInstance().getReference().child("/images/"+System.currentTimeMillis()+"."+getImageExt(uri));
            storageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageUrls=taskSnapshot.getDownloadUrl().toString();
                            SaveImages.add(imageUrls);
                            if(mArrayfarmerUri!=null) {
                                if (mArrayfarmerUri.size() == SaveImages.size()) {
                                    databaseReference.child("images").setValue(SaveImages);
                                    databaseReference.child("imagesUri").setValue(imagesUrl);
                                    progressDialog.dismiss();
                                    Log.d("SignedInToFrag", "SaveDataToFirebase: 3");
                                    SignedIn();
                                }
                            }
                            else{
                                if(mArrayfarmerUri.size()==SaveImages.size())
                                {
                                    databaseReference.child("images").setValue(SaveImages);
                                    databaseReference.child("imagesUri").setValue(imagesUrl);
                                    progressDialog.dismiss();
                                    Log.d("SignedInToFrag", "SaveDataToFirebase: 4");
                                    SignedIn();
                                }
                            }

                            Log.d("ArrayUri", SaveImages.size()+"");
                        }
                    });

        }
        else{
            //  Toast.makeText(getActivity(),"image uri doesn't found",Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImage(Uri imageUri) {
        Uri naqvi=imageUri;
        Log.d("ProfileImages", " out of if");
        Log.d("ProfileImages", imageUri+" out of if");
        if(!imageUri.equals(""))
        {
            Log.d("ProfileImages", " in of if");
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
//                            Log.d("ProfileImages",profileImageUrl);
                            profileImageUrl=taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("personal").child("profile").setValue(profileImageUrl);
                            databaseReferenceCard.child("profile").setValue(profileImageUrl);
                            if(mArrayfarmerUri==null) {
                                progressDialog.dismiss();
                                Log.d("SignedInToFrag", "SaveDataToFirebase: 5");
                                SignedIn();
                            }
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

    private void TakePhoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,UPLOAD_FROM_CAM);
    }

    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), UPLOAD_FROM_GAL);

    }

    private void UploadImageFrom() {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.profileimageafterlogin, null));

        alertDialog = builder.show();
    }

    @Override
    public void sendData(String farmer_choice) {
        if(farmer_choice.equals("camera") || farmer_choice.equals("gallery"))
        {
            Log.d("GalAndCam", farmer_choice+ "     farmer_choice");
            TakePhotoOrGallery(farmer_choice);
        }
        if(farmer_choice.equals("fruit") || farmer_choice.equals("crop"))
        {
            if(farmer_choice.equals("crop"))
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

    private void Add_fruit_product() {

        farmer_add_fruit_after_login farmerlogin=new farmer_add_fruit_after_login();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame3,farmerlogin);
        transaction.commit();
    }

    private void Add_crop_Product() {
        farmer_add_crop_after_login farmerlogin=new farmer_add_crop_after_login();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame3,farmerlogin);
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
                Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
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
            //    try {
                   /* InputStream inputStream=getActivity().getContentResolver().openInputStream(profilephotoUri);
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    ProfileImage.setImageBitmap(bitmap);*/
                    Picasso.with(getActivity())
                            .load(profilephotoUri)
                            .resize(100, 100)
                            .rotate(90)
                            .centerCrop()
                            .into(ProfileImage);

               /* } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/

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

            //upload images for respective fields

       else if(resultCode==RESULT_OK && (requestCode==UPLOAD_FROM_CAM || requestCode==UPLOAD_FROM_GAL))
        {
            if(requestCode==UPLOAD_FROM_CAM) {
                Bundle bundle = data.getExtras();
                Bitmap photo = (Bitmap) bundle.get("data");
                Uri photoUri = getImageUri(getActivity(), photo);
                mArrayfarmerUri.add(photoUri);
                imagesUrl.add(photoUri.toString());
            }

        else {
                if (requestCode == UPLOAD_FROM_GAL) {

                    if (resultCode == RESULT_OK) {

                        if (data.getData() != null) {

                            picimageUri = data.getData();
                            mArrayfarmerUri.add(picimageUri);
                            imagesUrl.add(picimageUri.toString());

                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    mArrayfarmerUri.add(uri);
                                    imagesUrl.add(uri.toString());
                                }
                            }

                        }

                    }

                }
            }

            gridview.setAdapter(new f_Cropgrid_logIn(getActivity(),mArrayfarmerUri));
            gridview.setVisibility(View.VISIBLE);

        }

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

}
