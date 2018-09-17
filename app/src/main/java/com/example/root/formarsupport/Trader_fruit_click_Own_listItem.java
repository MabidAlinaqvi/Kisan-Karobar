/*
package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.fMapFragmentLogIn.lati;
import static com.example.root.formarsupport.fMapFragmentLogIn.longi;


*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class Trader_fruit_click_Own_listItem extends Fragment  implements View.OnClickListener {
    View view;
    private static final int PROFILE_IMAGE = 3;
    int position;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    static Double t_latituLogin, t_logituLogin;
    static String Choose;
    Uri photoUri;
    EditText name, ntnNum, product, type, city, tehsil, distric, sackprice;
    ImageButton PersonCamera;
    ImageView SaveImage;
    Button saveData;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trader_fruit_click__own_list_item, container, false);
        gettingIds();
        MakeEditable();
        if (lati != 0.0 && longi != 0.0) {
            t_latituLogin = lati;
            t_logituLogin = longi;
        }
        initializingMaps();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initializingMaps() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        */
/**
         * it is showing the google map
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         *//*

        //  SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.personMap);
        //     Log.d(TAG, mapFragment+"");
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
                } else if (lati == 0.0 && longi == 0.0) {
                    Toast.makeText(getActivity(), "entered into current location", Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        t_latituLogin = location.getLatitude();
                        t_logituLogin = location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                } else {
                    LatLng selected = new LatLng(t_latituLogin, t_logituLogin);
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
                        //  chosen = "trader";
                        Choose = "fruit";
                        fMapFragmentLogIn mapsFragment = new fMapFragmentLogIn();
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

    private void gettingIds() {

        name = view.findViewById(R.id.personName);
        ntnNum = view.findViewById(R.id.personNtn);
        product = view.findViewById(R.id.personProduct);
        type = view.findViewById(R.id.personType);
        city = view.findViewById(R.id.personCity);
        tehsil = view.findViewById(R.id.personTehsil);
        distric = view.findViewById(R.id.personDistric);
        sackprice = view.findViewById(R.id.personPrice);
        SaveImage = view.findViewById(R.id.personImage);
        PersonCamera = view.findViewById(R.id.camera);
    }

    private void PicForProfile() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PROFILE_IMAGE);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        */
/*Log.d(TAG, "requestCode"+requestCode);
        Log.d(TAG, "resultCode"+resultCode);
        Log.d(TAG, "CAPTURE_IMADGE"+ CAPTURE_IMAGE);*//*

        if (resultCode == RESULT_OK && requestCode == PROFILE_IMAGE) {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap) bundle.get("data");
            photoUri = getImageUri(getActivity(), photo);
            // Log.d("CheckOut", photoUri+"");
            Picasso.with(getActivity())
                    .load(photoUri)
                    .resize(100, 100)
                    .centerCrop()
                    .into(SaveImage);
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("CheckOut", path + "   getting");
        return Uri.parse(path);
    }
    private void MakeEditable() {
        name.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        PersonCamera.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        ntnNum.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        product.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        type.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        city.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        tehsil.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        distric.setOnClickListener(Trader_fruit_click_Own_listItem.this);
        sackprice.setOnClickListener(Trader_fruit_click_Own_listItem.this);
    }

    @Override
    public void onClick(View view) {
        if (view == name) {
            name.setFocusableInTouchMode(true);
        } else if (view == ntnNum) {
            ntnNum.setFocusableInTouchMode(true);
        } else if (view == product) {
            product.setFocusableInTouchMode(true);
        } else if (view == type) {
            type.setFocusableInTouchMode(true);
        } else if (view == city) {
            city.setFocusableInTouchMode(true);
        } else if (view == tehsil) {
            tehsil.setFocusableInTouchMode(true);
        } else if (view == distric) {
            distric.setFocusableInTouchMode(true);
        } else if (view == sackprice) {
            sackprice.setFocusableInTouchMode(true);
        } else {
            PicForProfile();
        }
    }
}
*/
