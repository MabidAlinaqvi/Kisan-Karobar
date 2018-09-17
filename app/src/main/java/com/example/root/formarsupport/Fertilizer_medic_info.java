package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.MapsFragment.lati;
import static com.example.root.formarsupport.MapsFragment.longi;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fertilizer_medic_info extends Fragment {
    private EditText Product_name,pest_Name,city,TEHIL,DISTT,quantity,medic_Name;
    private MultiAutoCompleteTextView useRate,usage;
    private String get_Product_name,get_pest_type,get_city,get_TEHIL,get_DISTT,get_userRate,
            get_usage,get_quantity,get_medic_Name;
    private String dateIs;
    private ImageButton Picture_With_Camera;
    private Button From_gallery,create_account;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    Uri imageUri;
    StorageReference storageReference;
    GridView gridview;
    static fertilizer_pestcide_model user;
    SharedPreferences preferences;
    static ArrayList<Uri> mArrayfariti;
    String TAG="path_naqvi";
    static Double fari_lati,fari_longi;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    static String SaveIt;


View view;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_fertilizer_medic_info, container, false);
        gettingIds();
        retrevingFShared();
        if(lati !=0.0 && longi !=0.0) {
            fari_lati=lati;
            fari_longi=longi;
        }

        initializingMaps();
        gridview.setAdapter(new fertilizer_grid_view(getActivity()));
        UseGalleryAndCamera();
        CreateAccountButton();
        return view;
    }

    private void CreateAccountButton() {
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettingValuesFromField();
                Fertilizer_signUp fertilizer_signUp=new Fertilizer_signUp();
                FragmentTransaction transaction;
                FragmentManager manager=getFragmentManager();
                transaction=manager.beginTransaction();
                transaction.replace(R.id.frame,fertilizer_signUp);
                transaction.addToBackStack(null);
                transaction.commit();
                SaveIt="save";
                user=new fertilizer_pestcide_model(get_Product_name,get_pest_type,get_city,get_TEHIL,get_DISTT,
                        get_medic_Name,get_userRate,get_quantity,get_usage,fari_lati+"",fari_longi+"");
            }
        });

    }

    private void UseGalleryAndCamera() {
        From_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mArrayUri=new ArrayList<Uri>(); //so every time array should be intialized after clicking Gallery Button
                chooseFromGallery();
            }
        });
        Picture_With_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePhoto(view);
            }
        });
    }

    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initializingMaps() {

            locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            /**
             * it is showing the google map
             * https://www.youtube.com/watch?v=J3R4b-KauuI
             * https://www.youtube.com/watch?v=ovlHW6Y1eQM
             */
            mapFragment= (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            Log.d(TAG, mapFragment+"");
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
                    else if((lati==0.0 && longi==0.0) && (fari_lati==null && fari_longi==null)) {
                        Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            fari_lati=location.getLatitude();
                            fari_longi=location.getLongitude();
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            if (marker != null) {
                                marker.remove();
                            }
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                            marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));
                        }
                    }
                    else {
                        LatLng selected = new LatLng(fari_lati, fari_longi);
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

    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("fertilizer_medic", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("userproduct", "");   //will return false if doesn't exist "user_name"
        //  Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {
            String userproduct = preferences.getString("userproduct", "");
            String usertype = preferences.getString("usertype", "");
            String usercity = preferences.getString("usercity", "");
            String usertehsil = preferences.getString("usertehsil", "");
            String userdistric = preferences.getString("userdistric", "");
            String userarea = preferences.getString("userarea", "");
            String usersack = preferences.getString("usersack", "");
            String userquantity = preferences.getString("userquantity", "");
            String medicUse=preferences.getString("medicuse","");
            String userlati = preferences.getString("userlati", "");
            String userlongi = preferences.getString("userlongi", "");
            if(userlati != null && userlongi != null)
            {
               fari_lati=Double.parseDouble(userlati);
                fari_longi=Double.parseDouble(userlongi);
            }
            Product_name.setText(userproduct);
            pest_Name.setText(usertype);
            city.setText(usercity);
            TEHIL.setText(usertehsil);
            DISTT.setText(userdistric);
            medic_Name.setText(userarea);
            useRate.setText(usersack);
            quantity.setText(userquantity);
            usage.setText(medicUse);

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
     *
     * https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
     */
    private void saveInShared(){

        gettingValuesFromField();
        preferences=getActivity().getSharedPreferences("fertilizer_medic",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Toast.makeText(getActivity(),"crop trader data is saving",Toast.LENGTH_LONG).show();
        editor.putString("userproduct",get_Product_name);
        editor.putString("usertype",get_pest_type);
        editor.putString("usercity", get_city);
        editor.putString("usertehsil", get_TEHIL);
        editor.putString("userdistric", get_DISTT);
        editor.putString("userarea", get_medic_Name);
        editor.putString("usersack", get_userRate);
        editor.putString("userquantity", get_quantity);
        editor.putString("medicuse",get_usage);
        editor.putString("userlati", fari_lati+"");
        editor.putString("userlongi", fari_longi+"");
        editor.commit();
    }

    private void gettingValuesFromField() {
        get_Product_name=Product_name.getText().toString();
        get_pest_type=pest_Name.getText().toString();
        get_city=city.getText().toString();
        get_TEHIL=TEHIL.getText().toString();
        get_DISTT=DISTT.getText().toString();
        get_medic_Name=medic_Name.getText().toString();
        get_userRate=useRate.getText().toString();
        get_quantity=quantity.getText().toString();
        get_usage=usage.getText().toString();
    }

    private void gettingIds() {

        Product_name=view.findViewById(R.id.product);
        medic_Name=view.findViewById(R.id.medic_name);
        pest_Name=view.findViewById(R.id.pest_name);
        city=view.findViewById(R.id.city);
        TEHIL=view.findViewById(R.id.Thsil);
        DISTT=view.findViewById(R.id.District);
        useRate=view.findViewById(R.id.use_per_akr);
        quantity=view.findViewById(R.id.quantity);
        usage=view.findViewById(R.id.use_medic);
        create_account=view.findViewById(R.id.button2);
        mArrayfariti=new ArrayList<Uri>();
        Picture_With_Camera=view.findViewById(R.id.take_picture);
        From_gallery=view.findViewById(R.id.Chose_gallery);
        gridview = view.findViewById(R.id.gridview);
        firebaseDatabase= FirebaseDatabase.getInstance();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==CAPTURE_IMAGE)
        {
            Bundle bundle=data.getExtras();
            Bitmap photo= (Bitmap) bundle.get("data");
            Uri photoUri=getImageUri(getActivity(),photo);
            mArrayfariti.add(photoUri);

        }
        else{
            if(requestCode==PICK_IMAGES){

                if(resultCode==RESULT_OK){

                    if(data.getData()!=null){

                        imageUri=data.getData();
                        mArrayfariti.add(imageUri);

                    }else{
                        if(data.getClipData()!=null){
                            ClipData mClipData=data.getClipData();
                            for(int i=0;i<mClipData.getItemCount();i++){
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayfariti.add(uri);

                            }


                        }
                    }


                }

            }

        }
        gridview.setAdapter(new fertilizer_grid_view(getActivity()));
        Log.d(TAG, "image refreshed");
        gridview.setVisibility(View.VISIBLE);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public void TakePhoto(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);
    }

}
