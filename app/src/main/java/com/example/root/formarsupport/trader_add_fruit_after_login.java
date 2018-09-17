package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Permission;

import static com.example.root.formarsupport.MapFragmentAfterLogin.lato;
import static com.example.root.formarsupport.MapFragmentAfterLogin.longo;
import static com.example.root.formarsupport.TraderProfile.namePerson;
import static com.example.root.formarsupport.TraderProfile.setcardImage;


/**
 * A simple {@link Fragment} subclass.
 */
public class trader_add_fruit_after_login extends Fragment {
    private EditText Product_name,Product_type,city,TEHIL,DISTT,area_price,plants,harvest,WaterSource;
    private String get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area_price,get_plants,get_harvest,get_WaterSource,uid,imageUrls;
    private View view;
    private ImageButton take_photo;
    private Button register_account,take_gallery;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,cardReference;
    FirebaseAuth mAuth;
    private Toolbar mToolbar;
    ProgressDialog progressDialog;
    GoogleMap mMap;
    static String trader_field2;
    MapFragment mapFragment;
    static Double fruit_latitu,fruit_longitu;
    Double userlati=0.0,userlongi=0.0;
    LocationManager locationManager;
    Marker marker = null;
    SharedPreferences preferences;
    private trader_fruit_infor fruit_user;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Permission permission;
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_trader_add_fruit_after_login, container, false);
        mToolbar=view.findViewById(R.id.trader_fruit_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.add_fruit));
        gettingIds();
        trader_field2="fruit";
        databaseReference=firebaseDatabase.getReference("Users").child(uid);
        cardReference=firebaseDatabase.getReference("cardview").child(uid);
        retrevingFShared();
        if(lato !=0.0 && longo !=0.0) {
            fruit_latitu=lato;
            fruit_longitu=longo;
        }
        else if(userlati!=0.0 && userlongi!=0.0)
        {
            fruit_latitu=userlati;
            fruit_longitu=userlongi;
        }
        CreateMaps();
        RegistrationButton();
        gettinValuesFromField();
        return view;
    }

    private void RegistrationButton() {
        register_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getResources().getString(R.string.savingProfile));
                progressDialog.show();
                gettinValuesFromField();
                SaveDataToFirebase();
            }
        });
    }

    private void SaveDataToFirebase() {
        fruit_user=new trader_fruit_infor(get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area_price,fruit_latitu+"",fruit_longitu+"");
        databaseReference.child("fruit").setValue(fruit_user);
        cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(namePerson, "کسان", get_city, get_Product_name, "0", get_DISTT, "0", setcardImage, uid, "1");
        databaseReference.child("personal").child("field").setValue("fruit");
        cardReference.setValue(cardinfo);
        progressDialog.dismiss();
        SignedIn();

    }

    private void SignedIn() {
        TraderProfile farmerProfile=new TraderProfile();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.traderFrame,farmerProfile);
        transaction.commit();
    }

    private void retrevingFShared() {

        preferences = getActivity().getSharedPreferences("traderlogin_fruit", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("userproduct", "");   //will return false if doesn't exist "user_name"
        if (!Notexit.equals("")) {

            String userproduct = preferences.getString("userproduct", "");
            String usertype = preferences.getString("usertype", "");
            String usercity = preferences.getString("usercity", "");
            String usertehsil = preferences.getString("usertehsil", "");
            String userdistric = preferences.getString("userdistric", "");
            String userarea = preferences.getString("userarea", "");
            String userharvest = preferences.getString("userharvest", "");
            String userlatiIs=preferences.getString("userlati","");
            String userlongiIs=preferences.getString("userlongi","");
            if(userlatiIs!=null ||userlongiIs!=null) {
                fruit_latitu = Double.valueOf(preferences.getString("userlati", ""));
                fruit_longitu = Double.valueOf(preferences.getString("userlongi", ""));
            }
            Product_name.setText(userproduct);
            Product_type.setText(usertype);
            city.setText(usercity);
            TEHIL.setText(usertehsil);
            DISTT.setText(userdistric);
            area_price.setText(userarea);
            harvest.setText(userharvest);
        } else {
            Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CreateMaps() {

        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        /**
         * it is showing the google map
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         * https://stackoverflow.com/questions/33666071/android-marshmallow-request-permission
         */
        //  SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment= (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        Log.d("code_verified", mapFragment+"");
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    //   selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION);

                    return;
                }
                else if(lato==0.0 && longo==0.0 && userlati==0.0) {
                    //   Log.d("LatiAndLongi", "entered");
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        fruit_latitu=location.getLatitude();
                        fruit_longitu=location.getLongitude();
                        //  Log.d("code_verified",latitu+"   "+longitu);
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else {
                    Log.d("gettingCoordinate", fruit_latitu+"   crop_lati");
                    //  Log.d("Coordinates", fruit_lati+"");
                    // Log.d("Coordinates", fruit_longi+"");
                    LatLng selected = new LatLng(fruit_latitu, fruit_longitu);
                    if (marker != null) {
                        marker.remove();
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected, 17));
                    marker = mMap.addMarker(new MarkerOptions().position(selected).title("marked"));
                }

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MapFragmentAfterLogin mapsFragment=new MapFragmentAfterLogin();
                        FragmentTransaction transaction;
                        FragmentManager manager=getFragmentManager();
                        transaction=manager.beginTransaction();
                        transaction.replace(R.id.traderFrame,mapsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });
            }

        });

    }

    private void gettingIds() {
        Product_name=view.findViewById(R.id.product);
        Product_type=view.findViewById(R.id.product_type);
        city=view.findViewById(R.id.city);
        TEHIL=view.findViewById(R.id.Thsil);
        DISTT=view.findViewById(R.id.District);
        area_price=view.findViewById(R.id.area);
        plants=view.findViewById(R.id.quantity);
        harvest=view.findViewById(R.id.harvast);
        register_account=view.findViewById(R.id.create_account);
        firebaseDatabase= FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInShared();
    }
    private void saveInShared(){
        gettinValuesFromField();
        preferences=getActivity().getSharedPreferences("traderlogin_fruit",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Toast.makeText(getActivity(),"crop trader data is saving",Toast.LENGTH_LONG).show();
        editor.putString("userproduct",get_Product_name);
        editor.putString("usertype",get_Product_type);
        editor.putString("usercity", get_city);
        editor.putString("usertehsil", get_TEHIL);
        editor.putString("userdistric", get_DISTT);
        editor.putString("userarea", get_area_price);
        editor.putString("userplants", get_plants);
        editor.putString("userharvest", get_harvest);
        editor.putString("userlati", fruit_latitu+"");
        editor.putString("userlongi", fruit_longitu+"");
        editor.commit();
    }

    private void gettinValuesFromField() {
        get_Product_name=Product_name.getText().toString();
        get_Product_type=Product_type.getText().toString();
        get_city=city.getText().toString();
        get_TEHIL=TEHIL.getText().toString();
        get_DISTT=DISTT.getText().toString();
        get_area_price=area_price.getText().toString();
        get_harvest=harvest.getText().toString();
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

}
