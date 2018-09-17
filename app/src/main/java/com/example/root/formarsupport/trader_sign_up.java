package com.example.root.formarsupport;

import android.*;
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
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.Sign_Up_As.Former_choice;
import static com.example.root.formarsupport.Sign_Up_As.chosen;
import static com.example.root.formarsupport.crop_trader.trader_crop_user;
import static com.example.root.formarsupport.farmer_add_crop.mArrayUri;
import static com.example.root.formarsupport.farmer_add_crop.user;
import static com.example.root.formarsupport.farmer_fruit_product.fruit_user;
//import static com.example.root.formarsupport.farmer_fruit_product.mArrayFruitUri;
import static com.example.root.formarsupport.fruit_trader.trader_f_user;


public class trader_sign_up extends Fragment implements FarmerChoiceDone {
private ImageButton trader_product;
private Button createAccount;
private EditText name,ntnNum,phone,Code;
private TextView resendCode,CountTime,t_name_error,t_phone_error,t_ntn_error,t_incomplete_error;
    private CircleImageView ProfileImage;
    private ImageView upload_image;
private String namevalue,ntnNumvalue,phonevalue,Codevalue,mVerificationId,currentUser,fieldis="";
crop_trader crop_trader=new crop_trader();
fruit_trader fruit_trader=new fruit_trader();
    private static final int PICK_IMAGES=1;
    private static final int CAPTURE_IMAGE=2;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Uri imageUri,photoUri;
    StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private String imageUrl;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceCard;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String TAG="naqvi_verify";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    SharedPreferences preferences;
    ProgressDialog progressDialog;
    private int time=60;
    int i=0,j=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trader_sign_up, container, false);
        gettingids(view);
        retrevingFShared();
        String resendText=resendCode.getText().toString();
        UnderlineResendText(resendText);
        builder = new AlertDialog.Builder(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        mCallbacksMethod();
        trader_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogueBox(view);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==0) {
                    gettingValues();
                        CheckFieldsAndSaveData();

                }
                else{
                    String code=Code.getText().toString();
                    VerifyByEnteringCode(mVerificationId,code);
                }

            }
        });
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(phonevalue,mResendToken);

            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageFrom(view);
                j=j+1;
            }
        });
        return view;
    }

    private void VerifyByEnteringCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void UnderlineResendText(String underline) {
        SpannableString content = new SpannableString(underline);
        content.setSpan(new UnderlineSpan(), 0, underline.length(), 0);
        resendCode.setText(content);
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

                //      Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
               // Log.d("code_verified", mVerificationId);
                mResendToken = token;
                //Log.d("code_verified", mResendToken+"");
                //Toast.makeText(this,token,Toast.LENGTH_LONG);
                //signUp.setTextColor(Color.);
                            /*register.setVisibility(View.INVISIBLE);
                            verify.setVisibility(View.VISIBLE);*/

                // ...
            }

        };
    }

    /**
     * Retreive value from file
     */
    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("trader_sign_up", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("username", "");   //will return false if doesn't exist "user_name"
        //Log.d("SaveValues", Notexit);
        if (!Notexit.equals("")) {
            String username = preferences.getString("username", "");
            String userphone = preferences.getString("userphone", "");
            String ntn = preferences.getString("ntn", "");
            String code = preferences.getString("code", "");
            name.setText(username);
            ntnNum.setText(ntn);
            Code.setText(code);
            phone.setText(userphone);
        } else {
            Toast.makeText(getActivity(), "not exits", Toast.LENGTH_SHORT).show();
        }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==CAPTURE_IMAGE)
        {
            Bundle bundle=data.getExtras();
            Bitmap photo= (Bitmap) bundle.get("data");
            photoUri=getImageUri(getActivity(),photo);
           /* Picasso.with(getActivity())
                    .load(photoUri)
                    .centerCrop()
                    .into(ProfileImage);*/
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
    private void SendToFirebase() {
        currentUser = firebaseAuth.getCurrentUser().getUid();
        // Log.d("dataIsSaving", Former_choice);
        databaseReference = firebaseDatabase.getReference("Users").child(currentUser);
        databaseReferenceCard=firebaseDatabase.getReference("cardview").child(currentUser);
        if (Former_choice == null) {
            if (imageUri == null && photoUri == null) {
                imageUrl = "";
                trader_signUp_infor info = new trader_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl,fieldis);
                databaseReference.child("personal").setValue(info);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"آڑتی","","","0","","0",imageUrl,currentUser,"1");
                databaseReferenceCard.setValue(cardinfo);
                progressDialog.dismiss();
                SignedIn();
                // Log.d("dataIsSaving", "no image and farmer is null");
            } else {
                if (photoUri == null) {
                    UploadImage(imageUri);
                    //   Log.d("dataIsSaving", "camera upload image");
                    //    Log.d("dataIsSaving", imageUrl);

                }
                if (imageUri == null) {
                    UploadImage(photoUri);
                    // Log.d("dataIsSaving", "gallery upload image");
                    //     Log.d("dataIsSaving", imageUrl);
                }
                Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
                trader_signUp_infor info = new trader_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl,fieldis);
                databaseReference.child("personal").setValue(info);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"آڑتی","","","0","","0",imageUrl,currentUser,"1");
                databaseReferenceCard.setValue(cardinfo);
                //Log.d("dataIsSaving", "data saved");
                //     Log.d("dataIsSaving", imageUrl);
            }


        } else if (Former_choice.equals("crop")) {
            fieldis=Former_choice;
            if (imageUri == null && photoUri == null) {
                imageUrl = "";
                trader_signUp_infor info = new trader_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl,fieldis);
                databaseReference.child("personal").setValue(info);
                databaseReference.child("crop").setValue(trader_crop_user);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"آڑتی",trader_crop_user.getCity(),trader_crop_user.getProduct_name(),"0",trader_crop_user.getDISTT(),"0",imageUrl,currentUser,"1");
                databaseReferenceCard.setValue(cardinfo);
            } else {
                if (photoUri == null) {
                    UploadImage(imageUri);
                }
                if (imageUri == null) {
                    UploadImage(photoUri);
                }
                Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
                trader_signUp_infor info = new trader_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl,fieldis);
                databaseReference.child("personal").setValue(info);
                databaseReference.child("crop").setValue(trader_crop_user);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"آڑتی",trader_crop_user.getCity(),trader_crop_user.getProduct_name(),"0",trader_crop_user.getDISTT(),"0",imageUrl,currentUser,"1");
                databaseReferenceCard.setValue(cardinfo);
            }
            if (imageUri == null && photoUri == null) {
                progressDialog.dismiss();
                SignedIn();
            }

        } else {
            fieldis=Former_choice;
            //Log.d("dataIsSaving", Former_choice);
            if (imageUri == null && photoUri == null) {
                imageUrl = "";
                trader_signUp_infor info = new trader_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl,fieldis);
                databaseReference.child("personal").setValue(info);
                databaseReference.child("fruit").setValue(trader_f_user);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"آڑتی",trader_f_user.getCity(),trader_f_user.getPro_name(),"0",trader_f_user.getDistt(),"0",imageUrl,currentUser,"1");
                databaseReferenceCard.setValue(cardinfo);
                //Log.d("dataIsSaving", "no image");

            } else {
                if (photoUri == null) {
                    UploadImage(imageUri);
                    //      Log.d("dataIsSaving", "camera upload image");
//                    Log.d("dataIsSaving", imageUrl);
                }
                if (imageUri == null) {
                    UploadImage(photoUri);
                    // Log.d("dataIsSaving", "gallery upload image");
                    //                  Log.d("dataIsSaving", imageUrl);

                }
                Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
                trader_signUp_infor info = new trader_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl,fieldis);
                databaseReference.child("personal").setValue(info);
                databaseReference.child("fruit").setValue(trader_f_user);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"آڑتی",trader_f_user.getCity(),trader_f_user.getPro_name(),"0",trader_f_user.getDistt(),"0",imageUrl,currentUser,"1");
                databaseReferenceCard.setValue(cardinfo);
            }
            if (imageUri == null && photoUri == null) {
                progressDialog.dismiss();
                SignedIn();
            }
        }

    }

    private void SignedIn() {
        Intent trader_intent=new Intent(getActivity(),TraderLoggedIn.class);
        startActivity(trader_intent);
    }

    private void VerfiyPhoneNumberViacode() {
        phonevalue = phonevalue.replaceAll(" ", "");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonevalue,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallbacks
        );
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
       // Log.d("SaveValues", userlogin+"  "+userPass+"  ");
        preferences=getActivity().getSharedPreferences("trader_sign_up",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Toast.makeText(getActivity(),"signUp data is saving",Toast.LENGTH_LONG).show();
        editor.putString("username",namevalue);
        editor.putString("userphone",phonevalue);
        editor.putString("ntn",ntnNumvalue);
        editor.putString("code",Codevalue);
        editor.commit();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
       // Log.d(TAG, "signInWithPhoneAuthCredential: ");
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("saving profile.....");
                            progressDialog.show();
                            SendToFirebase();
                          //  i=0;
                            createAccount.setText(getResources().getString(R.string.go));
                            Toast.makeText(getActivity(),"successfully register",Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                           /* Log.d(TAG, "signInWithCredential:success");*/
                            // Toast.makeText(Mobithis,"successfully logged in",Toast.LENGTH_LONG).show();
                            // finish();
                            //  FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                          //  Codeerror.setVisibility(View.VISIBLE);
                          //  i=0;
                          //  signUp.setText(getResources().getString(R.string.go));
                            // Sign in failed, display a message and update the UI
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void CheckFieldsAndSaveData() {
        if(TextUtils.isEmpty(namevalue))
        {
            t_name_error.setVisibility(View.VISIBLE);
            t_incomplete_error.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(ntnNumvalue))
        {
            t_ntn_error.setVisibility(View.VISIBLE);
            t_incomplete_error.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(phonevalue))
        {
            t_phone_error.setVisibility(View.VISIBLE);
            t_incomplete_error.setVisibility(View.VISIBLE);

        }
        if(!TextUtils.isEmpty(namevalue) && !TextUtils.isEmpty(ntnNumvalue) && !TextUtils.isEmpty(phonevalue))
        {
            i=i+1;
            Toast.makeText(getActivity(), "check successfully verified", Toast.LENGTH_SHORT).show();
            createAccount.setText(getResources().getString(R.string.verify));
            VerfiyPhoneNumberViacode();
        }
    }
    private void gettingValues() {
        namevalue=name.getText().toString();
        phonevalue=phone.getText().toString();
        ntnNumvalue=ntnNum.getText().toString();
        Codevalue=Code.getText().toString();
        Checkfields();
    }

    private void showDialogueBox(View view) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_farmer_register_as,null));

        alertDialog=builder.show();
    }

    private void gettingids(View view) {

        trader_product=view.findViewById(R.id.add_info);
        createAccount=view.findViewById(R.id.sign_Up);
        name=view.findViewById(R.id.name);
        ntnNum=view.findViewById(R.id.NTN);
        phone=view.findViewById(R.id.phone);
        Code=view.findViewById(R.id.code);
        resendCode=view.findViewById(R.id.sendAgain);
        CountTime=view.findViewById(R.id.timer);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        ProfileImage=view.findViewById(R.id.ProfileIs);
        t_name_error=view.findViewById(R.id.WritetraderName);
        t_phone_error=view.findViewById(R.id.invalidtraderPhone);
        t_ntn_error=view.findViewById(R.id.Writetraderntn);
        t_incomplete_error=view.findViewById(R.id.traderError);
        upload_image=view.findViewById(R.id.UploadImage);
    }

    private void Checkfields() {

        if(!TextUtils.isEmpty(namevalue))
        {
            t_name_error.setVisibility(View.INVISIBLE);
            //broker_incomplete.setVisibility(View.INVISIBLE);
        }
        if(!TextUtils.isEmpty(ntnNumvalue))
        {
            t_ntn_error.setVisibility(View.INVISIBLE);
            //broker_incomplete.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(phonevalue))
        {
            ValidatePhoneNumber();
            //broker_incomplete.setVisibility(View.VISIBLE);

        }
        if(!TextUtils.isEmpty(namevalue) && !TextUtils.isEmpty(namevalue) && !TextUtils.isEmpty(namevalue)){

            t_incomplete_error.setVisibility(View.INVISIBLE);
        }

    }

    private void ValidatePhoneNumber() {
        String CheckPlusSign=phonevalue.substring(0,1);
        if(CheckPlusSign.equals("+") || CheckPlusSign.equals("0"))
        {
            //String CheckCode=;
            if(!(phonevalue.substring(0,4).equals("+923")&& (phonevalue.length()==13)) && !(phonevalue.length()==11)){
                Toast.makeText(getActivity(),"InValid Phone Number  ",Toast.LENGTH_SHORT).show();
            }
            else if(!(phonevalue.substring(0,2).equals("03")&& (phonevalue.length()==11)) && !(phonevalue.length()==13)){
                Toast.makeText(getActivity(),"InValid Phone Number ",Toast.LENGTH_SHORT).show();
            }
            else{
                t_phone_error.setVisibility(View.INVISIBLE);
                //Toast.makeText(getActivity(),"Valid Phone Number ",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Invalid Phone Number ",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void sendData(String farmer_choice) {
        if(farmer_choice.equals("camera") || farmer_choice.equals("gallery"))
        {
            TakePhotoOrGallery(farmer_choice);
        }
        else {
            RegistertraderAs(farmer_choice);
        }


    }

    private void RegistertraderAs(String farmer_choice) {
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
    private void UploadImageFrom(View view) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.upload_image,null));

        alertDialog=builder.show();

    }
    private void Add_fruit_product() {
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame,fruit_trader);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void Add_crop_Product(){
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame,crop_trader);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void UploadImage(final Uri imageUri) {
        if(!imageUri.equals(""))
        {
            storageReference= FirebaseStorage.getInstance().getReference().child("/images/"+System.currentTimeMillis()+"."+getImageExt(imageUri));
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageUrl=taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("personal").child("profile").setValue(imageUrl);
                            databaseReferenceCard.child("profile").setValue(imageUrl);
                            Toast.makeText(getActivity(),"successfully uploaded",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            SignedIn();
                        }
                    });

        }
        else{
                Toast.makeText(getActivity(),"error while getting uri",Toast.LENGTH_LONG).show();
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
