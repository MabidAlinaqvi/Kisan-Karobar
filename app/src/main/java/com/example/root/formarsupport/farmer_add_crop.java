package com.example.root.formarsupport;


import android.*;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.MapsFragment.lati;
import static com.example.root.formarsupport.MapsFragment.longi;


public class farmer_add_crop extends Fragment {
    private EditText Product_name,Product_type,city,TEHIL,DISTT,area,sack_price,quantity,HarvestTime;
    private String get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area,
            get_sack_price,get_quantity,get_HarvestTime;
    private String dateIs;
    private View view;
    private ImageButton Picture_With_Camera;
    private Button From_gallery,create_account;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    Uri imageUri;
    StorageReference storageReference;
    GridView gridview;
    static farmer_crop_infor user;
    SharedPreferences preferences;
    static ArrayList<Uri> mArrayUri;
    String TAG="path_naqvi";
    static Double latitude,longitude;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_farmer_add_crop,container,false);
        gettingIds();
        retrevingFShared();
        Log.d(TAG, lati+"     "+longi);
        if(lati !=0.0 && longi !=0.0) {
            latitude=lati;
            longitude=longi;
        }

        initializingMaps();
//        IfLatiAndLongINotNull();
        if(mArrayUri.size()>0)
        {
            gridview.setVisibility(View.VISIBLE);
            gridview.setAdapter(new f_cropGridViewAdapter(getActivity()));
        }
        else{
            gridview.setAdapter(new f_cropGridViewAdapter(getActivity()));
        }
        UseGalleryAndCamera();
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
                //makeJson();
                gettingValuesFromField();
                Farmer_signUp farmer_signUp=new Farmer_signUp();
                FragmentTransaction transaction;
                FragmentManager manager=getFragmentManager();
                transaction=manager.beginTransaction();
                transaction.replace(R.id.frame,farmer_signUp);
                transaction.addToBackStack(null);
                transaction.commit();
                user=new farmer_crop_infor(get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,
                        get_area,get_sack_price,get_quantity,latitude+"",longitude+"");
               // uploadImageOnFirebaeStorage();
            }
        });
    }

 /*   private void uploadImageOnFirebaeStorage() {
        if()
    }*/

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
                else if((lati==0.0 && longi==0.0) && (latitude==null && longitude==null)) {
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude=location.getLatitude();
                        longitude=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else {
                    LatLng selected = new LatLng(latitude, longitude);
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
    /**
     * Retreive value from file
     */
    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("farmer_crop", Context.MODE_PRIVATE);
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
            String userlati = preferences.getString("userlati", "");
            String userlongi = preferences.getString("userlongi", "");
            if(userlati != null && userlongi != null)
            {
                latitude=Double.parseDouble(userlati);
                longitude=Double.parseDouble(userlongi);
            }
            /*Set<String> set = preferences.getStringSet("imageArray", null);
            if (set != null) {
                ArrayList<String> array= new ArrayList<>(set);
                for(int k=0;k<array.size();k++)
                {
                    Uri uri=Uri.parse(array.get(k));
                    mArrayUri.add(uri);
                 //   Log.d("SaveValues", mArrayUri.size()+"");
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
            area.setText(userarea);
            sack_price.setText(usersack);
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
     *
     * https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
     */
    private void saveInShared(){
        gettingValuesFromField();
        preferences=getActivity().getSharedPreferences("farmer_crop",Context.MODE_PRIVATE);
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
        editor.putString("userlati", latitude+"");
        editor.putString("userlongi", longitude+"");
        //saving array to sharedprefrences
       /* Set<String> set = new HashSet<>();
        for(int l=0;l<mArrayUri.size();l++)
        {
            set.add(mArrayUri.get(l)+"");
            //Log.d("SaveValues", set.size()+"");
        }
        editor.putStringSet("imageArray", set);*/
        editor.commit();
    }

    /**
     * https://stackoverflow.com/questions/27372106/pick-multiple-images-from-gallery
     */
    private void chooseFromGallery() {
        /*Intent Gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Gallery, PICK_IMAGE);*/
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }


  /*  private void Store_data_ON_firebase() {
            gettingValuesFromField();

    }*/

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
     //   harvest=view.findViewById(R.id.harvast);
     //   WaterSource=view.findViewById(R.id.source);
        create_account=view.findViewById(R.id.button2);
        mArrayUri=new ArrayList<Uri>();
      //  picture_path=view.findViewById(R.id.imagePath);
        Picture_With_Camera=view.findViewById(R.id.take_picture);
        From_gallery=view.findViewById(R.id.Chose_gallery);
        gridview = view.findViewById(R.id.gridview);
       // land_proof=view.findViewById(R.id.Land_proof);
        firebaseDatabase=FirebaseDatabase.getInstance();

    }

    /**
     * https://stackoverflow.com/questions/12555420/how-to-get-a-uri-object-from-bitmap
     * @param requestCode
     * @param resultCode
     * @param data
     */

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
            mArrayUri.add(photoUri);

        }
        else{
            if(requestCode==PICK_IMAGES){

                if(resultCode==RESULT_OK){

                    if(data.getData()!=null){

                        imageUri=data.getData();
                        mArrayUri.add(imageUri);

                    }else{
                        if(data.getClipData()!=null){
                            ClipData mClipData=data.getClipData();
                            for(int i=0;i<mClipData.getItemCount();i++){
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayUri.add(uri);

                            }


                        }
                    //picture_path.setText(getResources().getString(R.string.imagesSelected));
                    }
                    /*for(int k=0;k<mArrayUri.size();k++)
                    {
                        ImageView image=new ImageView(getActivity());

                    }*/

                   /* String[] projection = { MediaStore.MediaColumns.DATA,
                            MediaStore.MediaColumns.DISPLAY_NAME };
                    for(int j=0;j<mArrayUri.size();j++) {
                        Cursor cursor = getActivity().managedQuery(mArrayUri.get(j), projection, null, null, null);
                        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    }*/

                }

            }

        }
        gridview.setAdapter(new f_cropGridViewAdapter(getActivity()));
        Log.d(TAG, "image refreshed");
        gridview.setVisibility(View.VISIBLE);

    }
   /* private void makeJson(){
        Map<String, JSONObject> userMap= new HashMap<String, JSONObject>();
        Log.i("size",Integer.toString(mArrayUri.size()));
        JSONObject jsonObject = new JSONObject();
        try{
            for (int i=0;i<mArrayUri.size();i++){
                jsonObject.put(Integer.toString(i),mArrayUri.get(i).toString());
            }

        }catch (Exception e){}
        databaseReference=firebaseDatabase.getReference();
        userMap.put("myUser", jsonObject);
        databaseReference.child("crop").setValue(userMap);
    }
*/

    /**
     * https://stackoverflow.com/questions/12555420/how-to-get-a-uri-object-from-bitmap
     * @param inContext
     * @param inImage
     * @return
     */
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
