package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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

import java.util.List;

import static com.example.root.formarsupport.Fertilizer_medic_info.fari_lati;
import static com.example.root.formarsupport.Fertilizer_medic_info.fari_longi;
import static com.example.root.formarsupport.Sign_Up_As.Former_choice;
import static com.example.root.formarsupport.Sign_Up_As.chosen;
import static com.example.root.formarsupport.broker_signUP.broker_lati;
import static com.example.root.formarsupport.broker_signUP.broker_longi;
import static com.example.root.formarsupport.crop_trader.trader_c_latitu;
import static com.example.root.formarsupport.crop_trader.trader_c_longitu;
import static com.example.root.formarsupport.farmer_add_crop.latitude;
import static com.example.root.formarsupport.farmer_add_crop.longitude;
import static com.example.root.formarsupport.farmer_fruit_product.latitu;
import static com.example.root.formarsupport.farmer_fruit_product.longitu;
import static com.example.root.formarsupport.fruit_trader.trader_f_latitu;
import static com.example.root.formarsupport.fruit_trader.trader_f_longitu;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements View.OnClickListener {
private View view;
private String TAG="WorkingFine";
private GoogleMap mMap;
private MapFragment mapFragment;
LocationManager locationManager;
EditText search_country;
Button view_type,save_location,search;
static double lati;
static double longi;
Marker marker = null;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_maps, container, false);
        intializingIds();
        intializingMaps();
        SettingviewForClick();
        return view;
    }

    private void SettingviewForClick() {
        search.setOnClickListener(MapsFragment.this);
        view_type.setOnClickListener(MapsFragment.this);
        save_location.setOnClickListener(MapsFragment.this);
    }

    @Override
    public void onClick(View view) {
        if(view==search)
        {
            Search_location(view);
        }
        else if(view==view_type)
        {
            changtype(view);
        }
        else{
            save_location(view);
        }

    }

    private void intializingIds() {
        search=view.findViewById(R.id.search_location);
        view_type=view.findViewById(R.id.map_type);
        save_location=view.findViewById(R.id.save);
        search_country=view.findViewById(R.id.insert_location);

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
                    if(chosen.equals("farmer"))
                    {
                    if (Former_choice.equals("crop")) {
                        LatLng current = new LatLng(latitude, longitude);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
                    } else {
                        LatLng current = new LatLng(latitu, longitu);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
                    }
                }
                else if(chosen.equals("trader")){
                        getMapOfTrader();
                    }
                else if(chosen.equals("fertilizer"))
                    {
                        getMapOfFertilizer();
                    }
                else
                    {
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

    private void getMapOfFertilizer() {
        LatLng current = new LatLng(fari_lati, fari_longi);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
    }

    private void getMapOfBroker() {
        LatLng current = new LatLng(broker_lati, broker_longi);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
    }

    private void getMapOfTrader() {
        if (Former_choice.equals("crop")) {
            LatLng current = new LatLng(trader_c_latitu, trader_c_longitu);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
        } else {
            Toast.makeText(getActivity(),"right place",Toast.LENGTH_SHORT).show();
            LatLng current = new LatLng(trader_f_latitu, trader_f_longitu);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
        }
    }

    public void Search_location(View view) {
        String locationRecevied = search_country.getText().toString();
        //   Log.d(TAG, locationRecevied);
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,30));
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));
                //   Log.d(TAG, "g0t location");
            } catch (Exception e) {

                Toast.makeText(getActivity(), "Did not found location", Toast.LENGTH_SHORT).show();
            }



        }
    }
    public void changtype(View view) {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
    public void save_location(View view) {
        if(lati != 0.0 || longi != 0.0)
        {
            if(chosen.equals("farmer")) {
                if (Former_choice.equals("crop")) {
                    //Toast.makeText(getActivity(), lati + "  " + longi, Toast.LENGTH_SHORT).show();
                    farmer_add_crop farmer_add_crop = new farmer_add_crop();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, farmer_add_crop);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    farmer_fruit_product farmer_fruit_product = new farmer_fruit_product();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, farmer_fruit_product);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
            else if(chosen.equals("trader")){

                if(Former_choice.equals("crop"))
                {
                    crop_trader crop_trader=new crop_trader();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, crop_trader);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    fruit_trader fruit_trader=new fruit_trader();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, fruit_trader);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
            else if(chosen.equals("fertilizer"))
            {
              //  Toast.makeText(getActivity(),"fertilizer",Toast.LENGTH_SHORT).show();
                Fertilizer_medic_info fertilizersignup=new Fertilizer_medic_info();
                FragmentTransaction transaction;
                FragmentManager manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, fertilizersignup);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else{
                broker_signUP brokersignup=new broker_signUP();
                FragmentTransaction transaction;
                FragmentManager manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, brokersignup);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        }
        else{
            getActivity().finish();
        }

    }
}
