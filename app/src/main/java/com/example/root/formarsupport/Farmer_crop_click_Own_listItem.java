/*
package com.example.root.formarsupport;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.fMapFragmentLogIn.lati;
import static com.example.root.formarsupport.fMapFragmentLogIn.longi;

*/
/**
 * https://stackoverflow.com/questions/12670719/delete-on-longpress
 *//*

public class Farmer_crop_click_Own_listItem extends Fragment implements View.OnClickListener {
    View view;
    GridView gridview;
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    private static final int PROFILE_IMAGE=3;
    static ArrayList<Uri> ArrayUriLogIn;
    Uri imageUri;
    int position;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    Marker marker = null;
    static Double f_latiLogin,f_logiLogin;
    static String chosen,Former_choice;
    Uri photoUri;
EditText name,phone,product,type,city,tehsil,distric,sackprice,productquant,area,harvesting;
ImageButton FromCamera,PersonCamera;
ImageView SaveImage;
Button saveData,FromGallery;
AlertDialog.Builder alertDialogBuilder;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_farmer_crop_click__own_list_item,container,false);
        gettingIds();
        MakeEditable();
        if(lati !=0.0 && longi !=0.0) {
            f_latiLogin=lati;
            f_logiLogin=longi;
        }
        initializingMaps();
        gridview.setAdapter(new f_Cropgrid_logIn(getActivity()));
        UseGalleryAndCamera();
        gridItemClicked();
        return view;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initializingMaps() {
        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        */
/**
         * it is showing the google map
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         *//*

        //  SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment= (MapFragment) getChildFragmentManager().findFragmentById(R.id.personMap);
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
                    ActivityCompat.requestPermissions(getActivity(),new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    return;
                }
                else if(lati==0.0 && longi==0.0) {
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        f_latiLogin=location.getLatitude();
                        f_logiLogin=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else {
                    LatLng selected = new LatLng(f_latiLogin,f_logiLogin);
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
                        chosen="farmer";
                        Former_choice="crop";
                        fMapFragmentLogIn mapsFragment=new fMapFragmentLogIn();
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
    private void gridItemClicked() {
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                position=i;
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setTitle("Delete");
                alertDialogBuilder.setMessage(getResources().getString(R.string.delete));
               alertDialogBuilder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       ArrayUriLogIn.remove(position);
                       gridview.setAdapter(new f_Cropgrid_logIn(getActivity()));
                   }
               });
               alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
               alertDialogBuilder.show();
                return false;
            }
        });
    }

    private void MakeEditable() {
       // EditText name,phone,product,type,city,tehsil,distric,sackprice,productquant,area,harvesting;
//        editname.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        name.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        PersonCamera.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        phone.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        product.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        type.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        city.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        tehsil.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        distric.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        sackprice.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        productquant.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        area.setOnClickListener(Farmer_crop_click_Own_listItem.this);
        harvesting.setOnClickListener(Farmer_crop_click_Own_listItem.this);
    }

    private void gettingIds() {

        name=view.findViewById(R.id.personName);
        phone=view.findViewById(R.id.personPhone);
        product=view.findViewById(R.id.personProduct);
        type=view.findViewById(R.id.personType);
        city=view.findViewById(R.id.personCity);
        tehsil=view.findViewById(R.id.personTehsil);
        distric=view.findViewById(R.id.personDistric);
        sackprice=view.findViewById(R.id.personPrice);
        productquant=view.findViewById(R.id.personQuant);
        area=view.findViewById(R.id.personArea);
        harvesting=view.findViewById(R.id.personTime);
        gridview = view.findViewById(R.id.gridview);
        FromGallery=view.findViewById(R.id.Chose_gallery);
        FromCamera=view.findViewById(R.id.take_picture);
        ArrayUriLogIn=new ArrayList<Uri>();
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        SaveImage=view.findViewById(R.id.personImage);
        PersonCamera=view.findViewById(R.id.camera);
       */
