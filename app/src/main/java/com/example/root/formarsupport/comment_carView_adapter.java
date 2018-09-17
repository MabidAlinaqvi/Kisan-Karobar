package com.example.root.formarsupport;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.root.formarsupport.commentList.CommentIs;


/**
 * Created by Tom Brain on 4/22/2018.
 */

public class comment_carView_adapter extends RecyclerView.Adapter<comment_carView_adapter.MyViewHolder> {
    private Context context;
    private ArrayList<comment_cardView_model> userData;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference,current_id_reference;
    DatabaseReference bookmarkReference;
    fieldsValue fieldIs;
    comment_cardView_model visitdata;
    String id,carduid,current_user,user_id;
    cardview_model_farmer_firstpage personalModel;
    comment_cardView_model data;
    private String uid;
    private int editText=0;

    public comment_carView_adapter() {

    }

    public comment_carView_adapter(Context Context, ArrayList<comment_cardView_model> userData) {
        this.context = Context;
        this.userData = userData;

      //  Log.d("imran", userAdsData.size() + "");
    }

    @Override
    public comment_carView_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_cardview, parent, false);
        data=new comment_cardView_model();
        if(userData!=null) {
            DatabaseReference totalcommentReference = FirebaseDatabase.getInstance().getReference("cardview").child(CommentIs);
            totalcommentReference.child("comment").setValue(userData.size() + "");
        }
        else
        {
            DatabaseReference totalcommentReference = FirebaseDatabase.getInstance().getReference("cardview").child(CommentIs);
            totalcommentReference.child("comment").setValue(0+"");
        }

        current_user = mAuth.getInstance().getCurrentUser().getUid();
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
            return new comment_carView_adapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public EditText comment;
        public CircleImageView imageView;
        public Button edit,delete;
        public MyViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.commentImage);
            name=view.findViewById(R.id.commentname);
            comment=view.findViewById(R.id.commentIs);
            edit=view.findViewById(R.id.editcomment);
            delete=view.findViewById(R.id.deletecomment);
            personalModel=new cardview_model_farmer_firstpage();
            uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


    }

    @Override
    public void onBindViewHolder(final comment_carView_adapter.MyViewHolder holder, final int position) {
        data = userData.get(position);
     /* Log.d("IdOfUser", data.getComment()+" modelData");
        Log.d("IdOfUser", data.getName()+" modelData");
        Log.d("IdOfUser", data.getProfile()+" modelData");*/
        Log.d("VisitorUId", data.getUid()+"   uid");
        if (uid.equals(data.getUid())) {
            Log.d("VisitorUId", uid+"    visitor");
            Log.d("VisitorUId", data.getUid()+"   uid");
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }
        if(!data.getProfile().equals("")) {
            Picasso.with(context)
                    .load(data.getProfile())
                    .placeholder(R.drawable.profile1)
                    .into(holder.imageView);
        }
        else
        {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.profile1));
        }
        holder.comment.setText(data.getComment());
        holder.name.setText(data.getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("VisitorUId", CommentIs+"    visitor");
                Log.d("VisitorUId", uid+"   uid");
                DatabaseReference deleteReference = FirebaseDatabase.getInstance().getReference("Users").child(CommentIs).child("comments").child(uid);
                deleteReference.removeValue();
                RefreshFragment();
             //   Toast.makeText(context,"delete please",Toast.LENGTH_SHORT).show();

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText==0) {
                    holder.comment.setFocusableInTouchMode(true);
                    holder.comment.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    holder.edit.setText(context.getResources().getString(R.string.save));
                    editText=editText+1;
                }
                else
                {
                    String edited=holder.comment.getText().toString();
                    if(TextUtils.isEmpty(edited))
                    {
                        Toast.makeText(context,"please write comment",Toast.LENGTH_LONG).show();
                    }
                    else {
                        holder.edit.setText(context.getResources().getString(R.string.editcomment));
                        DatabaseReference editReference = FirebaseDatabase.getInstance().getReference("Users").child(CommentIs).child("comments").child(uid);
                        editReference.child("comment").setValue(edited);
                        holder.comment.setInputType(InputType.TYPE_NULL);
                        holder.comment.setFocusable(false);
                        editText = 0;
                    }
                }
            }
        });
    }

    private void RefreshFragment() {
        commentList commentlist=new commentList();
        FragmentTransaction transaction;
        FragmentManager manager=((AppCompatActivity)context).getFragmentManager();
        transaction=manager.beginTransaction();
        Log.d("user_id_Value", user_id);
        if(user_id.equals("trader"))
        {
            transaction.replace(R.id.traderFrame,commentlist);
        }
        else if(user_id.equals("broker"))
        {
            transaction.replace(R.id.brokerFrame,commentlist);
        }
        else if(user_id.equals("farmer")){
            transaction.replace(R.id.frame3,commentlist);
        }
        transaction.addToBackStack(null);
        transaction.commit();

    }
}

