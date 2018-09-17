package com.example.root.formarsupport;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.MapFragmentAfterLogin.lato;
import static com.example.root.formarsupport.MapFragmentAfterLogin.longo;
import static com.example.root.formarsupport.card_view_adapter_farmer_firstpage.visitData;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrokerProfile extends Fragment {
    private EditText name,NTN,phone_num,product_name,city,TEHSIL,DISTT,product_quant;
    private String get_name,get_NTN,get_phone_num,get_product_name,get_city,
            get_TEHSIL,get_DISTT,get_product_quant,mVerificationId,get_brokerName,get_brokerPhone,get_brokerNtn;
    private String currentUser;
    private Button create_account;
    private TextView detailIs,broker_name,broker_ntn,broker_phone,broker_incomplete,code_error,Resendcode,UploadText;
    private ImageView upload_image,editInformation;
    private CircleImageView ProfileImage;
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    private Toolbar mToolbar;
    private View view;
    RelativeLayout layout;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;//this is very important to declare to dismiss the dialogue
    static Double brok_lati,brok_longi;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceCard;
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    String imageUrl="",online_User;
    Marker marker = null;
    SharedPreferences preferences;
    Uri photoUri,imageUri,saveUri;
    ArrayList<String> uidArray;
    static String broker_id;
    int k=0,i=0;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private DatabaseReference friendReference,personaldatabase;
    private broker_infor brokerinfo;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_broker_profile, container, false);
        mToolbar=view.findViewById(R.id.broker_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.farmerProfile));
        gettingIds();
        online_User=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String showDetail=detailIs.getText().toString();
        uidArray=new ArrayList<>();
        UnderlineText(showDetail);
        builder = new AlertDialog.Builder(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        retrevingFShared();
        retrevingDataFromFirebase();
        if(lato !=0.0 && longo !=0.0) {
            brok_lati=lato;
            brok_longi=longo;
        }
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettingValues();
                if(!TextUtils.isEmpty(get_brokerName) && !TextUtils.isEmpty(get_brokerNtn)) {
                    progressDialog.setMessage("please wati");
                    progressDialog.show();
                    SendToFirebase();
                }
                else
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.namAndNtn), Toast.LENGTH_SHORT).show();
                }
            }
        });
        detailIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(k==0) {
                    layout.setVisibility(View.VISIBLE);
                    detailIs.setText("تفصیل چھپائیں");
                    String showDetail=detailIs.getText().toString();
                    UnderlineText(showDetail);
                    k=k+1;
                }
                else{
                    layout.setVisibility(View.GONE);
                    detailIs.setText(getResources().getString(R.string.showdetail));
                    String showDetail=detailIs.getText().toString();
                    UnderlineText(showDetail);
                    k=0;
                }
            }
        });
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageFrom();
            }
        });
        editInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeClickable();
            }
        });

        return view;

    }

    private void retrevingDataFromFirebase() {

        personaldatabase=firebaseDatabase.getReference("Users").child(online_User).child("personal");
        personaldatabase.addValueEventListener(new ValueEventListener() {
            @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                brokerinfo=dataSnapshot.getValue(broker_infor.class);
                broker_id=brokerinfo.getId();
                personalValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void personalValues() {
        //name,NTN,phone_num,pass,product_name,city,TEHSIL,DISTT,product_quant,comment,commentNo
        name.setText(brokerinfo.getName());
        NTN.setText(brokerinfo.getNTN());
        product_name.setText(brokerinfo.getProduct_name());
        city.setText(brokerinfo.getCity());
        TEHSIL.setText(brokerinfo.getTEHSIL());
        product_quant.setText(brokerinfo.getProduct_quant());
        brok_lati=Double.parseDouble(brokerinfo.getLatitude());
        brok_longi=Double.parseDouble(brokerinfo.getLongitude());
        if(!brokerinfo.getProfile().equals(""))
        {
            Picasso.with(getActivity())
                    .load(brokerinfo.getProfile())
                    .placeholder(R.drawable.profile1)
                    .into(ProfileImage);
        }
        else
        {
            ProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
        }
        Maps();
    }

    private void makeClickable() {

        name.setFocusableInTouchMode(true);
        name.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        NTN.setFocusableInTouchMode(true);
        NTN.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        product_name.setFocusableInTouchMode(true);
        product_name.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        city.setFocusableInTouchMode(true);
        city.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        TEHSIL.setFocusableInTouchMode(true);
        TEHSIL.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        DISTT.setFocusableInTouchMode(true);
        DISTT.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        product_quant.setFocusableInTouchMode(true);
        product_quant.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        upload_image.setVisibility(View.VISIBLE);
        UploadText.setVisibility(View.VISIBLE);



    }

    private void UnderlineText(String underline) {
        SpannableString content = new SpannableString(underline);
        content.setSpan(new UnderlineSpan(), 0, underline.length(), 0);
        detailIs.setText(content);
    }

    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("broker_signUp", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("brokername", "");   //will return false if doesn't exist "user_name"
        Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {

            String username = preferences.getString("brokername", "");
            String userntn = preferences.getString("brokerntn", "");
            String userphone = preferences.getString("brokerphone", "");
            String userproduct = preferences.getString("brokerproduct", "");
            String usercity = preferences.getString("brokercity", "");
            String usertehsil = preferences.getString("brokertehsil", "");
            String userdistt = preferences.getString("brokerdistt", "");
            String userquantity = preferences.getString("brokerquant", "");
            String userlati = preferences.getString("brokerlati", "");
            String userlongi = preferences.getString("brokerlongi", "");
            if(userlati != null && userlongi != null)
            {
                brok_lati=Double.valueOf(userlati);
                brok_longi=Double.valueOf(userlongi);
            }
            name.setText(username);
            NTN.setText(userntn);
            phone_num.setText(userphone);
            product_name.setText(userproduct);
            city.setText(usercity);
            TEHSIL.setText(usertehsil);
            DISTT.setText(userdistt);
            product_quant.setText(userquantity);


        } else {
            Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendToFirebase() {
        //   Uri UploadUri;
        currentUser=firebaseAuth.getCurrentUser().getUid();
        Log.d("checking_Data", currentUser);
        databaseReference=firebaseDatabase.getReference("Users").child(currentUser).child("personal");
        databaseReferenceCard=firebaseDatabase.getReference("cardview").child(currentUser);
        Toast.makeText(getActivity(),"data is about so save",Toast.LENGTH_LONG).show();
        Log.d("checking_Data", photoUri+"    photo"+"      "+imageUri+"   image");
        if(imageUri==null && photoUri==null) {
            imageUrl = "";
            if(!brokerinfo.getProfile().equals("")) {
                broker_infor info = new broker_infor(get_name, get_NTN, get_phone_num, "", get_product_name, get_city, get_TEHSIL, get_DISTT, get_product_quant, "broker", brok_lati + "", brok_longi + "",brokerinfo.getProfile());
                databaseReference.setValue(info);
            }
            else
            {
                broker_infor info = new broker_infor(get_name, get_NTN, get_phone_num, "", get_product_name, get_city, get_TEHSIL, get_DISTT, get_product_quant, "broker", brok_lati + "", brok_longi + "",imageUrl);
                databaseReference.setValue(info);
            }

            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(get_name,"بروکر",get_city,get_product_name,"0",get_DISTT,"0",imageUrl,currentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
            progressDialog.dismiss();
            SignedIn();
        }
        else{
            if(photoUri==null) {
                Log.d("PhotoImage", imageUri+" image");
                UploadImage(imageUri);
            }
            if(imageUri==null){
                Log.d("PhotoImage", photoUri+" photo");
                UploadImage(photoUri);
            }
            Toast.makeText(getActivity(),"data Is saving",Toast.LENGTH_SHORT).show();
            broker_infor info=new broker_infor(get_name,get_NTN,get_phone_num,"",get_product_name,get_city,get_TEHSIL,get_DISTT,get_product_quant,"broker",brok_lati+"",brok_longi+"",imageUrl);
            databaseReference.setValue(info);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(get_name,"بروکر",get_city,get_product_name,"0",get_DISTT,"0",imageUrl,currentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
        }
        //


    }

    private void SignedIn() {
        Toast.makeText(getActivity(),getResources().getString(R.string.data_saving),Toast.LENGTH_LONG).show();
    }
/*    private void TakePhoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);
    }

    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);

    }*/

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
        preferences=getActivity().getSharedPreferences("broker_signUp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Toast.makeText(getActivity(),"broker data is saving",Toast.LENGTH_LONG).show();
        editor.putString("brokername",get_name);
        editor.putString("brokerntn",get_NTN);
        editor.putString("brokerphone",get_phone_num);
        editor.putString("brokerproduct",get_product_name);
        editor.putString("brokercity", get_city);
        editor.putString("brokertehsil", get_TEHSIL);
        editor.putString("brokerdistt", get_DISTT);
        editor.putString("brokerquant", get_product_quant);
        editor.putString("brokerlati", brok_lati+"");
        editor.putString("brokerlongi", brok_longi+"");
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void Maps() {
        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        /**
         * it is showing the google map
         *
         * https://www.youtube.com/watch?v=J3R4b-KauuI
         * https://www.youtube.com/watch?v=ovlHW6Y1eQM
         */
        //  SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment= (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //  Log.d(TAG, mapFragment+"");
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
                else if((lato==0.0 && longo==0.0) && (brok_lati==null && brok_longi==null)) {
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        brok_lati=location.getLatitude();
                        brok_longi=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else {
                    LatLng selected = new LatLng(brok_lati, brok_longi);
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
                        MapFragmentAfterLogin mapsFragment=new MapFragmentAfterLogin();
                        FragmentTransaction transaction;
                        FragmentManager manager=getFragmentManager();
                        transaction=manager.beginTransaction();
                        transaction.replace(R.id.brokerFrame,mapsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });
            }

        });

    }

    private void gettingIds() {
        name=view.findViewById(R.id.name);
        NTN=view.findViewById(R.id.NTN);
        phone_num=view.findViewById(R.id.phone);
        product_name=view.findViewById(R.id.product);
        city=view.findViewById(R.id.city);
        TEHSIL=view.findViewById(R.id.Thsil);
        DISTT=view.findViewById(R.id.District);
        product_quant=view.findViewById(R.id.quantity);
        create_account=view.findViewById(R.id.sign_Up);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        layout=view.findViewById(R.id.relative);
        detailIs=view.findViewById(R.id.ShowDetail);
        upload_image=view.findViewById(R.id.UploadImage);
        ProfileImage=view.findViewById(R.id.ProfileIs);
        broker_name=view.findViewById(R.id.WritebrokerName);
        broker_phone=view.findViewById(R.id.invalidbrokerPhone);
        broker_ntn=view.findViewById(R.id.Writebrokerntn);
        broker_incomplete=view.findViewById(R.id.brokerError);
        code_error=view.findViewById(R.id.invalidcode);
        Resendcode=view.findViewById(R.id.resendCode);
        editInformation=view.findViewById(R.id.personbrokeredit);
        UploadText=view.findViewById(R.id.uploadimageText);
    }

    private void gettingValues(){
        get_name=name.getText().toString();
        get_NTN=NTN.getText().toString();
        get_phone_num=phone_num.getText().toString();
        get_product_name=product_name.getText().toString();
        get_city=city.getText().toString().toString();
        get_TEHSIL=TEHSIL.getText().toString();
        get_DISTT=DISTT.getText().toString();
        get_brokerName=broker_name.getText().toString();
        get_brokerPhone=broker_phone.getText().toString();
        get_brokerNtn=broker_ntn.getText().toString();

    }

    ImageView cam,gal;
    private void UploadImageFrom() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.profileimageafterlogin,null);
        gal=view.findViewById(R.id.user_gallery);
        cam=view.findViewById(R.id.use_camera);
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "gal", Toast.LENGTH_SHORT).show();
                TakePhotoOrGallery("gallery");
                // alertDialog.dismiss();
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), "cam", Toast.LENGTH_SHORT).show();
                TakePhotoOrGallery("camera");
                // alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog = builder.show();
    }

    private void TakePhotoOrGallery(String farmer_choice) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return;
        }
        if(farmer_choice.equals("camera"))
        {
            // ProfileImage.setImageDrawable(R.drawable.profile2);
            //ProfileImage.setImageURI();
            OpenCamera();
            alertDialog.dismiss();
        }
        else{
            OpenGallery();
            alertDialog.dismiss();
        }
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

    private void OpenCamera() {
        //Toast.makeText(getActivity(),"camera",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);

    }

    private void OpenGallery() {
        // Toast.makeText(getActivity(),"gallery",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==CAPTURE_IMAGE)
        {
            Bundle bundle=data.getExtras();
            Bitmap photo= (Bitmap) bundle.get("data");
            photoUri=getImageUri(getActivity(),photo);
            saveUri=photoUri;
            Picasso.with(getActivity())
                    .load(photoUri)
                    .resize(100,100)
                    .rotate(90)
                    .centerCrop()
                    .into(ProfileImage);
            //   UploadImage(photoUri);
        }
        else{
            if(resultCode==RESULT_OK &&requestCode==PICK_IMAGES){

                if(resultCode==RESULT_OK){

                    if(data.getData()!=null){

                        imageUri=data.getData();
                        Log.d("CheckingUri", imageUri+"");
                        saveUri=imageUri;
                        Picasso.with(getActivity())
                                .load(imageUri)
                                .resize(100,100)
                                .centerCrop()
                                .into(ProfileImage);
                        //             UploadImage(photoUri);
                    }
                }

            }

        }

    }

    private void UploadImage(Uri imageUri) {
        // Uri naqvi=imageUri;
        //      databaseReference=firebaseDatabase.getReference("Users").child(get_phone_num);
        //    Log.d("SaveValues", naqvi+"");
        if(!imageUri.equals(""))
        {
            storageReference= FirebaseStorage.getInstance().getReference().child("/images/"+System.currentTimeMillis()+"."+getImageExt(imageUri));
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //   Toast.makeText(getActivity(),"successfull",Toast.LENGTH_SHORT).show();
                            imageUrl=taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("profile").setValue(imageUrl);
                            databaseReferenceCard.child("profile").setValue(imageUrl);
                            progressDialog.dismiss();
                            SignedIn();
                            //    i++;
                            //   Log.d("SaveValues", imageUri+"");
                            // user=new farmer_crop_infor(get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area,get_sack_price,get_quantity,get_harvest,get_WaterSource,imageUrl);
//
                        }
                    });

        }
        else{

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
