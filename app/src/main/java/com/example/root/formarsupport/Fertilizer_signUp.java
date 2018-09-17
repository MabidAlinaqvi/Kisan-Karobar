package com.example.root.formarsupport;


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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.root.formarsupport.Fertilizer_medic_info.SaveIt;
import static com.example.root.formarsupport.Fertilizer_medic_info.mArrayfariti;
import static com.example.root.formarsupport.Fertilizer_medic_info.user;
import static com.example.root.formarsupport.Sign_Up_As.chosen;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fertilizer_signUp extends Fragment implements FarmerChoiceDone {
    private ImageButton trader_product;
    private Button createAccount;
    private EditText name,ntnNum,phone,Code;
    private TextView resendCode,CountTime,t_name_error,t_phone_error,t_ntn_error,t_incomplete_error;
    private CircleImageView ProfileImage;
    private ImageView upload_image;
    private String namevalue,ntnNumvalue,phonevalue,Codevalue,mVerificationId,currentUser;
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
    private String keyis;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    SharedPreferences preferences;
    ProgressDialog progressDialog;
    View view;
    private int time=60;
    private ArrayList<String> keyValues,SaveImages;
    private String imageUrls;
    int i=0,j=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_fertilizer_sign_up, container, false);
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
               // Toast.makeText(getActivity(), "add info", Toast.LENGTH_SHORT).show();
                showDialogueBox();
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
                UploadImageFrom();
         //       j=j+1;
            }
        });
        return view;
    }

    private void UploadImageFrom() {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.upload_image,null));

        alertDialog=builder.show();

       /* LayoutInflater inflater = getActivity().getLayoutInflater();

        View inflaterView=inflater.inflate(R.layout.upload_images_fertilizers,null);

        builder.setView(inflater.inflate(R.layout.upload_images_fertilizers,null));

        ChooseGal=inflaterView.findViewById(R.id.user_gallery);

        ChooseCam=inflaterView.findViewById(R.id.use_camera);

        ChooseGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "gal", Toast.LENGTH_SHORT).show();
                //TakePhotoOrGallery("gallery");
            }
        });

        ChooseCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "cam", Toast.LENGTH_SHORT).show();
                // TakePhotoOrGallery("camera");
            }
        });

        alertDialog=builder.show();*/

    }

    private void TakePhotoOrGallery(String chosen)
    {
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
        if(chosen.equals("camera"))
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

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }

    private void OpenCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE);
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

    private void VerifyByEnteringCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
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
        preferences=getActivity().getSharedPreferences("fertilizer_sign_up",Context.MODE_PRIVATE);
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


    private void SendToFirebase() {
        currentUser = firebaseAuth.getCurrentUser().getUid();
        // Log.d("dataIsSaving", Former_choice);
        databaseReference = firebaseDatabase.getReference("Users").child(currentUser);
        databaseReferenceCard=firebaseDatabase.getReference("cardview");
        if (SaveIt == null) {
            if (imageUri == null && photoUri == null) {
                imageUrl = "";
                fertilizer_signUp_infor info = new fertilizer_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl);
                databaseReference.child("personal").setValue(info);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"ادویات فروش","","","0","","0",imageUrl,currentUser,"1");
                keyis=databaseReferenceCard.push().getKey();
                keyValues.add(keyis);
                databaseReferenceCard.child(keyis).setValue(cardinfo);
                databaseReference.child("keysAre").setValue(keyValues);
                progressDialog.dismiss();
                SignedIn();
                // Log.d("dataIsSaving", "no image and farmer is null");
            } else {
                if (photoUri == null) {
                    UploadImage(imageUri);
                }
                if (imageUri == null) {
                    UploadImage(photoUri);
                    // Log.d("dataIsSaving", "gallery upload image");
                    //     Log.d("dataIsSaving", imageUrl);
                }
                Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
                fertilizer_signUp_infor info = new fertilizer_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl);
                databaseReference.child("personal").setValue(info);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"ادویات فروش","","","0","","0",imageUrl,currentUser,"1");
                keyis=databaseReferenceCard.push().getKey();
                keyValues.add(keyis);
                databaseReferenceCard.child(keyis).setValue(cardinfo);
                databaseReference.child("keysAre").setValue(keyValues);
                //Log.d("dataIsSaving", "data saved");
                //     Log.d("dataIsSaving", imageUrl);
            }


        } else if (SaveIt.equals("save")) {
            if (imageUri == null && photoUri == null) {
                imageUrl = "";
                fertilizer_signUp_infor info = new fertilizer_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl);
                databaseReference.child("personal").setValue(info);
                keyis=databaseReferenceCard.push().getKey();
                keyValues.add(keyis);
                databaseReference.child("fertilizer").setValue(user);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"ادویات فروش",user.getCity(),user.getProduct_name(),"0",user.getDISTT(),"0",imageUrl,currentUser,"1");
                databaseReferenceCard.child(keyis).setValue(cardinfo);
                databaseReference.child("keysAre").setValue(keyValues);
                if(mArrayfariti.isEmpty()!=true)
                {
                    Log.d("ArrayUri", mArrayfariti.size()+"");
                    for(int j=0;j<mArrayfariti.size();j++)
                    {
                        UploadImages(mArrayfariti.get(j));
                    }
                }
                else
                {
                        progressDialog.dismiss();
                        SignedIn();
                }
            } else {
                if (photoUri == null) {
                    UploadImage(imageUri);
                }
                if (imageUri == null) {
                    UploadImage(photoUri);
                }
                Toast.makeText(getActivity(), "data Is saving", Toast.LENGTH_SHORT).show();
                fertilizer_signUp_infor info = new fertilizer_signUp_infor(namevalue, phonevalue, ntnNumvalue, chosen, imageUrl);
                databaseReference.child("personal").setValue(info);
                keyis=databaseReferenceCard.push().getKey();
                keyValues.add(keyis);
                databaseReference.child("fertilizer").setValue(user);
                cardview_model_farmer_firstpage cardinfo=new cardview_model_farmer_firstpage(namevalue,"ادویات فروش",user.getCity(),user.getProduct_name(),"0",user.getDISTT(),"0",imageUrl,currentUser,"1");
                databaseReferenceCard.child(keyis).setValue(cardinfo);
                databaseReference.child("keysAre").setValue(keyValues);
                if(mArrayfariti.isEmpty()!=true)
                {
                    Log.d("ArrayUri", mArrayfariti.size()+"");
                    for(int j=0;j<mArrayfariti.size();j++)
                    {
                        UploadImages(mArrayfariti.get(j));
                    }
                }
            }

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
                            if(mArrayfariti!=null) {
                                if (mArrayfariti.size() == SaveImages.size()) {
                                    databaseReference.child("images").setValue(SaveImages);
                                    progressDialog.dismiss();
                                    SignedIn();
                                }
                            }
                            else{
                                if(mArrayfariti.size()==SaveImages.size())
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
        }
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

    private void gettingValues() {
        namevalue=name.getText().toString();
        phonevalue=phone.getText().toString();
        ntnNumvalue=ntnNum.getText().toString();
        Codevalue=Code.getText().toString();
        Checkfields();
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

    private void showDialogueBox() {
        Fertilizer_medic_info medic_info=new Fertilizer_medic_info();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        transaction.replace(R.id.frame,medic_info);
        transaction.addToBackStack(null);
        transaction.commit();


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

    private void UnderlineResendText(String resendText) {
        SpannableString content = new SpannableString(resendText);
        content.setSpan(new UnderlineSpan(), 0, resendText.length(), 0);
        resendCode.setText(content);
    }

    private void retrevingFShared() {
        preferences = getActivity().getSharedPreferences("fertilizer_sign_up", Context.MODE_PRIVATE);
        String Notexit = preferences.getString("username", "");   //will return false if doesn't exist "user_name"
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

    private void gettingids(View view) {
        trader_product=view.findViewById(R.id.add_feri_info);
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
        keyValues=new ArrayList<>();
        SaveImages=new ArrayList<>();
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
                            databaseReferenceCard.child(keyis).child("profile").setValue(imageUrl);
                            Toast.makeText(getActivity(),"successfully uploaded",Toast.LENGTH_LONG).show();
                            if(mArrayfariti.isEmpty()==true) {
                                progressDialog.dismiss();
                                SignedIn();
                            }
                        }
                    });

        }
        else{
            Toast.makeText(getActivity(),"error while getting uri",Toast.LENGTH_LONG).show();
        }
    }

    private void SignedIn() {
        Intent trader_intent=new Intent(getActivity(),FertilizerLoggedIn.class);
        startActivity(trader_intent);
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

    @Override
    public void sendData(String farmer_choice) {
        if(farmer_choice.equals("camera") || farmer_choice.equals("gallery"))
        {
            TakePhotoOrGallery(farmer_choice);
        }
    }
}
