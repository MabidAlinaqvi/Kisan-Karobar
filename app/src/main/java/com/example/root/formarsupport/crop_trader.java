package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.root.formarsupport.MapsFragment.lati;
import static com.example.root.formarsupport.MapsFragment.longi;


public class crop_trader extends Fragment {
    private EditText product_name,city,TEHSIL,DISTT,Sack_price,quantity;
    private String get_product_name,get_city,get_TEHSIL,get_DISTT,get_Sack_price,get_quantity;
    private Button create_account;
    private View view;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    SharedPreferences preferences;
    static Double trader_c_latitu,trader_c_longitu;
    static trader_crop_infor trader_crop_user;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_crop_trader,container,false);
        gettingIds();
        Log.d("SaveValues", trader_c_latitu+"  naqvi  "+trader_c_longitu);
        retrevingFShared();
        if(lati !=0.0 && longi !=0.0) {
            trader_c_latitu=lati;
            trader_c_longitu=longi;
        }
        ShowingMaps();
        ClickCreateAccount();

        return view;
    }

    private void ClickCreateAccount() {
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettinValues();
                trader_sign_up trader_sign_up=new trader_sign_up();
                FragmentTransaction transaction;
                FragmentManager manager=getFragmentManager();
                transaction=manager.beginTransaction();
                transaction.replace(R.id.frame,trader_sign_up);
                transaction.addToBackStack(null);
                transaction.commit();
                trader_crop_user=new trader_crop_infor(get_product_name,get_city,get_TEHSIL,get_DISTT,
                        get_Sack_price,get_quantity,trader_c_latitu+"",trader_c_longitu+"");
            }
        });
    }

    /**
     * Retreive value from file
     */
    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("trader_crop", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("Tc_name", "");   //will return false if doesn't exist "user_name"
        Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {
            String userproduct = preferences.getString("Tc_name", "");
            String usercity = preferences.getString("Tc_city", "");
            String usertehsil = preferences.getString("Tc_tehsil", "");
            String userdistric = preferences.getString("Tc_distric", "");
            String usersack = preferences.getString("Tc_sack", "");
            String userquantity = preferences.getString("Tc_quant", "");
            String userlati = preferences.getString("Tc_lati", "");
            String userlongi = preferences.getString("Tc_longi", "");
            if(userlati !=null && userlongi !=null)
            {
                trader_c_latitu= Double.valueOf(userlati);
                trader_c_longitu=Double.valueOf(userlongi);
            }
            product_name.setText(userproduct);
            city.setText(usercity);
            TEHSIL.setText(usertehsil);
            DISTT.setText(userdistric);
            Sack_price.setText(usersack);
            quantity.setText(userquantity);

        } else {
            Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInShared();
    }
    /**
     * saveing values in file
     */
    private void saveInShared(){
        gettinValues();
        preferences=getActivity().getSharedPreferences("trader_crop",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Toast.makeText(getActivity(),"crop trader data is saving",Toast.LENGTH_LONG).show();
        editor.putString("Tc_name",get_product_name );
        editor.putString("Tc_city", get_city);
        editor.putString("Tc_tehsil", get_TEHSIL);
        editor.putString("Tc_distric", get_DISTT);
        editor.putString("Tc_sack", get_Sack_price);
        editor.putString("Tc_quant", get_quantity);
        editor.putString("Tc_lati", trader_c_latitu+"");
        editor.putString("Tc_longi", trader_c_longitu+"");
        editor.commit();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void ShowingMaps() {
        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        /**
         * it is showing the google map
         *
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         */
        //  SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment= (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       // Log.d(TAG, mapFragment+"");
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
                else if((lati==0.0 && longi==0.0) && (trader_c_latitu==null && trader_c_longitu==null)) {
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    Log.d("SaveValues", trader_c_latitu+"   naqviabid");
                    if (location != null && trader_c_latitu==null ) {
                        trader_c_latitu=location.getLatitude();
                        trader_c_longitu=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));
                    }
                    else{
                        SelectPlace();
                    }
                }
                else {
                    SelectPlace();

                }
                mMap.setMyLocationEnabled(true);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
                });
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
    private void SelectPlace() {
        LatLng selected = new LatLng(trader_c_latitu, trader_c_longitu);
        if (marker != null) {
            marker.remove();
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected, 17));
        marker = mMap.addMarker(new MarkerOptions().position(selected).title("marked"));
    }

    private void gettingIds() {

        product_name=view.findViewById(R.id.product);
        city=view.findViewById(R.id.city);
        TEHSIL=view.findViewById(R.id.Thsil);
        DISTT=view.findViewById(R.id.District);
        Sack_price=view.findViewById(R.id.sack);
        quantity=view.findViewById(R.id.quantity);
        create_account=view.findViewById(R.id.button2);
    }
    private void gettinValues(){
        get_product_name=product_name.getText().toString();
        get_city=city.getText().toString();
        get_TEHSIL=TEHSIL.getText().toString();
        get_DISTT=DISTT.getText().toString();
        get_Sack_price=Sack_price.getText().toString();
        get_quantity=quantity.getText().toString();


    }

}
