package com.example.root.formarsupport;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    private Button SignIn;
    private EditText userPhone,User_code;
    private TextView registration,ResendCode;
    Animation anim_alpha = null;
    private String user_choice=null,Former_choice=null,Phone,Code;
    Intent intent;
    Bundle bundle;
    Farmer_signUp farmer_signUp=new Farmer_signUp();
    SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId,DeviceToken,uid;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    int i=0;
    static String server_ip="192.168.40.163";
    private String server_url="http://"+server_ip+"/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gettingIds();
        firebaseAuth=FirebaseAuth.getInstance();
        mCallbacksMethod();
        String signUpText=registration.getText().toString();
        String resendtext=ResendCode.getText().toString();
        UnderlineText(signUpText);
        UnderlineResend(resendtext);
        retrevingFShared();
        anim_alpha=AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(anim_alpha);
                if(i==0) {
                    i=i+1;
                    SignIn.setText(getResources().getString(R.string.verify));
                    Phone = userPhone.getText().toString();
                    SignInToApp();
                }
                else{
                    Phone = userPhone.getText().toString();
                    Code = User_code.getText().toString();
                    VerifyByEnteringCode(mVerificationId,Code);
                }
            }
        });
        ResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(Phone,mResendToken);
            }
        });


    }
    private void mCallbacksMethod() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("FailedVerify", "onVerificationFailed: ");
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                } else if (e instanceof FirebaseApiNotAvailableException) {
                } else if (e instanceof FirebaseNetworkException) {
                }
            }
            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                Log.d("code_verified", mVerificationId);
                mResendToken = token;
                Log.d("code_verified", mResendToken + "");
            }
        };
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                LoginActivity.this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void SignInToApp() {
        Phone = Phone.replaceAll(" ", "");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }
    private void VerifyByEnteringCode(String mVerificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uid=firebaseAuth.getCurrentUser().getUid();
                            String phoneno=firebaseAuth.getCurrentUser().toString();
                            Log.d("CheckingLogin", uid);
                            Log.d("CheckingLogin", phoneno);
                            databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uid).child("personal").child("id");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String id=dataSnapshot.getValue(String.class);
                                    if(id==null)
                                    {
                                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.regiterself),Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        SignedInPerson(id);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            /**
                             * try {
                             DeviceToken= FirebaseInstanceId.getInstance().getToken();
                             Thread.sleep(3000);
                             } catch (InterruptedException e) {
                             e.printStackTrace();
                             }
                             Log.d("sharjeel", DeviceToken);
                             databaseReference
                             .child("device_token")
                             .setValue(DeviceToken)
                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            Intent intent=new Intent(LoginActivity.this,Main2Activity.class);
                            startActivity(intent);
                            }
                            });
                             */
                            //Toast.makeText(R.this, "succesfully register", Toast.LENGTH_SHORT).show();
                            // SendToFirebase();
                            //    i=0;
                            //    signUp.setText(getResources().getString(R.string.));

                        } else {
                            // Codeerror.setVisibility(View.VISIBLE);
                            //   i=0;
                            //Sendcode.setText(getResources().getString(R.string.send));
                            // Sign in failed, display a message and update the UI
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                        // loadingBar.dismiss();

                    }
                });
    }
    private void SignedInPerson(String id) {
        try {
            DeviceToken= FirebaseInstanceId.getInstance().getToken();
           /* DeviceToken=FirebaseServer.refreshedToken;
            Log.d("DeviceTokenIs", DeviceToken);*/
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        saveTokentoServer();
        if(id.equals("farmer"))
        {
            Intent farmer_intent=new Intent(LoginActivity.this,Farmer_logged_In.class);
            startActivity(farmer_intent);
        }
        else if(id.equals("broker"))
        {
            Intent broker_intent=new Intent(LoginActivity.this,BrokerLoggedIn.class);
            startActivity(broker_intent);
        }
        else if(id.equals("trader"))
        {
            Intent trader_intent=new Intent(LoginActivity.this,TraderLoggedIn.class);
            startActivity(trader_intent);
        }
        else{
            Intent fertilizer_intent=new Intent(LoginActivity.this,FertilizerLoggedIn.class);
            startActivity(fertilizer_intent);
        }
    }

    private void saveTokentoServer() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();*/
                Log.d("ParamsValues", error+"");
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
               /* params.put("user_name",get_user_name);
                params.put("user_pass",get_password); */
                Log.d("ParamsValues", uid);
                Log.d("ParamsValues", DeviceToken);
                params.put("use",uid);
                params.put("tok",DeviceToken);
                return params;
            }
        };
        Make_Request_Queue.getInstance(LoginActivity.this).addtorequestque(stringRequest);

    }

    private void UnderlineText(String underline) {
        SpannableString content = new SpannableString(underline);
        content.setSpan(new UnderlineSpan(), 0, underline.length(), 0);
        registration.setText(content);
    }
    private void UnderlineResend(String underline) {
        SpannableString content = new SpannableString(underline);
        content.setSpan(new UnderlineSpan(), 0, underline.length(), 0);
        ResendCode.setText(content);
    }
    private void retrevingFShared() {
        preferences=getSharedPreferences("login", Context.MODE_PRIVATE);
        String Notexit=preferences.getString("user_phone","");   //will return false if doesn't exist "user_name"
        Log.d("SaveValues", Notexit);
                if(!Notexit.equals(""))
                {
                    String name=preferences.getString("user_phone","");
                    String pass=preferences.getString("user_code","");
                    userPhone.setText(name);
                    User_code.setText(pass);
                }
                else{
Toast.makeText(this,"not exits",Toast.LENGTH_SHORT).show();
                }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(LoginActivity.this,"please login",Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInShared();
    }


    private void saveInShared(){
        gettingValues();
        Log.d("SaveValues", Phone+"  "+Code+"  ");
        preferences=getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user_phone",Phone);
        editor.putString("user_code",Code);
        editor.commit();
    }

    private void gettingValues() {
        Phone=userPhone.getText().toString();
        Code=User_code.getText().toString();
    }

    private void gettingIds() {
        SignIn=findViewById(R.id.sign_in_button);
        userPhone=findViewById(R.id.email);
        User_code=findViewById(R.id.password);
        registration=findViewById(R.id.register);
        ResendCode=findViewById(R.id.resendcode);
    }

    //Reference => https://developer.android.com/guide/topics/ui/dialogs.html
    public void showSignUpChoices(View view) {

        view.startAnimation(anim_alpha);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.activity_choose__sign_up__options,null));

        builder.show();
    }
    public void Go_Forward(View view) {
        view.startAnimation(anim_alpha);
        if(user_choice==null)
        {
            Toast.makeText(this,R.string.atleastOne,Toast.LENGTH_LONG).show();
        }
        else
        {
            bundle=new Bundle();
            intent=new Intent(this,Sign_Up_As.class);
            bundle.putString("signUp_as",user_choice);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

    /*private void PlzSelectOne() {

    }*/

    public void ferti_clicked(View view) {
        //Toast.makeText(this,"fertic selected",Toast.LENGTH_SHORT).show();
        user_choice="fertilizer";
    }

    public void broker_clicked(View view) {
        //Toast.makeText(this,"broker_signUP selected",Toast.LENGTH_SHORT).show();
        user_choice="broker";
    }

    public void trader_clicked(View view) {
       // Toast.makeText(this,"trader selected",Toast.LENGTH_SHORT).show();
        user_choice="trader";
    }

    public void farmer_clicked(View view) {
     //   Toast.makeText(this,"farmer selected",Toast.LENGTH_SHORT).show();
        user_choice="farmer";
    }

  /*  *//**
     * farmer choice for registration
     *//*
    public void move_forward(View view)
    {
        if(Former_choice==null)
        {

            Toast.makeText(this,R.string.atleastOne,Toast.LENGTH_LONG).show();
        }
        else{
            farmerChoiceDone= (FarmerChoiceDone) getApplication();
            farmerChoiceDone.sendData(Former_choice);
        }
    }
    public void crops(View view)
    {
        Former_choice="crop";
    }
    public void fruits_vegetables(View view)
    {
        Former_choice="fruit";
    }

    public interface FarmerChoiceDone{

        void sendData(String farmer_choice);
    }*/
}


