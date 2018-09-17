package com.example.root.formarsupport;
import android.Manifest;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.Sign_Up_As.Former_choice;
//import static com.example.root.formarsupport.Sign_Up_As.cameraIs;
import static com.example.root.formarsupport.Sign_Up_As.chosen;
//import static com.example.root.formarsupport.farmer_add_crop.test1;
import static com.example.root.formarsupport.farmer_add_crop.mArrayUri;
import static com.example.root.formarsupport.farmer_add_crop.user;
import static com.example.root.formarsupport.farmer_fruit_product.fruit_user;
import static com.example.root.formarsupport.farmer_fruit_product.mArrayFruitUri;
//import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Farmer_signUp extends Fragment implements FarmerChoiceDone {
    private ImageButton imgbutton;
    private ImageView upload_image;
    //,Use_camera,Use_gallery;
    private View view, view2;
    Uri imageUri, photoUri;
    private EditText name, phone_num, CheckCode;
    private Button signUp;
    private String name_value = " ", phone_value = " ", invalidcode = " ",CurrentUser,fieldIs="";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceCard;
    private FirebaseAuth firebaseAuth;
    private CircleImageView ProfileImage;
    private ArrayList<String> SaveImages;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private TextView ShowError, InvalidPhone, WriteName, Codeerror,resendmeCode;
    farmer_add_crop farmer_add_crop = new farmer_add_crop();
    farmer_fruit_product farmer_fruit = new farmer_fruit_product();
    AlertDialog.Builder builder;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    AlertDialog alertDialog;//this is very important to declare to dismiss the dialogue
    String FieldError, mVerificationId;
    StorageReference storageReference;
    private String DataOf;
    private static final int PICK_IMAGES = 1;
    private static final int CAPTURE_IMAGE = 2;
    SharedPreferences preferences;
    int i = 0, j = 0;
    String imageUrl,imageUrls;
    ProgressDialog progressDialog;
    ArrayList<String> strUris;
