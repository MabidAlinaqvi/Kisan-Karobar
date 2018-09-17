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
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class fruit_trader extends Fragment {
private String pro_name,pro_type,city,tehsil,distt,area_price;
private EditText proname,protype,City,Tehsil,Distt,Area;
private Button registerAccount;
    GoogleMap mMap;
    static Double trader_f_latitu,trader_f_longitu;
    LocationManager locationManager;
    Marker marker = null;
    MapFragment mapFragment;
   static trader_fruit_infor trader_f_user;
View view;
SharedPreferences preferences;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_fruit_trader, container, false);
        gettingIds();
        retrevingFShared();
        if(lati !=0.0 && longi !=0.0) {
            trader_f_latitu=lati;
            trader_f_longitu=longi;
        }
        CreatingMapView();
        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettingValues();
                trader_sign_up trader_sign_up=new trader_sign_up();
                FragmentTransaction transaction;
                FragmentManager manager=getFragmentManager();
                transaction=manager.beginTransaction();
                transaction.replace(R.id.frame,trader_sign_up);
                transaction.addToBackStack(null);
                transaction.commit();
                trader_f_user=new trader_fruit_infor(pro_name,pro_type,city,tehsil,
                        distt,area_price,trader_f_latitu+"",trader_f_longitu+"");
            }
        });
        return view;
    }
    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("trader_fruit", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("Tfproduct", "");
     //   Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {

            String username = preferences.getString("Tfproduct","" );
            String usertype = preferences.getString("Tftype", "");
            String usercity = preferences.getString("Tfcity","");
            String usertehsil = preferences.getString("Tftehsil", "");
            String userdistt = preferences.getString("Tfdistt", "");
            String userarea = preferences.getString("Tfarea", "");
            String userlati = preferences.getString("Tflati", "");
            String userlongi = preferences.getString("Tflongi", "");
            if(userlati !=null && userlongi !=null)
            {
                trader_f_latitu=Double.valueOf(userlati);
                trader_f_longitu=Double.valueOf(userlongi);
            }
            proname.setText(username);
            protype.setText(usertype);
            City.setText(usercity);
            Tehsil.setText(usertehsil);
            Distt.setText(userdistt);
            Area.setText(userarea);

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
        gettingValues();
        preferences=getActivity().getSharedPreferences("trader_fruit",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Toast.makeText(getActivity(),"fruit trader data is saving",Toast.LENGTH_LONG).show();

        editor.putString("Tfproduct",pro_name);
        editor.putString("Tftype",pro_name);
        editor.putString("Tfcity", city);
        editor.putString("Tftehsil", tehsil);
        editor.putString("Tfdistt", distt);
        editor.putString("Tfarea", area_price);
        editor.putString("Tflati", trader_f_latitu+"");
        editor.putString("Tflongi", trader_f_longitu+"");
        editor.commit();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void CreatingMapView() {
        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        /**
         * it is showing the google map
         *
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         */
        //  SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment= (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
     //   Log.d(TAG, mapFragment+"");
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
                else if((lati==0.0 && longi==0.0) && (trader_f_latitu==null && trader_f_longitu==null)) {
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        trader_f_latitu=location.getLatitude();
                        trader_f_longitu=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else {
                    LatLng selected = new LatLng(trader_f_latitu, trader_f_longitu);
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

    private void gettingValues() {
        pro_name=proname.getText().toString();
        pro_type=protype.getText().toString();
        city=City.getText().toString();
        tehsil=Tehsil.getText().toString();
        distt=Distt.getText().toString();
        area_price=Area.getText().toString();
    }

    private void gettingIds() {
        proname=view.findViewById(R.id.product);
        protype=view.findViewById(R.id.product_type);
        City=view.findViewById(R.id.city);
        Tehsil=view.findViewById(R.id.Thsil);
        Distt=view.findViewById(R.id.District);
        Area=view.findViewById(R.id.area);
        registerAccount=view.findViewById(R.id.create_account);
    }

}
