package com.example.root.formarsupport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckCurrentUser extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent startpageIntent = new Intent(CheckCurrentUser.this, LoginActivity.class);
         //   startpageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //user will be unable to press back button
            startActivity(startpageIntent);
            finish();
        } else {
            String uid=currentUser.getUid();
            databaseReference=firebaseDatabase.getReference("Users").child(uid).child("id");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String id=dataSnapshot.getValue(String.class);
                    FirebaseAuth.getInstance().signOut();
                    Intent startpageIntent = new Intent(CheckCurrentUser.this, LoginActivity.class);
                    startActivity(startpageIntent);
                //    SignedInPerson(id);
                   // Log.d("idIs", id);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

    }

    private void SignedInPerson(String id) {
        if(id.equals("farmer"))
        {
            Intent farmer_intent=new Intent(CheckCurrentUser.this,Farmer_logged_In.class);
            startActivity(farmer_intent);
        }
        else if(id.equals("broker"))
        {
            Intent broker_intent=new Intent(CheckCurrentUser.this,BrokerLoggedIn.class);
            startActivity(broker_intent);
        }
        else if(id.equals("trader"))
        {
            Intent trader_intent=new Intent(CheckCurrentUser.this,TraderLoggedIn.class);
            startActivity(trader_intent);
        }
        else{
            Intent fertilizer_intent=new Intent(CheckCurrentUser.this,FertilizerLoggedIn.class);
            startActivity(fertilizer_intent);
        }
    }
}
