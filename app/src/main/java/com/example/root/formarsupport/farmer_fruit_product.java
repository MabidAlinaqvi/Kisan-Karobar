package com.example.root.formarsupport;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
import java.io.IOException;
import java.io.Serializable;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.MapsFragment.lati;
import static com.example.root.formarsupport.MapsFragment.longi;


/**
 * A simple {@link Fragment} subclass.
 */
public class farmer_fruit_product extends Fragment{
private EditText Product_name,Product_type,city,TEHIL,DISTT,area_price,plants,harvest,WaterSource;
private String get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area_price,get_plants,get_harvest,get_WaterSource;
    private View view;
    private ImageButton take_photo;
    private Button register_account,take_gallery;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    Uri imageUri;
    StorageReference storageReference;
    GridView gridview;
    static ArrayList<Uri> mArrayFruitUri;
    ArrayList<String> SaveUri;
    GoogleMap mMap;
    MapFragment mapFragment;
    String TAG="path_naqvi";
    static Double latitu,longitu;
    Double userlati=0.0,userlongi=0.0;
    LocationManager locationManager;
    Marker marker = null;
    SharedPreferences preferences;
    static farmer_fruit_infor fruit_user;
    int targetSdkVersion;
    Permission permission;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     view=inflater.inflate(R.layout.fragment_farmer_fruit_product,container,false);
     gettingIds();
     retrevingFShared();
     //   Log.d("LatiAndLongi", userlati+"   "+userlongi);
        if(lati !=0.0 && longi !=0.0) {
            latitu=lati;
            longitu=longi;
        }
        else if(userlati!=0.0 && userlongi!=0.0)
        {
            latitu=userlati;
            longitu=userlongi;
        }
        CreateMaps();
        gridview.setAdapter(new f_fruitGridViewAdapter(getActivity()));
        /*Log.d("SaveValues", mArrayFruitUri.size()+"  arraylist size");
       // mArrayFruitUri.clear();
        if(mArrayFruitUri.size()>0)
        {

            gridview.setVisibility(View.VISIBLE);
        }*/
        GalleryAndCamera();
        RegistrationButton();
     gettinValuesFromField();

