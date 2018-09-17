/*
package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
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

import java.util.List;

import static com.example.root.formarsupport.Farmer_crop_click_Own_listItem.Former_choice;
import static com.example.root.formarsupport.Farmer_crop_click_Own_listItem.chosen;
import static com.example.root.formarsupport.Farmer_crop_click_Own_listItem.f_latiLogin;
import static com.example.root.formarsupport.Farmer_crop_click_Own_listItem.f_logiLogin;
import static com.example.root.formarsupport.Trader_crop_click_Own_listItem.Choose;
import static com.example.root.formarsupport.Trader_crop_click_Own_listItem.t_latiLogin;
import static com.example.root.formarsupport.Trader_crop_click_Own_listItem.t_logiLogin;
import static com.example.root.formarsupport.Trader_fruit_click_Own_listItem.t_latituLogin;
import static com.example.root.formarsupport.Trader_fruit_click_Own_listItem.t_logituLogin;


*/
/**
 * A simple {@link Fragment} subclass.
 *//*


public class fMapFragmentLogIn extends Fragment implements View.OnClickListener {
    private View view;
    private String TAG = "WorkingFine";
    private GoogleMap mMap;
    private MapFragment mapFragment;
    LocationManager locationManager;
    EditText search_country;
    Button view_type, save_location, search;
    static double lati;
    static double longi;
    String choice,Typechoose;
    Marker marker = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_f_map_fragment_log_in, container, false);
        if(chosen==null)
        {
            choice="trader";
            if(Choose==null)
            {
                Typechoose="fruit";
            }
        }
        else{
            choice="farmer";
        }
        intializingIds();
        intializingMaps();
        SettingviewForClick();
        return view;
    }

    private void SettingviewForClick() {
        search.setOnClickListener(fMapFragmentLogIn.this);
        view_type.setOnClickListener(fMapFragmentLogIn.this);
        save_location.setOnClickListener(fMapFragmentLogIn.this);
    }

    @Override
    public void onClick(View view) {
        if (view == search) {
            Search_location(view);
        } else if (view == view_type) {
            changtype(view);
        } else {
            save_location(view);
        }

    }

    private void intializingIds() {
        search = view.findViewById(R.id.search_location);
        view_type = view.findViewById(R.id.map_type);
        save_location = view.findViewById(R.id.save);
        search_country = view.findViewById(R.id.insert_location);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    private void intializingMaps() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        Log.d(TAG, mapFragment + "");
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                    return;
                } else {
                    // Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    //  if (location != null) {
                    Log.d("CheckOutPut", choice+"    "+Former_choice);
                    if (choice.equals("farmer")) {
                        if (Former_choice.equals("crop")) {
                            Log.d("CheckOutPut", f_latiLogin+"    "+f_logiLogin);
                            LatLng current = new LatLng(f_latiLogin, f_logiLogin);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                            if (marker != null) {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
                        } else {
                           */
/* LatLng current = new LatLng(fruit_latitu, fruit_longitu);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                            if (marker != null) {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));*//*

                        }
                    } else if (choice.equals("trader")) {
                        getMapOfTrader();
                    } else {
                        getMapOfBroker();
                    }
                    //    }
                }
                mMap.setMyLocationEnabled(true);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        lati = latLng.latitude;
                        longi = latLng.longitude;
                        Toast.makeText(getActivity(),
                                latLng.latitude + ", " + latLng.longitude,
                                Toast.LENGTH_SHORT).show();
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                    }
                });
            }
        });
    }

    private void getMapOfBroker() {
       */
/* LatLng current = new LatLng(broker_lati, broker_longi);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));*//*

    }

    private void getMapOfTrader() {
        if (Typechoose.equals("crop")) {
            LatLng current = new LatLng(t_latiLogin, t_logiLogin);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
        } else {
            //Toast.makeText(getActivity(), "right place", Toast.LENGTH_SHORT).show();
            LatLng current = new LatLng(t_latituLogin, t_logituLogin);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
        }
    }

    public void Search_location(View view) {
        String locationRecevied = search_country.getText().toString();
        List<Address> addressList = null;
        if (locationRecevied != null || !locationRecevied.equals("")) {

            try {
                Geocoder geocoder = new Geocoder(getActivity());
                addressList = geocoder.getFromLocationName(locationRecevied, 1);
                Address address = addressList.get(0);
                double x, y;
                x = address.getLatitude();
                y = address.getLongitude();
                // Log.d(TAG, x + "    " + y);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                if (marker != null) {
                    marker.remove();
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));
                //   Log.d(TAG, "g0t location");
            } catch (Exception e) {

                Toast.makeText(getActivity(), "Did not found location", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void changtype(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void save_location(View view) {
        if (lati != 0.0 || longi != 0.0) {
            if (choice.equals("farmer")) {
                if (Former_choice.equals("crop")) {
                    //Toast.makeText(getActivity(), lati + "  " + longi, Toast.LENGTH_SHORT).show();
                    Farmer_crop_click_Own_listItem farmer_add_crop = new Farmer_crop_click_Own_listItem();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame3, farmer_add_crop);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Farmer_fruit_click_Own_listItem farmer_fruit_product = new Farmer_fruit_click_Own_listItem();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame3, farmer_fruit_product);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if (choice.equals("trader")) {
                if (Typechoose.equals("crop")) {
                    Trader_crop_click_Own_listItem crop_trader = new Trader_crop_click_Own_listItem();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame3, crop_trader);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Trader_fruit_click_Own_listItem fruit_trader = new Trader_fruit_click_Own_listItem();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame3, fruit_trader);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } */
/*else {
                broker_signUP brokersignup = new broker_signUP();
                FragmentTransaction transaction;
                FragmentManager manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, brokersignup);
                transaction.addToBackStack(null);
                transaction.commit();
            }*//*


        } else {
            getActivity().finish();
        }

    }


}
*/
