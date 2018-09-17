package com.example.root.formarsupport;


import android.*;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
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
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.MapsFragment.lati;
import static com.example.root.formarsupport.MapsFragment.longi;
import static com.example.root.formarsupport.Sign_Up_As.Former_choice;
import static com.example.root.formarsupport.Sign_Up_As.chosen;
import static com.example.root.formarsupport.farmer_add_crop.user;
import static com.example.root.formarsupport.farmer_fruit_product.fruit_user;

/**
 * https://stackoverflow.com/questions/8033316/to-draw-an-underline-below-the-textview-in-android
 */
public class broker_signUP extends Fragment implements FarmerChoiceDone {

    private EditText name,NTN,phone_num,pass,product_name,city,TEHSIL,DISTT,product_quant;
    private String get_name,get_NTN,get_phone_num,get_pass,get_product_name,get_city,
            get_TEHSIL,get_DISTT,get_product_quant,mVerificationId,get_brokerName,get_brokerPhone,get_brokerNtn,get_incomplete;
    private String currentUser;
    private Button create_account;
    private TextView detailIs,broker_name,broker_ntn,broker_phone,broker_incomplete,code_error,Resendcode;
    private ImageView upload_image;
    private CircleImageView ProfileImage;
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    private View view;
    RelativeLayout layout;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;//this is very important to declare to dismiss the dialogue
    static Double broker_lati,broker_longi;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceCard;
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    GoogleMap mMap;
    MapFragment mapFragment;
    LocationManager locationManager;
    String imageUrl;
    Marker marker = null;
    SharedPreferences preferences;
    Uri photoUri,imageUri,saveUri;
    ArrayList<String> uidArray;
    int k=0,i=0;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private DatabaseReference friendReference;
    ProgressDialog progressDialog;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_broker_signup, container, false);
       gettingIds();
        String showDetail=detailIs.getText().toString();
        String resendText=Resendcode.getText().toString();
        uidArray=new ArrayList<>();
        UnderlineText(showDetail);
        UnderlineResendText(resendText);
        mCallbacksMethod();
        builder = new AlertDialog.Builder(getActivity());
        progressDialog = new ProgressDialog(getActivity());
       retrevingFShared();
        if(lati !=0.0 && longi !=0.0) {
            broker_lati=lati;
            broker_longi=longi;
        }
       Maps();
       create_account.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               gettingValues();
               CheckFieldsAndSaveData();
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
                UploadImageFrom(view);
            }
        });
        Resendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendCodeToDevice(get_phone_num,mResendToken);
            }
        });
        return view;
    }

    private void mCallbacksMethod() {
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //directly verify if sim is in current mobile
                //  SendToFirebase();
                signInWithPhoneAuthCredential(phoneAuthCredential);
                //    Log.d(TAG, "onVerificationCompleted: ");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Log.d(TAG, "onVerificationFailed: ");
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    //       Log.d(TAG, "invalid credentials");

                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    //         Log.d(TAG, "Too many requests: ");
                }
                else if (e instanceof FirebaseApiNotAvailableException)
                {
                    //        Log.d(TAG, "api is not available");
                }
                else if(e instanceof FirebaseNetworkException){
                    //        Log.d(TAG, "problem with network");

                }

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                //        Log.d(TAG, "onCodeAutoRetrievalTimeOut: ");
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

            }

        };
    }

    private void ResendCodeToDevice(String phoneNumber,
                PhoneAuthProvider.ForceResendingToken token) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    getActivity(),               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        }

    private void CheckFieldsAndSaveData() {
        if(TextUtils.isEmpty(get_name))
        {
            broker_name.setVisibility(View.VISIBLE);
            broker_incomplete.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(get_NTN))
        {
            broker_ntn.setVisibility(View.VISIBLE);
            broker_incomplete.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(get_phone_num))
        {
            broker_phone.setVisibility(View.VISIBLE);
            broker_incomplete.setVisibility(View.VISIBLE);

        }
        if(!TextUtils.isEmpty(get_name) && !TextUtils.isEmpty(get_NTN) && !TextUtils.isEmpty(get_phone_num))
        {
            if(i==0) {
                create_account.setText(R.string.verify);
                VerfiyPhoneNumberViacode();
                i=i+1;
            }
            else{
                if(!TextUtils.isEmpty(get_pass)){
                    VerifyByEnteringCode(mVerificationId, get_pass);
                }
                else{
                    code_error.setVisibility(View.VISIBLE);
                    broker_incomplete.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void VerifyByEnteringCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void ValidatePhoneNumber() {
        String CheckPlusSign=get_phone_num.substring(0,1);
        if(CheckPlusSign.equals("+") || CheckPlusSign.equals("0"))
        {
            //String CheckCode=;
            if(!(get_phone_num.substring(0,4).equals("+923")&& (get_phone_num.length()==13)) && !(get_phone_num.length()==11)){
                Toast.makeText(getActivity(),"InValid Phone Number  ",Toast.LENGTH_SHORT).show();
            }
            else if(!(get_phone_num.substring(0,2).equals("03")&& (get_phone_num.length()==11)) && !(get_phone_num.length()==13)){
                Toast.makeText(getActivity(),"InValid Phone Number ",Toast.LENGTH_SHORT).show();
            }
            else{
                broker_phone.setVisibility(View.INVISIBLE);
                //Toast.makeText(getActivity(),"Valid Phone Number ",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Invalid Phone Number ",Toast.LENGTH_SHORT).show();
        }
    }
    private void UploadImageFrom(View view) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.upload_image,null));

        alertDialog=builder.show();

    }

    private void UnderlineText(String underline) {
        SpannableString content = new SpannableString(underline);
        content.setSpan(new UnderlineSpan(), 0, underline.length(), 0);
        detailIs.setText(content);
    }
    private void UnderlineResendText(String underline) {
        SpannableString content = new SpannableString(underline);
        content.setSpan(new UnderlineSpan(), 0, underline.length(), 0);
        Resendcode.setText(content);
    }

    /**
     * Retreive value from file
     */
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
                broker_lati=Double.valueOf(userlati);
                broker_longi=Double.valueOf(userlongi);
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
            broker_infor info=new broker_infor(get_name,get_NTN,get_phone_num,get_pass,get_product_name,get_city,get_TEHSIL,get_DISTT,get_product_quant,chosen,broker_lati+"",broker_longi+"",imageUrl);
            databaseReference.setValue(info);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(get_name,"بروکر",get_city,get_product_name,"0",get_DISTT,"0",imageUrl,currentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
            progressDialog.dismiss();
            SignedIn();
        }
        else{
            if(photoUri==null) {
                UploadImage(imageUri);
            }
            if(imageUri==null){
                UploadImage(photoUri);
            }
            Toast.makeText(getActivity(),"data Is saving",Toast.LENGTH_SHORT).show();
            broker_infor info=new broker_infor(get_name,get_NTN,get_phone_num,get_pass,get_product_name,get_city,get_TEHSIL,get_DISTT,get_product_quant,chosen,broker_lati+"",broker_longi+"",imageUrl);
            databaseReference.setValue(info);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(get_name,"بروکر",get_city,get_product_name,"0",get_DISTT,"0",imageUrl,currentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
        }
        //


    }

    private void SignedIn() {
        Intent broker_intent=new Intent(getActivity(),BrokerLoggedIn.class);
        startActivity(broker_intent);
    }

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
        editor.putString("brokerlati", broker_lati+"");
        editor.putString("brokerlongi", broker_longi+"");
        editor.commit();
    }

    private void VerfiyPhoneNumberViacode() {
        get_phone_num = get_phone_num.replaceAll(" ", "");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                get_phone_num,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallbacks
        );
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
      //  Log.d(TAG, "signInWithPhoneAuthCredential: ");
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("saving profile.....");
                            progressDialog.show();
                            SendToFirebase();
                          //  i=0;
                            create_account.setText(getResources().getString(R.string.go));
                            Toast.makeText(getActivity(),"successfully register",Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
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
                else if(lati==0.0 && longi==0.0) {
                    Toast.makeText(getActivity(),"entered into current location",Toast.LENGTH_LONG).show();
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        broker_lati=location.getLatitude();
                        broker_longi=location.getLongitude();
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        if (marker != null) {
                            marker.remove();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("Me"));


                    }
                }
                else {
                    LatLng selected = new LatLng(broker_lati, broker_longi);
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

    private void gettingIds() {
        name=view.findViewById(R.id.name);
        NTN=view.findViewById(R.id.NTN);
        phone_num=view.findViewById(R.id.phone);
        pass=view.findViewById(R.id.pass);
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
    }
    private void gettingValues(){
        get_name=name.getText().toString();
        get_NTN=NTN.getText().toString();
        get_phone_num=phone_num.getText().toString();
        get_pass=pass.getText().toString();
        get_product_name=product_name.getText().toString();
        get_city=city.getText().toString().toString();
        get_TEHSIL=TEHSIL.getText().toString();
        get_DISTT=DISTT.getText().toString();
        get_brokerName=broker_name.getText().toString();
        get_brokerPhone=broker_phone.getText().toString();
        get_brokerNtn=broker_ntn.getText().toString();
        get_incomplete=broker_incomplete.getText().toString();
        Checkfields();

    }

    private void Checkfields() {

        if(!TextUtils.isEmpty(get_name))
        {
            broker_name.setVisibility(View.INVISIBLE);
            //broker_incomplete.setVisibility(View.INVISIBLE);
        }
        if(!TextUtils.isEmpty(get_NTN))
        {
            broker_ntn.setVisibility(View.INVISIBLE);
            //broker_incomplete.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(get_phone_num))
        {
            ValidatePhoneNumber();
            //broker_incomplete.setVisibility(View.VISIBLE);

        }
        if(!TextUtils.isEmpty(get_name) && !TextUtils.isEmpty(get_NTN) && !TextUtils.isEmpty(get_phone_num)){

            broker_incomplete.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void sendData(String farmer_choice) {

            TakePhotoOrGallery(farmer_choice);
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
    private void UploadImage(final Uri imageUri) {
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
    /*@Override
    public void onPause() {
        super.onPause();
        saveInShared();
    }

    *//**
     * saveing values in file
     *//*
    private void saveInShared(){
        gettingValues();
        // Log.d("SaveValues", userlogin+"  "+userPass+"  ");
        preferences=getActivity().getSharedPreferences("farmer_signUp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        //   Toast.makeText(getActivity(),"signUp data is saving",Toast.LENGTH_LONG).show();
        get_name,get_NTN,get_phone_num,get_pass,
                get_again_pass,get_product_name,get_city,
                get_TEHSIL,get_DISTT,get_product_quant
        editor.putString("brokername",get_name);
        editor.putString("brokerntn",get_NTN);
        editor.putString("brokerphone",get_phone_num);
        editor.putString("brokerproduct",get_product_name);
        editor.putString("brokercity", get_city);
        editor.putString("brokertehsil", get_TEHSIL);
        editor.putString("brokerdistt", get_DISTT);
        editor.putString("brokerquant", get_product_quant);
        editor.putString("brokerlati", +"");
        editor.putString("brokerlongi", +"");
        editor.commit();
    }*/
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