        return view;
    }

    private void RegistrationButton(){
        register_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettinValuesFromField();
                Farmer_signUp farmer_signUp=new Farmer_signUp();
                FragmentTransaction transaction;
                FragmentManager manager=getFragmentManager();
                transaction=manager.beginTransaction();
                transaction.replace(R.id.frame,farmer_signUp);
                transaction.addToBackStack(null);
                transaction.commit();
                fruit_user=new farmer_fruit_infor(get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area_price,get_plants,get_harvest,latitu+"",longitu+"");
            }
        });
    }

    private void GalleryAndCamera() {
        take_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mArrayUri=new ArrayList<Uri>(); //so every time array should be intialized after clicking Gallery Button
                FromGallery();
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FromCamera(view);
            }
        });
    }

    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("farmer_fruit", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("userproduct", "");   //will return false if doesn't exist "user_name"
        //  Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {

            String userproduct = preferences.getString("userproduct", "");
            String usertype = preferences.getString("usertype", "");
            String usercity = preferences.getString("usercity", "");
            String usertehsil = preferences.getString("usertehsil", "");
            String userdistric = preferences.getString("userdistric", "");
            String userarea = preferences.getString("userarea", "");
            String userplants = preferences.getString("userplants", "");
            String userharvest = preferences.getString("userharvest", "");
            String userlatiIs=preferences.getString("userlati","");
            String userlongiIs=preferences.getString("userlongi","");
            if(userlatiIs!=null ||userlongiIs!=null) {
                userlati = Double.valueOf(preferences.getString("userlati", ""));
                userlongi = Double.valueOf(preferences.getString("userlongi", ""));
            }
            /* this is working fine
            Set<String> set = preferences.getStringSet("key", null);

            if(set !=null) {
              //  Log.d("dataIs", set.size()+"  sizeIs");
               // int i=0;
                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String data=it.next();
                    Uri uriIs=Uri.parse(data);
                    mArrayFruitUri.add(uriIs);

                }
                if(mArrayFruitUri!=null)
                {
                    gridview.setAdapter(new f_fruitGridViewAdapter(getActivity()));
                    gridview.setVisibility(View.VISIBLE);

                }
            }*/

            //
            //ArrayList<String> array=set.toArray(new ArrayList<String>);
            /*try {
                SaveUri = (ArrayList<Uri>) ObjectSerializer.deserialize(preferences.getString("array", ObjectSerializer.serialize(new ArrayList<Uri>())));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for(int i=0;i<SaveUri.size();i++)
            {
                Log.d("dataIs", SaveUri.get(i)+"");
            }*/
            /*Set<String> set = preferences.getStringSet("imageArray", null);
            if (set != null) {
                ArrayList<String> array= new ArrayList<>(set);
                for(int k=0;k<array.size();k++)
                {
                    Uri uri=Uri.parse(array.get(k));
                    mArrayFruitUri.add(uri);
                   // Log.d("SaveValues", mArrayFruitUri.size()+"");
                }
                set.clear();
            }
            else{
                Toast.makeText(getActivity(),"set is null",Toast.LENGTH_SHORT).show();
            }*/
            Product_name.setText(userproduct);
            Product_type.setText(usertype);
            city.setText(usercity);
            TEHIL.setText(usertehsil);
            DISTT.setText(userdistric);
            area_price.setText(userarea);
            plants.setText(userplants);
            harvest.setText(userharvest);
        } else {
            Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        saveInShared();
    }
    private void saveInShared(){
        gettinValuesFromField();
        preferences=getActivity().getSharedPreferences("farmer_fruit",Context.MODE_PRIVATE);
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
        editor.putString("userlati", latitu+"");
        editor.putString("userlongi", longitu+"");
        /*
        This is working fine
        String uri;
        for(int i=0;i<mArrayFruitUri.size();i++)
        {
            uri=mArrayFruitUri.get(i).toString();
            SaveUri.add(uri);
        }
        Set<String> set = new HashSet<String>();
        set.addAll(SaveUri);
        Log.d("dataIs", set.size()+"");
        editor.putStringSet("key",set);*/
       // scoreEditor.commit();
       /* try {
            editor.putString("array", ObjectSerializer.serialize(mArrayFruitUri));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        editor.commit();
        //saving array to sharedprefrences
        /*Set<String> set = new HashSet<>();
        for(int l=0;l<mArrayFruitUri.size();l++)
        {
            set.add(mArrayFruitUri.get(l)+"");
            //Log.d("SaveValues", set.size()+"");
        }
        Log.d("SaveValues", set.size()+" set size");
   //     mArrayFruitUri.clear();
        editor.putStringSet("imageArray", set);*/

    }
    private void FromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*Log.d(TAG, "requestCode"+requestCode);
        Log.d(TAG, "resultCode"+resultCode);
        Log.d(TAG, "CAPTURE_IMADGE"+ CAPTURE_IMAGE);*/

        if(resultCode==RESULT_OK && requestCode==CAPTURE_IMAGE)
        {
            Bundle bundle=data.getExtras();
            Bitmap photo= (Bitmap) bundle.get("data");
            Uri photoUri=getImageUri(getActivity(),photo);
            mArrayFruitUri.add(photoUri);

        }
        else{
            if(requestCode==PICK_IMAGES){

                if(resultCode==RESULT_OK){

                    if(data.getData()!=null){

                        imageUri=data.getData();
                        mArrayFruitUri.add(imageUri);

                    }else{
                        if(data.getClipData()!=null){
                            ClipData mClipData=data.getClipData();
                            for(int i=0;i<mClipData.getItemCount();i++){
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayFruitUri.add(uri);

                            }
                        }
                    }
                }
            }
        }
        Log.d("ArraySize", mArrayFruitUri.size()+"  from start");
        gridview.setAdapter(new f_fruitGridViewAdapter(getActivity()));
        //Log.d(TAG, "image refreshed");
        gridview.setVisibility(View.VISIBLE);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
                     //   selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION);

                        return;
                    }
               else if(lati==0.0 && longi==0.0 && userlati==0.0) {
                     //   Log.d("LatiAndLongi", "entered");
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitu=location.getLatitude();
                        longitu=location.getLongitude();
                        Log.d("code_verified",latitu+"   "+longitu);
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else if(userlongi!=0.0 || lati!=0.0) {
                    LatLng selected = new LatLng(latitu, longitu);
                    if (marker != null) {
                        marker.remove();
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected, 17));
                    marker = mMap.addMarker(new MarkerOptions().position(selected).title("marked"));
                }
              //  mMap.setMyLocationEnabled(true);

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

    private void gettinValuesFromField() {
        get_Product_name=Product_name.getText().toString();
        get_Product_type=Product_type.getText().toString();
        get_city=city.getText().toString();
        get_TEHIL=TEHIL.getText().toString();
        get_DISTT=DISTT.getText().toString();
        get_area_price=area_price.getText().toString();
        get_plants=plants.getText().toString();
        get_harvest=harvest.getText().toString();
        //get_WaterSource=WaterSource.getText().toString();

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
        mArrayFruitUri=new ArrayList<Uri>();
        SaveUri=new ArrayList<String>();
        //WaterSource=view.findViewById(R.id.source);
        take_gallery=view.findViewById(R.id.from_gallery);
        take_photo=view.findViewById(R.id.take_picture);
        register_account=view.findViewById(R.id.create_account);
        gridview=view.findViewById(R.id.gridview);

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void FromCamera(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);
    }

}
