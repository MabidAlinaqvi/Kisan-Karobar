
package com.example.root.formarsupport;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import static com.example.root.formarsupport.LoginActivity.server_ip;


/**
 * Created by root on 12/18/17.
 */


public class FirebaseMessage extends FirebaseMessagingService {
    //https://stackoverflow.com/questions/13902115/how-to-create-a-notification-with-notificationcompat-builder
    private String server_url="http://"+server_ip+"/requested.php";
    String TAG = "data received";
    Bundle bundle=new Bundle();
    String user_name="";
    String comp_name="";
    String title="";
    String reply="";
    private DatabaseReference idReference;
    String id;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        if (remoteMessage.getData()!=null) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            user_name = remoteMessage.getData().get("sender");
            comp_name = remoteMessage.getData().get("message");
            idReference= FirebaseDatabase.getInstance().getReference("Users").child(user_name).child("personal").child("id");
            final Intent result = new Intent(this, Farmer_logged_In.class);
            idReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    id=dataSnapshot.getValue().toString();
                    if(id.equals("farmer"))
                    {
                        title="کسان";
                    }
                    else if(id.equals("trader"))
                    {
                        title="آرھتی";
                    }
                    else if(id.equals("broker"))
                    {
                        title="بروکر";
                    }
                    else{
                        title="ادویات فروش";

                    }
                   //change it next time
                    PendingIntent resultPending =
                            PendingIntent.getActivity(
                                    FirebaseMessage.this,
                                    0,
                                    result,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(FirebaseMessage.this)
                                    .setSmallIcon(R.drawable.farmer_icon)
                                    .setContentTitle(title)
                                    .setContentText(comp_name)
                                    .setAutoCancel(true)
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(resultPending);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, mBuilder.build());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
           /* reply=remoteMessage.getData().get("rejec");*/
            /*if(reply.equals("rejected") || reply.equals("returned"))
            {

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.noti)
                                .setContentTitle(reply)
                                .setContentText(comp_name)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, mBuilder.build());
            }
            else
                if (reply.equals("accepted"))
            {
                Intent resultIntent = new Intent(this, Student_account_new.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.noti)
                                .setContentTitle(reply)
                                .setContentText(comp_name)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(resultPendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, mBuilder.build());

                sendRequest();

            }*/
           /* else
                {*/


        }





}

    private void sendRequest() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("userid",user_name);
                params.put("msgis",comp_name);
                //params.put("statis",comp_status);
                return params;
            }

        };
        Make_Request_Queue.getInstance(getApplicationContext()).addtorequestque(stringRequest);
    }

}
