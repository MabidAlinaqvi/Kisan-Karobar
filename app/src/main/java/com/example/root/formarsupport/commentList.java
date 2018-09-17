package com.example.root.formarsupport;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.root.formarsupport.Click_to_visit_brokerProfile.toOpenCommentbroker;
import static com.example.root.formarsupport.Click_to_visit_farmerProfile.toOpenCommentFarmer;
import static com.example.root.formarsupport.Click_to_visit_traderProfile.toOpenComment;


/**
 * A simple {@link Fragment} subclass.
 */
public class commentList extends Fragment implements View.OnClickListener {
    View view;
    DatabaseReference databaseReference,currentUserReference,databaseReference2,current_id_reference;
    FirebaseDatabase firebaseDatabase;
    comment_cardView_model model_data;
    comment_cardView_model exist_data;
    ArrayList<comment_cardView_model> model_array;
    comment_carView_adapter adapter;
    RecyclerView recyclerView;
    EditText commenttext;
    private Toolbar mToolbar;
    ImageView commentBtn;
    String uid,current_user,user_id;
    ArrayList<String > userUids;
    cardview_model_farmer_firstpage personalModel;
    String comment;
    int sizeIs=0,already=1;
    static String CommentIs;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String server_url = "http://"+ LoginActivity.server_ip+"/firebase.php";
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_comment_list, container, false);
        commenttext=view.findViewById(R.id.writeComment);
        mToolbar=view.findViewById(R.id.comment_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.comment));
       // comment=commenttext.getTag().toString();
        commentBtn=view.findViewById(R.id.postcomment);
        model_data=new comment_cardView_model();
        personalModel=new cardview_model_farmer_firstpage();
        exist_data=new comment_cardView_model();
        recyclerView=view.findViewById(R.id.recycler_view2);
        firebaseDatabase=FirebaseDatabase.getInstance();
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        commentBtn.setOnClickListener(this);
        current_user=mAuth.getCurrentUser().getUid();
        current_id_reference=FirebaseDatabase.getInstance().getReference("Users").child(current_user).child("personal").child("id");
        current_id_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("SingleValue", dataSnapshot+"");
                user_id=(String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // Log.d("commentPosted", toOpenComment);
      /*  currentUserReference=firebaseDatabase.getReference("cardview");
        currentUserReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                personalModel=dataSnapshot.getValue(cardview_model_farmer_firstpage.class);
                if(uid.equals(personalModel.getPhone())) {
                    currentUser = personalModel.getPhone();
                    Log.d("CurrenUseris", currentUser);
                    Log.d("CurrenUseris", FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //
        if(toOpenComment==null && toOpenCommentbroker==null) {
            CommentIs=toOpenCommentFarmer;
        }
        else if(toOpenCommentFarmer==null && toOpenCommentbroker==null) {
            CommentIs = toOpenComment;
        }
        else if(toOpenComment==null && toOpenCommentFarmer==null)
        {
            CommentIs=toOpenCommentbroker;
        }

        Log.d("farmerComment", CommentIs);
        databaseReference=firebaseDatabase.getReference("Users").child(CommentIs).child("comments");
        model_array=new ArrayList<comment_cardView_model>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("IdOfUser",dataSnapshot+"");
                model_data=dataSnapshot.getValue(comment_cardView_model.class);
            //    currentUser=model_data.getUid();
            //    Log.d("CurrentUserhas", currentUser);
                model_array.add(model_data);
                adapter = new comment_carView_adapter(getActivity(), model_array);
             //   Log.d("IdOfUser", adapter+"");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



/*commentBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});*/

        return view;
    }

    private void AlreadyCommentOrNot(comment_cardView_model model_data) {
   //     Log.d("commnetBtnclick", "already comment or not");
        userUids=new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                exist_data=dataSnapshot.getValue(comment_cardView_model.class);
                userUids.add(exist_data.getUid());
                CheckingExistance();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
  //      Log.d("commnetBtnclick", "checking loops");

    }

    private void CheckingExistance() {
      /*  Log.d("Howtimes", userUids.size()+"    userUids");
        Log.d("Howtimes", model_array.size()+"   model_array");*/
        Log.d("ArraySizes", model_array.size()+"   model_array");
        Log.d("ArraySizes", userUids.size()+"   model_array");
        if (model_array.size() == userUids.size()) {
            Log.d("Howtimes", userUids.size()+"   inside loop");
            for (sizeIs = 0; sizeIs < userUids.size(); sizeIs++) {
                if (uid.equals(userUids.get(sizeIs))) {
                    if(already==2) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.already_comment), Toast.LENGTH_SHORT).show();
                        already=1;
                    }
                    already=already+1;
                    break;
                }
                if (sizeIs + 1 == userUids.size()) {
                    databaseReference.child(uid).setValue(model_data);
                    sendData(model_data.getComment(),uid);
                    RefreshFragment();
                    break;
                }

            }
        }
    }

    private void RefreshFragment() {
        commentList commentlist=new commentList();
        FragmentTransaction transaction;
        FragmentManager manager=getFragmentManager();
        transaction=manager.beginTransaction();
        if(user_id.equals("trader"))
        {
            transaction.replace(R.id.traderFrame,commentlist);
        }
        else if(user_id.equals("farmer"))
        {
            transaction.replace(R.id.frame3,commentlist);
        }
        else if(user_id.equals("broker")) {
            transaction.replace(R.id.brokerFrame, commentlist);
        }
        else{
            transaction.replace(R.id.Fertilizer_Frame,commentlist);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
/*
    public void saveComment(View view) {
        Log.d("ViewIdIs", view.getId()+"");
        Log.d("ViewIdIs", commentBtn.getId()+"");
    }
*/

    @Override
    public void onClick(View view) {
        if(view.getId()==commentBtn.getId())
        {
            comment=commenttext.getText().toString();

            if(TextUtils.isEmpty(comment))
            {
                Toast.makeText(getActivity(),getResources().getString(R.string.commentPlease),Toast.LENGTH_SHORT).show();
            }
            else
            {
                // uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                currentUserReference=firebaseDatabase.getReference("cardview");
                currentUserReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        personalModel=dataSnapshot.getValue(cardview_model_farmer_firstpage.class);
                        if(uid.equals(personalModel.getPhone()))
                        {
                            Log.d("ChildComment", CommentIs);
                            model_data=new comment_cardView_model(personalModel.getName(),comment,personalModel.getProfile(),personalModel.getPhone());
                           DatabaseReference commentNodeReference=firebaseDatabase.getReference("Users").child(CommentIs);
                           commentNodeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if(dataSnapshot.hasChild("comments"))
                                   {
                                       Log.d("ChildComment", "yes");
                                       AlreadyCommentOrNot(model_data);
                                   }
                                   else{
                                       Log.d("ChildComment", "no");
                                       databaseReference.child(uid).setValue(model_data);
                                       sendData(model_data.getComment(),uid);
                                       RefreshFragment();
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });

                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }



    private void sendData(final String selected, final String getuser) {
        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("out"))
                        {
                            Toast.makeText(getActivity(),"Out Of Stock",Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("selected", selected);
                params.put("user", uid);
                params.put("receive",CommentIs);
                return params;
            }

        };
        Make_Request_Queue.getInstance(getActivity()).addtorequestque(request);
    }


}
