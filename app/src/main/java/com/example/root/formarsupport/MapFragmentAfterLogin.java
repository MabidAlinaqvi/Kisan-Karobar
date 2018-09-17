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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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


import static com.example.root.formarsupport.BrokerProfile.brok_lati;
import static com.example.root.formarsupport.BrokerProfile.brok_longi;
import static com.example.root.formarsupport.BrokerProfile.broker_id;
import static com.example.root.formarsupport.FarmerProfile.id;
import static com.example.root.formarsupport.FarmerProfile.latitude;
import static com.example.root.formarsupport.FarmerProfile.longitude;
import static com.example.root.formarsupport.TraderProfile.trader_id;
import static com.example.root.formarsupport.TraderProfile.trader_latitude;
import static com.example.root.formarsupport.TraderProfile.trader_longitude;
import static com.example.root.formarsupport.farmer_add_crop_after_login.add_field;
import static com.example.root.formarsupport.farmer_add_crop_after_login.crop_lati;
import static com.example.root.formarsupport.farmer_add_crop_after_login.crop_longi;
import static com.example.root.formarsupport.farmer_add_fruit_after_login.add_field2;
import static com.example.root.formarsupport.farmer_add_fruit_after_login.fruit_lati;
import static com.example.root.formarsupport.farmer_add_fruit_after_login.fruit_longi;
import static com.example.root.formarsupport.trader_add_crop_after_login.crop_latitu;
import static com.example.root.formarsupport.trader_add_crop_after_login.crop_longitu;
import static com.example.root.formarsupport.trader_add_crop_after_login.trader_field3;
import static com.example.root.formarsupport.trader_add_fruit_after_login.fruit_latitu;
import static com.example.root.formarsupport.trader_add_fruit_after_login.fruit_longitu;
import static com.example.root.formarsupport.trader_add_fruit_after_login.trader_field2;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragmentAfterLogin extends Fragment implements View.OnClickListener {
    private GoogleMap mMap;
    private MapFragment mapFragment;
    LocationManager locationManager;
    EditText search_country;
    Button view_type,save_location,search;
    static double lato;
    static double longo;
    double latitu,longitu;
    static String Refresh="refresh";
    private Toolbar mToolbar;
    Marker marker = null;
    View view;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view=inflater.inflate(R.layout.fragment_map_fragment_after_login, container, false);
        mToolbar=view.findViewById(R.id.map_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.mapIs));
         if(latitude==null && fruit_lati==null && trader_latitude==null && fruit_latitu==null && crop_latitu==null && brok_lati==null)
         {
             latitu=crop_lati;
             longitu=crop_longi;
         }
         else if(crop_lati==null && fruit_lati==null && trader_latitude==null && fruit_latitu==null && crop_latitu==null && brok_lati==null)
         {
             latitu=latitude;
             longitu=longitude;
         }
         else if(crop_lati==null && latitude==null && trader_latitude==null && fruit_latitu==null && crop_latitu==null && brok_lati==null)
         {
             latitu=fruit_lati;
             longitu=fruit_longi;
         }
         else if(crop_lati==null && latitude==null && fruit_lati==null && fruit_latitu==null && crop_latitu==null && brok_lati==null)
         {
             latitu=trader_latitude;
             longitu=trader_longitude;
         }
         else if(crop_lati==null && latitude==null && fruit_lati==null && trader_latitude==null && crop_latitu==null && brok_lati==null)
         {
             latitu=fruit_latitu;
             longitu=fruit_longitu;
         }
         else if(crop_lati==null && latitude==null && fruit_lati==null && trader_latitude==null && fruit_latitu==null && brok_lati==null)
         {
             latitu=crop_latitu;
             longitu=crop_longitu;
         }
         else if(crop_lati==null && latitude==null && fruit_lati==null && trader_latitude==null && fruit_latitu==null && crop_latitu==null)
         {
             latitude=brok_lati;
             longitu=brok_longi;
         }
        intializingIds();
        intializingMaps();
        SettingviewForClick();
        Refresh="not_refresh";
         return view;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void intializingMaps() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.loginmap);
      //  Log.d(TAG, mapFragment + "");
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
                //    Log.d("UserIs", id+" map");
                    if(id!=null || trader_id!=null || broker_id!=null)
                    {
                      //  if (field.equals("crop")) {
                            LatLng current = new LatLng(latitu, longitu);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                            if (marker != null) {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
                       /* } else {
                            LatLng current = new LatLng(latitu, longitu);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                            if (marker != null) {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
                        }*/
                    }
                    /*else if(trader_id!=null){
                        getMapOfTrader();
                    }*/
                    else
                    {
                      //  getMapOfBroker();
                    }
                    //    }
                }
                mMap.setMyLocationEnabled(true);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        lato = latLng.latitude;
                        longo = latLng.longitude;
                        Toast.makeText(getActivity(),
                                latLng.latitude+ ", " + latLng.longitude,
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

    private void intializingIds() {

        search=view.findViewById(R.id.loginsearch_location);
        view_type=view.findViewById(R.id.loginmap_type);
        save_location=view.findViewById(R.id.loginsave);
        search_country=view.findViewById(R.id.logininsert_location);

    }
    private void SettingviewForClick() {
        search.setOnClickListener(MapFragmentAfterLogin.this);
        view_type.setOnClickListener(MapFragmentAfterLogin.this);
        save_location.setOnClickListener(MapFragmentAfterLogin.this);
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

    private void save_location(View view) {
        if(lato != 0.0 || longo != 0.0)
        {
            Log.d("Coordinates", latitu+"  latitu");
            Log.d("Coordinates", longitu+"  longitu");
            if(id!=null) {
                Log.d("CheckingIds", id);
              if(add_field!=null) {
                //  if (add_field.equals("crop")) {
                      farmer_add_crop_after_login farmer_crop = new farmer_add_crop_after_login();
                      FragmentTransaction transaction;
                      FragmentManager manager = getFragmentManager();
                      transaction = manager.beginTransaction();
                      transaction.replace(R.id.frame3, farmer_crop);
                      transaction.addToBackStack(null);
                      transaction.commit();
                  }
                  else if(add_field2!=null) {
                  //if (add_field2.equals("fruit")) {

                      farmer_add_fruit_after_login farmer_fruit = new farmer_add_fruit_after_login();
                      FragmentTransaction transaction;
                      FragmentManager manager = getFragmentManager();
                      transaction = manager.beginTransaction();
                      transaction.replace(R.id.frame3, farmer_fruit);
                      transaction.addToBackStack(null);
                      transaction.commit();

               //   }
              }
              else
              {
                  FarmerProfile farmer_add_crop = new FarmerProfile();
                  FragmentTransaction transaction;
                  FragmentManager manager = getFragmentManager();
                  transaction = manager.beginTransaction();
                  transaction.replace(R.id.frame3, farmer_add_crop);
                  transaction.addToBackStack(null);
                  transaction.commit();
              }
              }
              else if(trader_id!=null)
            {
                Log.d("CheckingIds", trader_id);
                if(trader_field3!=null) {
                    trader_add_crop_after_login trader_crop = new trader_add_crop_after_login();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.traderFrame, trader_crop);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(trader_field2!=null) {
                    trader_add_fruit_after_login trader_fruit = new trader_add_fruit_after_login();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.traderFrame, trader_fruit);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else
                {
                    TraderProfile trader_add_crop = new TraderProfile();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.traderFrame, trader_add_crop);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
            else if(broker_id!=null)
            {
                BrokerProfile  brokerProfile = new BrokerProfile();
                FragmentTransaction transaction;
                FragmentManager manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.brokerFrame, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }

              /*  } else {
                    farmer_fruit_product farmer_fruit_product = new farmer_fruit_product();
                    FragmentTransaction transaction;
                    FragmentManager manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, farmer_fruit_product);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }*/
           /* else if(id.equals("trader")){
                if(field.equals("crop"))
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
            else{
                broker_signUP brokersignup=new broker_signUP();
                FragmentTransaction transaction;
                FragmentManager manager = getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, brokersignup);
                transaction.addToBackStack(null);
                transaction.commit();
            }*/

        }
        else{
            getActivity().finish();
        }


    }

    private void changtype(View view) {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void Search_location(View view) {
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));
                //   Log.d(TAG, "g0t location");
            } catch (Exception e) {

                Toast.makeText(getActivity(), getResources().getString(R.string.notfound), Toast.LENGTH_SHORT).show();
            }



        }
    }

    /*private void getMapOfBroker() {
        LatLng current = new LatLng(broker_lati, broker_longi);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(current).title("Marker"));
    }

    private void getMapOfTrader() {
        if (field.equals("crop")) {
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
    }*/
}