//String regexStr = "^\\+[0-9]{10,13}$";
//FragmentManager mapFragment
//String MakeEmpty;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_farmer_sign_up, container, false);
        // view2=inflater.inflate(R.layout.upload_image, container,false);
        gettingIds(view);
        retrevingFShared();
        mCallbackMethod();
        String resendcodetext=resendmeCode.getText().toString();
        UnderlineResendText(resendcodetext);
        progressDialog = new ProgressDialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogueBox(view);
            }
        });
      /*  if(cameraIs.equals("naqvi"))
        {
            UseCamera();
        }*/
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageFrom(view);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    getValueFromTextField();
                    if (!TextUtils.isEmpty(phone_value) && !TextUtils.isEmpty(name_value)) {
                        signUp.setText(getResources().getString(R.string.verify));
                        i = i + 1;
                        VerfiyPhoneNumberViacode();
                    }
                } else {
                    String code = CheckCode.getText().toString();
                    VerifyByEnteringCode(mVerificationId, code);
                }
            }
        });
        resendmeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(phone_value,mResendToken);
            }
        });
        ConvertUriToString();
        return view;
    }

    private void ConvertUriToString() {
        if(mArrayUri!=null) {
            for (int i=0;i<mArrayUri.size();i++)
            {
                strUris.add(mArrayUri.get(i).toString());
            }
        }
        if(mArrayFruitUri!=null)
        {
            for(int j=0;j<mArrayFruitUri.size();j++)
            {
                strUris.add(mArrayFruitUri.get(j).toString());
            }
        }
    }

    private void mCallbackMethod() {
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
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void UploadImageFrom(View view) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.upload_image, null));

        alertDialog = builder.show();

    }

    private void VerifyByEnteringCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    /**
     * sending data to fire base
     */
    private void SendToFirebase() {
        CurrentUser = firebaseAuth.getCurrentUser().getUid();
        databaseReference = firebaseDatabase.getReference("Users").child(CurrentUser);
        databaseReferenceCard=firebaseDatabase.getReference("cardview").child(CurrentUser);
        if (Former_choice == null) {
            if (imageUri == null && photoUri == null) {
                imageUrl = "";
                Farmer_signup_infor info = new Farmer_signup_infor(name_value, phone_value, chosen,imageUrl,fieldIs);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(name_value,"کسان","","","0","","0",imageUrl,CurrentUser,"1");
                databaseReference.child("personal").setValue(info);
                databaseReferenceCard.setValue(cardinfo);
                /* try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("ProgressDia", "naqvi");*/
                progressDialog.dismiss();
                SignedIn();
            }
            else{
            if (photoUri == null) {
                UploadImage(imageUri);

            }
            if (imageUri == null) {
                UploadImage(photoUri);
            }
            Farmer_signup_infor info = new Farmer_signup_infor(name_value, phone_value, chosen,imageUrl,fieldIs);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(name_value,"کسان","","","0","","0",imageUrl,CurrentUser,"1");
            databaseReference.child("personal").setValue(info);
            databaseReferenceCard.setValue(cardinfo);

        }
    }
        else if(Former_choice.equals("crop"))
        {
            DataOf=Former_choice;
            fieldIs=Former_choice;
            if(mArrayUri.isEmpty()!=true)
            {
                Log.d("ArrayUri", mArrayUri.size()+"");
                for(int j=0;j<mArrayUri.size();j++)
                {
                    UploadImages(mArrayUri.get(j));
                }
            }
                SaveDataToFirebase();
        }
        else {
            DataOf=Former_choice;
            fieldIs=Former_choice;
            if (mArrayFruitUri.isEmpty() != true) {
                for (int j = 0; j < mArrayFruitUri.size(); j++) {
                    UploadImages(mArrayFruitUri.get(j));
                }
                Log.d("ArrayUri", mArrayFruitUri+"");
            }
            SaveDataToFirebase();
        }

    }

    private void SignedIn() {
        Intent farmer_intent=new Intent(getActivity(),Farmer_logged_In.class);
        startActivity(farmer_intent);
    }

    private void SaveDataToFirebase() {
if(DataOf.equals("crop")) {
    CropDataToFirebase();
    if(mArrayUri.isEmpty()==true && imageUri == null && photoUri == null)
    {
        progressDialog.dismiss();
        SignedIn();
    }
}
else{
    FruitDataToFirebase();
    if(mArrayFruitUri.isEmpty()==true && imageUri == null && photoUri == null)
    {
        progressDialog.dismiss();
        SignedIn();
    }
}
    }

    private void FruitDataToFirebase() {
        if (imageUri == null && photoUri == null) {
            imageUrl = "";
            Farmer_signup_infor info = new Farmer_signup_infor(name_value, phone_value, chosen, imageUrl,fieldIs);
            databaseReference.child("personal").setValue(info);
            databaseReference.child(DataOf).setValue(fruit_user);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(name_value,"کسان",fruit_user.getCity(),fruit_user.getProduct_name(),"0",fruit_user.getDISTT(),"0",imageUrl,CurrentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
        } else {
            if (photoUri == null) {
                UploadImage(imageUri);
            }
            if (imageUri == null) {
                UploadImage(photoUri);
            }
            Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
            Farmer_signup_infor info = new Farmer_signup_infor(name_value, phone_value, chosen, imageUrl,fieldIs);
            databaseReference.child("personal").setValue(info);
            databaseReference.child(DataOf).setValue(fruit_user);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(name_value,"کسان",fruit_user.getCity(),fruit_user.getProduct_name(),"0",fruit_user.getDISTT(),"0",imageUrl,CurrentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
        }
    }

    private void CropDataToFirebase() {
        if (imageUri == null && photoUri == null) {
            imageUrl = "";
            Farmer_signup_infor info = new Farmer_signup_infor(name_value, phone_value, chosen, imageUrl,fieldIs);
            databaseReference.child("personal").setValue(info);
            databaseReference.child(DataOf).setValue(user);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(name_value,"کسان",user.getCity(),user.getProduct_name(),"0",user.getDISTT(),"0",imageUrl,CurrentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
        } else {
            if (photoUri == null) {
                UploadImage(imageUri);
            }
            if (imageUri == null) {
                UploadImage(photoUri);
            }
            Toast.makeText(getActivity(), getResources().getString(R.string.data_saving), Toast.LENGTH_SHORT).show();
            Farmer_signup_infor info = new Farmer_signup_infor(name_value, phone_value, chosen, imageUrl,fieldIs);
            databaseReference.child("personal").setValue(info);
            databaseReference.child(DataOf).setValue(user);
            cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(name_value,"کسان",user.getCity(),user.getProduct_name(),"0",user.getDISTT(),"0",imageUrl,CurrentUser,"1");
            databaseReferenceCard.setValue(cardinfo);
        }
    }

    /**
     * Retreive value from file
     */
    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("farmer_signUp", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("username", "");   //will return false if doesn't exist "user_name"
        Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {
            String username = preferences.getString("username", "");
            String userphone = preferences.getString("userphone", "");
            name.setText(username);
            phone_num.setText(userphone);
        } else {
           // Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
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
        getValueFromTextField();
        // Log.d("SaveValues", userlogin+"  "+userPass+"  ");
        preferences=getActivity().getSharedPreferences("farmer_signUp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
     //   Toast.makeText(getActivity(),"signUp data is saving",Toast.LENGTH_LONG).show();
        editor.putString("username",name_value);
        editor.putString("userphone",phone_value);
        editor.commit();
    }
    private void UploadImage(Uri imageUri) {
        Uri naqvi=imageUri;
    //    Log.d("SaveValues", naqvi+"");
        if(!imageUri.equals(""))
        {
            //databaseReference=firebaseDatabase.getReference(phone_value);
            storageReference= FirebaseStorage.getInstance().getReference().child("/images/"+System.currentTimeMillis()+"."+getImageExt(imageUri));
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         //   Toast.makeText(getActivity(),"successfull",Toast.LENGTH_SHORT).show();
                            imageUrl=taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("personal").child("profile").setValue(imageUrl);
                            databaseReferenceCard.child("profile").setValue(imageUrl);
                            if(mArrayFruitUri==null || mArrayUri==null) {
                                progressDialog.dismiss();
                                SignedIn();
                            }
                            //i++;
                         //   Log.d("SaveValues", imageUri+"");
                            // user=new farmer_crop_infor(get_Product_name,get_Product_type,get_city,get_TEHIL,get_DISTT,get_area,get_sack_price,get_quantity,get_harvest,get_WaterSource,imageUrl);
//
                        }
                    });

        }
        else{
          //  Toast.makeText(getActivity(),"image uri doesn't found",Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImages(final Uri imageUri) {
        //    Log.d("SaveValues", naqvi+"");
        if(!imageUri.equals(""))
        {
            storageReference= FirebaseStorage.getInstance().getReference().child("/images/"+System.currentTimeMillis()+"."+getImageExt(imageUri));
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageUrls=taskSnapshot.getDownloadUrl().toString();
                            SaveImages.add(imageUrls);
                            if(mArrayUri!=null) {
                                if (mArrayUri.size() == SaveImages.size()) {
                                    databaseReference.child("images").setValue(SaveImages);
                                 // databaseReference.child("imagesUri").setValue(strUris);
                                    progressDialog.dismiss();
                                    SignedIn();
                                }
                            }
                            else{
                                if(mArrayFruitUri.size()==SaveImages.size())
                                {
                                    databaseReference.child("images").setValue(SaveImages);
                                   //databaseReference.child("imagesUri").setValue(strUris);
                                    progressDialog.dismiss();
                                    SignedIn();
                                }
                            }

                            Log.d("ArrayUri", SaveImages.size()+"");
                        }
                    });

        }
        else{
            //  Toast.makeText(getActivity(),"image uri doesn't found",Toast.LENGTH_SHORT).show();
        }
    }
    private void UnderlineResendText(String underline) {
        SpannableString content = new SpannableString(underline);
        content.setSpan(new UnderlineSpan(), 0, underline.length(), 0);
        resendmeCode.setText(content);
    }
    private void VerfiyPhoneNumberViacode() {
        phone_value = phone_value.replaceAll(" ", "");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_value,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallbacks
        );

    }
    private String TAG="naqvi_task";
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d(TAG, "signInWithPhoneAuthCredential: ");
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage(getResources().getString(R.string.savingProfile));
                            progressDialog.show();
                            SendToFirebase();
                            i=0;
                            signUp.setText(getResources().getString(R.string.go));
                           // Toast.makeText(getActivity(),"successfully register",Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                           /* Log.d(TAG, "signInWithCredential:success");*/
                            // Toast.makeText(Mobithis,"successfully logged in",Toast.LENGTH_LONG).show();
                            // finish();
                            //  FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            Codeerror.setVisibility(View.VISIBLE);
                            i=0;
                            signUp.setText(getResources().getString(R.string.go));
                            // Sign in failed, display a message and update the UI
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void showDialogueBox(View view) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_farmer_register_as,null));

        alertDialog=builder.show();
    }

    /**
     * use to get values from edit text fields
     */
    private void getValueFromTextField() {
        FieldError=getResources().getString(R.string.required_field);
        name_value = name.getText().toString();
        phone_value = phone_num.getText().toString();

        if(!TextUtils.isEmpty(phone_value)){
            ValidatePhoneNumber();
        }
        if(!TextUtils.isEmpty(name_value))
        {
            WriteName.setVisibility(View.INVISIBLE);
        }

        AllEmptyField();


    }

    private void AllEmptyField() {


            if(TextUtils.isEmpty(phone_value))
            {
                InvalidPhone.setVisibility(View.VISIBLE);
                ShowError.setVisibility(View.VISIBLE);
            }
            if(TextUtils.isEmpty(name_value))
            {
                WriteName.setVisibility(View.VISIBLE);
                ShowError.setVisibility(View.VISIBLE);
            }

        }


    /**
     * it is checking while phone is valid or not
     */
    private void ValidatePhoneNumber() {
        String CheckPlusSign=phone_value.substring(0,1);
        if(CheckPlusSign.equals("+") || CheckPlusSign.equals("0"))
        {
            //String CheckCode=;
            if(!(phone_value.substring(0,4).equals("+923")&& (phone_value.length()==13)) && !(phone_value.length()==11)){
                Toast.makeText(getActivity(),getResources().getString(R.string.error_invalid_phone),Toast.LENGTH_LONG).show();
            }
            else if(!(phone_value.substring(0,2).equals("03")&& (phone_value.length()==11)) && !(phone_value.length()==13)){
                Toast.makeText(getActivity(),getResources().getString(R.string.error_invalid_phone),Toast.LENGTH_LONG).show();
            }
            else{
                InvalidPhone.setVisibility(View.INVISIBLE);
                //Toast.makeText(getActivity(),"Valid Phone Number ",Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(getActivity(),getResources().getString(R.string.error_invalid_phone),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * moving to next fragment to add information about product
     */
    private void Add_crop_Product() {
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame, farmer_add_crop);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * getting ids from xml file
     * @param view
     */
    private void gettingIds(View view) {
        imgbutton=view.findViewById(R.id.add_new_info);
        name=view.findViewById(R.id.name);
        phone_num=view.findViewById(R.id.phone);
        ProfileImage=view.findViewById(R.id.personProfileIs);
       /* pass=view.findViewById(R.id.pass);
        again_pass=view.findViewById(R.id.again_pass);*/
        signUp=view.findViewById(R.id.sign_Up);
        ShowError=view.findViewById(R.id.Error);
        upload_image=view.findViewById(R.id.UploadImage);
        resendmeCode=view.findViewById(R.id.resendIt);
      /*  Use_camera=view2.findViewById(R.id.use_camera);
        Use_gallery=view2.findViewById(R.id.user_gallery);*/
    //    ShowPassEr=view.findViewById(R.id.PassError);
        InvalidPhone=view.findViewById(R.id.invalidPhone);
    //    WritePass=view.findViewById(R.id.writePass);
        WriteName=view.findViewById(R.id.WriteName);
        CheckCode=view.findViewById(R.id.code);
        Codeerror=view.findViewById(R.id.invalidCode);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        SaveImages=new ArrayList<>();
        strUris=new ArrayList<>();

        //databaseReference=firebaseDatabase.getReference();
    }

    /**
     * implemented method to get data from other class
     * @param farmer_choice
     */
    @Override
    public void sendData(String farmer_choice) {
        if(farmer_choice.equals("camera") || farmer_choice.equals("gallery"))
        {
            TakePhotoOrGallery(farmer_choice);
        }
        else {
            proceddRegistration(farmer_choice);
        }
    }

    private void TakePhotoOrGallery(String farmer_choice) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},1);
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
        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.select_pic)), PICK_IMAGES);
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
            Picasso.with(getActivity())
                    .load(photoUri)
                    .resize(100,100)
                    .rotate(90)
                    .centerCrop()
                    .into(ProfileImage);

        }
        else{
            if(requestCode==PICK_IMAGES){

                if(resultCode==RESULT_OK){

                    if(data.getData()!=null){

                       imageUri=data.getData();
                        Picasso.with(getActivity())
                                .load(imageUri)
                                .resize(100,100)
                                .centerCrop()
                                .into(ProfileImage);
                    }
                }

            }

        }

    }
    /**
     * after select the choice farmer is sent to related fragment
     * @param farmer_choice
     */
    private void proceddRegistration(String farmer_choice) {
        if(farmer_choice.equals("crop"))
        {
            Add_crop_Product();
            alertDialog.dismiss();
        }
        else{

            Add_fruit_product();
            alertDialog.dismiss();
        }
    }

    private void Add_fruit_product() {
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame,farmer_fruit);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, getResources().getString(R.string.imagePath), null);
        return Uri.parse(path);
    }
}