/* editname=view.findViewById(R.id.editName);
        editphone=view.findViewById(R.id.editPhone);
        edittype=view.findViewById(R.id.editType);
        editcity=view.findViewById(R.id.editCity);
        edittehsil=view.findViewById(R.id.editTehsil);
        editdistric=view.findViewById(R.id.editDistric);
        editsacprice=view.findViewById(R.id.editPrice);
        editproductquant=view.findViewById(R.id.editProduct);
        editarea=view.findViewById(R.id.editArea);
        editharvesting=view.findViewById(R.id.editTime);
        editproduct=view.findViewById(R.id.editProduct);*//*

    }
    @Override
    public void onClick(View view) {
        if(view==name)
        {
            name.setFocusableInTouchMode(true);
        }
        else if(view==phone)
        {
            phone.setFocusableInTouchMode(true);
        }
        else if(view==product)
        {
            city.setFocusableInTouchMode(true);
        }
        else if(view==type)
        {
            type.setFocusableInTouchMode(true);
        }
        else if(view==city)
        {
            city.setFocusableInTouchMode(true);
        }
        else if(view==tehsil)
        {
            tehsil.setFocusableInTouchMode(true);
        }
        else if(view==distric)
        {
            distric.setFocusableInTouchMode(true);
        }
        else if(view==sackprice)
        {
            sackprice.setFocusableInTouchMode(true);
        }
        else if(view==productquant)
        {
            productquant.setFocusableInTouchMode(true);
        }
        else if(view==area)
        {
            area.setFocusableInTouchMode(true);
        }
        else if(view==PersonCamera){
            PicForProfile();
        }
        else{
            harvesting.setFocusableInTouchMode(true);
        }
    }

    private void PicForProfile() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,PROFILE_IMAGE);

    }

    private void UseGalleryAndCamera() {
        FromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mArrayUri=new ArrayList<Uri>(); //so every time array should be intialized after clicking Gallery Button
                chooseFromGallery();
            }
        });
        FromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(getActivity(),new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},1);
                    return;
                }
                else {
                    TakePhoto(view);
                }
            }
        });
    }
    private void chooseFromGallery() {
        */
/*Intent Gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Gallery, PICK_IMAGE);*//*


        Intent intent = new Intent();
        intent.setType("image*/
/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        */
/*Log.d(TAG, "requestCode"+requestCode);
        Log.d(TAG, "resultCode"+resultCode);
        Log.d(TAG, "CAPTURE_IMADGE"+ CAPTURE_IMAGE);*//*

        if(resultCode==RESULT_OK && requestCode==PROFILE_IMAGE){
            Bundle bundle=data.getExtras();
            Bitmap photo= (Bitmap) bundle.get("data");
            photoUri=getImageUri(getActivity(),photo);
           // Log.d("CheckOut", photoUri+"");
            Picasso.with(getActivity())
                    .load(photoUri)
                    .resize(100,100)
                    .centerCrop()
                    .into(SaveImage);
        }
        else if(resultCode==RESULT_OK && requestCode==CAPTURE_IMAGE)
        {
            Bundle bundle=data.getExtras();
            Bitmap photo= (Bitmap) bundle.get("data");
            Uri photoUri=getImageUri(getActivity(),photo);
            Log.d("CheckOut", photoUri+"");
            ArrayUriLogIn.add(photoUri);
        }
        else{
            if(requestCode==PICK_IMAGES){

                if(resultCode==RESULT_OK){

                    if(data.getData()!=null){

                        imageUri=data.getData();
                        ArrayUriLogIn.add(imageUri);

                    }else{
                        if(data.getClipData()!=null){
                            ClipData mClipData=data.getClipData();
                            for(int i=0;i<mClipData.getItemCount();i++){
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                ArrayUriLogIn.add(uri);

                            }
                        }
                    }

                }

            }

        }
        gridview.setAdapter(new f_Cropgrid_logIn(getActivity()));
  //      Log.d(TAG, "image refreshed");
        if(requestCode!=PROFILE_IMAGE)
            gridview.setVisibility(View.VISIBLE);

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
      */
/*  Log.d("CheckOut", p
                ath+"   getting");*//*

        return Uri.parse(path);
    }
    public void TakePhoto(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);
    }


}
*/
