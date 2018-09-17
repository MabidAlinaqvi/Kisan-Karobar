package com.example.root.formarsupport;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

/**
 * Created by Tom Brain on 4/21/2018.
 */

public class bookmarked_adapter extends RecyclerView.Adapter<bookmarked_adapter.MyViewHolder>{
    private Context context;
    private ArrayList<cardview_model_farmer_firstpage> userAdsData;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference,current_id_reference;
    DatabaseReference bookmarkReference;
    fieldsValue fieldIs;
    static cardview_model_farmer_firstpage visitdata;
    String id,current_user,user_id;

    public bookmarked_adapter(){

    }

    public bookmarked_adapter(Context Context, ArrayList<cardview_model_farmer_firstpage> userAdsData) {
        this.context = Context;
        this.userAdsData= userAdsData;

        Log.d("imran", userAdsData.size()+"");
    }
    @Override
    public bookmarked_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_farmer_first_page, parent, false);
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

        return new bookmarked_adapter.MyViewHolder(itemView);
    }
    @Override
    public int getItemCount() {
        return userAdsData.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView product,city,commentNo,distt,id;
        public RatingBar rating;
        public CircleImageView imageView;
        public ImageView bookmarks;
        public View.OnClickListener onClickListener;

        public MyViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.personImage);
            product=view.findViewById(R.id.product);
            city=view.findViewById(R.id.city);
            commentNo=view.findViewById(R.id.commentNo);
            distt=view.findViewById(R.id.Thsil);
            id=view.findViewById(R.id.Title);
            rating=view.findViewById(R.id.rateit);
            bookmarks=view.findViewById(R.id.cardbookmark);


            //     view.setOnClickListener(this);
        }


    }

    cardview_model_farmer_firstpage data;
    @Override
    public void onBindViewHolder(final bookmarked_adapter.MyViewHolder holder, final int position) {
        data = userAdsData.get(position);
        try {
            Picasso.with(context)
                    .load(data.getProfile())
                    .placeholder(R.drawable.profile1)
                    .into(holder.imageView);
        }
        catch (Exception e)
        {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.profile1));
        }
        holder.product.setText(data.getProduct());
        holder.id.setText(data.getId());
        holder.city.setText(data.getCity());
        holder.commentNo.setText(data.getComment());
        holder.distt.setText(data.getDistt());
        holder.rating.setRating(Float.parseFloat(data.getRating()));
        holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarked));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitdata=new cardview_model_farmer_firstpage();
                visitdata=userAdsData.get(position);
                id=visitdata.getId();
               // Log.d("IDiS", id);
                ShowRespectiveFragment();
            }
        });
    /*    holder.bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid=mAuth.getInstance().getCurrentUser().getUid();
                visitdata=new cardview_model_farmer_firstpage();
                visitdata=userAdsData.get(position);
                bookmarkReference= FirebaseDatabase.getInstance().getReference("Users").child(uid).child("bookmarks").child(visitdata.getPhone());
                holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarkis));
                bookmarkReference.removeValue();
                RefreshFragment();
                Toast.makeText(context,"successfully remove from bookmark",Toast.LENGTH_SHORT).show();
            }
        });*/

        holder.bookmarks.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                String uid=mAuth.getInstance().getCurrentUser().getUid();
                visitdata = new cardview_model_farmer_firstpage();
                visitdata = userAdsData.get(position);
                bookmarkReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("bookmarks");
                bookmarkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Log.d("GetBooked", visitData.getPhone());
                        if(dataSnapshot.hasChild(visitdata.getPhone()))
                        {
                            holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarkis));
                            bookmarkReference.child(visitdata.getPhone()).removeValue();
                            RefreshFragment();
                            Toast.makeText(context,"unbooked",Toast.LENGTH_SHORT).show();
                        }
                       /* else
                        {
                            bookmarkReference.child(visitdata.getPhone()).setValue(visitdata);
                            int book=Integer.parseInt(visitdata.getBook());
                            if(book==1)
                            {
                                holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarked));
                                bookmarkReference.child(visitdata.getPhone()).child("book").setValue("2");
                                Toast.makeText(context, "successfully bookmarked", Toast.LENGTH_SHORT).show();
                            }
                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void RefreshFragment() {
        FragmentTransaction transaction;
        FragmentManager manager=((AppCompatActivity)context).getFragmentManager();
        transaction=manager.beginTransaction();
        if(user_id.equals("trader"))
        {
            transaction.replace(R.id.traderFrame,new BookMarkedUsers());
        }
        else if(user_id.equals("farmer"))
        {
            transaction.replace(R.id.frame3,new BookMarkedUsers());
        }
        else if(user_id.equals("broker")) {
            transaction.replace(R.id.brokerFrame,new BookMarkedUsers());
        }
        else{
            transaction.replace(R.id.Fertilizer_Frame,new BookMarkedUsers());
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void ShowRespectiveFragment() {
        // Log.d("ValuesREader", visitData.getPhone());
        Log.d("VisitDataWas", "ShowRespectiveFragment: ");
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(visitdata.getPhone()).child("personal").child("field");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    try {


                        Log.d("VisitDataWas", dataSnapshot + "");
                        String field = dataSnapshot.getValue().toString();
                        Log.d("VisitDataWas", field + "");
                        if (field.equals("crop") || field.equals("fruit")) {
                            moveToFragment(id);
                        } else {
                            Toast.makeText(context, "No given", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        moveToFragment(id);
                    }
                }
                else{
                    moveToFragment(id);
                }

              //   Log.d("ValuesREader", dataSnapshot+"");
              //  fieldIs=dataSnapshot.getValue(fieldsValue.class);
                // Log.d("ValuesREader", fieldIs+"");
             //   String field=fieldIs.getField();
                //  Log.d("ValuesREader", field);
                Log.d("User_ID", user_id);
                if(user_id.equals("broker"))
                {

                }
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void moveToFragment(String id) {
        if(id.equals("کسان"))
        {
            Click_to_visit_farmerProfile farmerprofile=new Click_to_visit_farmerProfile();
            FragmentManager manager=((AppCompatActivity)context).getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            if(user_id.equals("trader"))
            {
                transaction.replace(R.id.traderFrame,farmerprofile);
            }
            else if(user_id.equals("farmer"))
            {
                transaction.replace(R.id.frame3,farmerprofile);
            }
            else if(user_id.equals("broker")) {
                transaction.replace(R.id.brokerFrame,farmerprofile);
            }
            else{
                transaction.replace(R.id.Fertilizer_Frame,farmerprofile);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id.equals("آڑتی"))
        {
            Click_to_visit_traderProfile traderProfile=new Click_to_visit_traderProfile();
            FragmentManager manager=((AppCompatActivity)context).getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            if(user_id.equals("trader"))
            {
                transaction.replace(R.id.traderFrame,traderProfile);
            }
            else if(user_id.equals("farmer"))
            {
                transaction.replace(R.id.frame3,traderProfile);
            }
            else if(user_id.equals("broker")) {
                transaction.replace(R.id.brokerFrame,traderProfile);
            }
            else{
                transaction.replace(R.id.Fertilizer_Frame,traderProfile);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id.equals("بروکر"))
        {
            Click_to_visit_brokerProfile brokerProfile=new Click_to_visit_brokerProfile();
            FragmentManager manager=((AppCompatActivity)context).getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            if(user_id.equals("trader"))
            {
                transaction.replace(R.id.traderFrame,brokerProfile);
            }
            else if(user_id.equals("farmer"))
            {
                transaction.replace(R.id.frame3,brokerProfile);
            }
            else if(user_id.equals("broker")) {
                transaction.replace(R.id.brokerFrame,brokerProfile);
            }
            else{
                transaction.replace(R.id.Fertilizer_Frame,brokerProfile);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }

        else if(id.equals("ادویات فروش"))
        {
            Click_to_visit_fertilizerProfile brokerProfile=new Click_to_visit_fertilizerProfile();
            FragmentManager manager=((AppCompatActivity)context).getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            if(user_id.equals("trader"))
            {
                transaction.replace(R.id.traderFrame,brokerProfile);
            }
            else if(user_id.equals("farmer"))
            {
                transaction.replace(R.id.frame3,brokerProfile);
            }
            else if(user_id.equals("broker")) {
                transaction.replace(R.id.brokerFrame,brokerProfile);
            }
            else{
                transaction.replace(R.id.Fertilizer_Frame,brokerProfile);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
       /* else if(id.equals(""))
        {
           Click_to_visit_fertilizerProfile fertilizerProfile=new Click_to_visit_fertilizerProfile();
            FragmentManager manager=((AppCompatActivity)context).getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.frame3,fertilizerProfile);
            transaction.addToBackStack(null);
            transaction.commit();
        }*/

    }

}
class fieldsValue {
    private String field;

    public fieldsValue() {

    }

    public fieldsValue(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
