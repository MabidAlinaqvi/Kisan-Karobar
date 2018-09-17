package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import static com.example.root.formarsupport.MapFragmentAfterLogin.lato;
import static com.example.root.formarsupport.MapFragmentAfterLogin.longo;
import static com.example.root.formarsupport.TraderProfile.namePerson;
import static com.example.root.formarsupport.TraderProfile.setcardImage;
import static com.example.root.formarsupport.TraderProfile.trader_field;


/**
 * A simple {@link Fragment} subclass.
 */
public class trader_add_crop_after_login extends Fragment {

    private EditText Product_name,Product_type,city,TEHIL,DISTT,area,sack_price,quantity,HarvestTime;
    private String get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area,
            get_sack_price,get_quantity,get_HarvestTime;
    private String dateIs,uid,imageUrls;
    private ImageButton Picture_With_Camera;
    private Button From_gallery,create_account;
    static String trader_field3;
    private Toolbar mToolbar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,cardReference;
    View view;
    SharedPreferences preferences;
    static Double crop_latitu,crop_longitu;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    trader_crop_infor traderCropInfor;
    Marker marker = null;
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_trader_add_crop_after_login, container, false);
        mToolbar=view.findViewById(R.id.trader_crop_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.add_crop));
        gettingIds();
        trader_field3="crop";
        databaseReference=firebaseDatabase.getReference("Users").child(uid);
        cardReference=firebaseDatabase.getReference("cardview").child(uid);
        retrevingFShared();
        if(lato !=0.0 && longo !=0.0) {
            crop_latitu=lato;
            crop_longitu=longo;
        }

        initializingMaps();
        CreateAccountButton();
        HarvestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update();
        }

    };
    private void update() {
        String myFormat = "yy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        dateIs=sdf.format(myCalendar.getTime()).toString();
        Toast.makeText(getActivity(),dateIs,Toast.LENGTH_SHORT).show();
        HarvestTime.setText(dateIs);
    }

    private void CreateAccountButton() {

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getResources().getString(R.string.savingProfile));
                progressDialog.show();
                gettingValuesFromField();
                SaveDataToFirebase();
            }
        });

    }

    private void SaveDataToFirebase() {
        traderCropInfor=new trader_crop_infor(get_Product_name,get_city,get_TEHIL,get_DISTT,get_sack_price,get_quantity,crop_latitu+"",crop_longitu+"");
        databaseReference.child("crop").setValue(traderCropInfor);
        cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(namePerson, "کسان", get_city, get_Product_name, "0", get_DISTT, "0", setcardImage, uid, "1");
        cardReference.setValue(cardinfo);
        databaseReference.child("personal").child("field").setValue("crop");
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

    private void gettingIds() {

        Product_name=view.findViewById(R.id.product);
        Product_type=view.findViewById(R.id.product_type);
        city=view.findViewById(R.id.city);
        TEHIL=view.findViewById(R.id.Thsil);
        DISTT=view.findViewById(R.id.District);
        area=view.findViewById(R.id.area);
        sack_price=view.findViewById(R.id.sack);
        quantity=view.findViewById(R.id.quantity);
        HarvestTime=view.findViewById(R.id.harvast);
        create_account=view.findViewById(R.id.button2);
        firebaseDatabase=FirebaseDatabase.getInstance();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInShared();
    }
    /**
     * saveing values in file
     *
     * https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
     */
    private void saveInShared(){
        gettingValuesFromField();
        preferences=getActivity().getSharedPreferences("traderlogin_crop", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Toast.makeText(getActivity(),"crop trader data is saving",Toast.LENGTH_LONG).show();
        editor.putString("userproduct",get_Product_name);
        editor.putString("usertype",get_Product_type);
        editor.putString("usercity", get_city);
        editor.putString("usertehsil", get_TEHIL);
        editor.putString("userdistric", get_DISTT);
        editor.putString("userarea", get_area);
        editor.putString("usersack", get_sack_price);
        editor.putString("userquantity", get_quantity);
        editor.putString("userlati", crop_latitu+"");
        editor.putString("userlongi", crop_longitu+"");
        editor.commit();
    }

    private void gettingValuesFromField() {

        get_Product_name=Product_name.getText().toString();
        get_Product_type=Product_type.getText().toString();
        get_city=city.getText().toString();
        get_TEHIL=TEHIL.getText().toString();
        get_DISTT=DISTT.getText().toString();
        get_area=area.getText().toString();
        get_sack_price=sack_price.getText().toString();
        get_quantity=quantity.getText().toString();
        get_HarvestTime=HarvestTime.getText().toString();

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

    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("traderlogin_crop", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("userproduct", "");   //will return false if doesn't exist "user_name"
        if (!Notexit.equals("")) {
            String userproduct = preferences.getString("userproduct", "");
            String usertype = preferences.getString("usertype", "");
            String usercity = preferences.getString("usercity", "");
            String usertehsil = preferences.getString("usertehsil", "");
            String userdistric = preferences.getString("userdistric", "");
            String userarea = preferences.getString("userarea", "");
            String usersack = preferences.getString("usersack", "");
            String userquantity = preferences.getString("userquantity", "");
            String userlati = preferences.getString("userlati", "");
            String userlongi = preferences.getString("userlongi", "");
            if(userlati != null && userlongi != null)
            {
                crop_latitu=Double.parseDouble(userlati);
                crop_longitu=Double.parseDouble(userlongi);
            }
            Product_name.setText(userproduct);
            Product_type.setText(usertype);
            city.setText(usercity);
            TEHIL.setText(usertehsil);
            DISTT.setText(userdistric);
            area.setText(userarea);
            sack_price.setText(usersack);
            quantity.setText(userquantity);
        } else {
            Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
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
                else if((lato==0.0 && longo==0.0) && (crop_latitu==null && crop_longitu==null)) {
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        crop_latitu=location.getLatitude();
                        crop_longitu=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));
                    }
                }
                else {
                    Log.d("gettingCoordinate", crop_latitu+"   crop_lati");
                    LatLng selected = new LatLng(crop_latitu, crop_longitu);
                    if (marker != null) {
                        marker.remove();
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected, 15));
                    marker = mMap.addMarker(new MarkerOptions().position(selected).title("marked"));
                }
                mMap.setMyLocationEnabled(true);

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


}
