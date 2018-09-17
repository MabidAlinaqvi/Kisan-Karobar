package com.example.root.formarsupport;


import android.*;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.Permission;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.FarmerProfile.namePerson;
import static com.example.root.formarsupport.FarmerProfile.setcardImage;
import static com.example.root.formarsupport.MapFragmentAfterLogin.lato;
import static com.example.root.formarsupport.MapFragmentAfterLogin.longo;


/**
 * A simple {@link Fragment} subclass.
 */
public class farmer_add_fruit_after_login extends Fragment {
    private EditText Product_name,Product_type,city,TEHIL,DISTT,area_price,plants,harvest,WaterSource;
    private String get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area_price,get_plants,get_harvest,get_WaterSource,uid,imageUrls;
    private View view;
    private ImageButton take_photo;
    private Button register_account,take_gallery;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,cardReference;
    FirebaseAuth mAuth;
    Toolbar mToolbar;
    static String add_field2;
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    Uri imageUri;
    StorageReference storageReference;
    GridView gridview;
    private ArrayList<Uri> mArrayFruitUri;
    ProgressDialog progressDialog;
    ArrayList<String> SaveUri;
    ArrayList<String> SaveImages;
    GoogleMap mMap;
    MapFragment mapFragment;
    String TAG="path_naqvi";
    static Double fruit_lati,fruit_longi;
    Double userlati=0.0,userlongi=0.0;
    LocationManager locationManager;
    Marker marker = null;
    SharedPreferences preferences;
    private farmer_fruit_infor fruit_user;
    int targetSdkVersion;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Permission permission;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_farmer_add_fruit_after_login, container, false);
        mToolbar=view.findViewById(R.id.fruit_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.add_fruit);
        gettingIds();
        add_field2="fruit";
        databaseReference=firebaseDatabase.getReference("Users").child(uid);
        cardReference=firebaseDatabase.getReference("cardview").child(uid);
        retrevingFShared();
        if(lato !=0.0 && longo !=0.0) {
            fruit_lati=lato;
            fruit_longi=longo;
        }
        else if(userlati!=0.0 && userlongi!=0.0)
        {
            fruit_lati=userlati;
            fruit_longi=userlongi;
        }
        CreateMaps();
        gridview.setAdapter(new f_Cropgrid_logIn(getActivity(),mArrayFruitUri));
        GalleryAndCamera();
        RegistrationButton();
        gettinValuesFromField();

        return view;
    }

    private void RegistrationButton() {
        register_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getResources().getString(R.string.savingProfile));
                progressDialog.show();
                gettinValuesFromField();
          //      ConvertUriToString();
               /* Farmer_signUp farmer_signUp=new Farmer_signUp();
                FragmentTransaction transaction;
                FragmentManager manager=getFragmentManager();
                transaction=manager.beginTransaction();
                transaction.replace(R.id.frame,farmer_signUp);
                transaction.addToBackStack(null);
                transaction.commit();*/
                SaveDataToFirebase();
            }
        });
    }

    private void SaveDataToFirebase() {

        SavefruitData();


        if(mArrayFruitUri.isEmpty()!=true)
        {
            Log.d("ArrayUri", mArrayFruitUri.size()+"");
            for(int j=0;j<mArrayFruitUri.size();j++)
            {
                UploadImages(mArrayFruitUri.get(j));
            }
        }
    }

    private void UploadImages(Uri uri) {
        imageUri=uri;
        storageReference= FirebaseStorage.getInstance().getReference().child("/images/"+System.currentTimeMillis()+"."+getImageExt(imageUri));
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageUrls=taskSnapshot.getDownloadUrl().toString();
                        SaveImages.add(imageUrls);
                        if(mArrayFruitUri!=null) {
                            if (mArrayFruitUri.size() == SaveImages.size()) {
                                databaseReference.child("images").setValue(SaveImages);
                              //  databaseReference.child("imagesUri").setValue(SaveUri);
                                progressDialog.dismiss();
                                SignedIn();
                            }
                        }
                    }
                });
    }

    private void SavefruitData() {

        fruit_user=new farmer_fruit_infor(get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area_price,get_plants,get_harvest,fruit_lati+"",fruit_longi+"");
        databaseReference.child("fruit").setValue(fruit_user);
        cardview_model_farmer_firstpage cardinfo = new cardview_model_farmer_firstpage(namePerson, "کسان", get_city, get_Product_name, "0", get_DISTT, "0", setcardImage, uid, "1");
        databaseReference.child("personal").child("field").setValue("fruit");
        cardReference.setValue(cardinfo);
        if(mArrayFruitUri.isEmpty()==true) {
            Log.d("EmptyArray", "yes");
            progressDialog.dismiss();
            SignedIn();
           /* try {
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }
    }

    private void SignedIn() {

        FarmerProfile farmerProfile=new FarmerProfile();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame3,farmerProfile);
        transaction.commit();
    }

    private void ConvertUriToString() {
        if(mArrayFruitUri!=null) {
            for (int i=0;i<mArrayFruitUri.size();i++)
            {
                SaveUri.add(mArrayFruitUri.get(i).toString());
            }
        }
    }

    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("farmerlogin_fruit", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("userproduct", "");   //will return false if doesn't exist "user_name"
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

    private void FromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }

    public void FromCamera(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);
    }


    @Override
    public void onPause() {
        super.onPause();
        saveInShared();
    }
    private void saveInShared(){
        gettinValuesFromField();
        preferences=getActivity().getSharedPreferences("farmerlogin_fruit",Context.MODE_PRIVATE);
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
        editor.putString("userlati", fruit_lati+"");
        editor.putString("userlongi", fruit_longi+"");
        editor.commit();
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
        SaveImages=new ArrayList<String>();
        take_gallery=view.findViewById(R.id.from_gallery);
        take_photo=view.findViewById(R.id.take_picture);
        register_account=view.findViewById(R.id.create_account);
        gridview=view.findViewById(R.id.gridview);
        firebaseDatabase= FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
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
        gridview.setAdapter(new f_Cropgrid_logIn(getActivity(),mArrayFruitUri));
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
                    ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    //   selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION);

                    return;
                }
                else if(lato==0.0 && longo==0.0 && userlati==0.0) {
                    //   Log.d("LatiAndLongi", "entered");
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        fruit_lati=location.getLatitude();
                        fruit_longi=location.getLongitude();
                      //  Log.d("code_verified",latitu+"   "+longitu);
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else {
                    Log.d("gettingCoordinate", fruit_lati+"   crop_lati");
                  //  Log.d("Coordinates", fruit_lati+"");
                   // Log.d("Coordinates", fruit_longi+"");
                    LatLng selected = new LatLng(fruit_lati, fruit_longi);
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
                        MapFragmentAfterLogin mapsFragment=new MapFragmentAfterLogin();
                        FragmentTransaction transaction;
                        FragmentManager manager=getFragmentManager();
                        transaction=manager.beginTransaction();
                        transaction.replace(R.id.frame3,mapsFragment);
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

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
