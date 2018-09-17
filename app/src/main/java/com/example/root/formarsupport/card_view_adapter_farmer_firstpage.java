package com.example.root.formarsupport;

/**
 * Created by Tom Brain on 4/18/2018.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import java.sql.Ref;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * https://stackoverflow.com/questions/23212162/how-to-move-from-one-fragment-to-another-fragment-on-click-of-a-imageview-in-and
 */
public class card_view_adapter_farmer_firstpage extends RecyclerView.Adapter<card_view_adapter_farmer_firstpage.MyViewHolder>{
    private Context context;
    private ArrayList<cardview_model_farmer_firstpage> User_card;
    private ArrayList<String> bookedUids=new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference bookmarkReference,bookOrnotReference,current_id_reference;
    fieldValue fieldIs=new fieldValue();
    static cardview_model_farmer_firstpage visitData;
    cardview_model_farmer_firstpage bookOrNot;
    String id,uid,current_user,user_id,user_id_value;
    trader_signUp_infor trader_info;
    int i=0;



    public card_view_adapter_farmer_firstpage(){

    }
    public card_view_adapter_farmer_firstpage(Context Context, ArrayList<cardview_model_farmer_firstpage> User_card) {
        this.context = Context;
        this.User_card= User_card;
        Log.d("imran", User_card.size()+"");
    }

    @Override
    public card_view_adapter_farmer_firstpage.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        return new card_view_adapter_farmer_firstpage.MyViewHolder(itemView);
        // return null;
    }

    @Override
    public int getItemCount() {
        return User_card.size();
    }
 /*   public cardview_model_farmer_firstpage getItem(int i) {
        return User_card.get(getItemCount()-i-1);
    }*/

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView product,city,commentNo,distt,id,comments;
        public RatingBar rating;
        public CircleImageView imageView;
        public ImageView bookmarks;
    //    public View.OnClickListener onClickListener;

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
            uid = mAuth.getInstance().getCurrentUser().getUid();
        }


    }
    cardview_model_farmer_firstpage data;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        data = User_card.get(position);
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
       // holder.rating.setRating(Float.parseFloat(data.getRating()));
        //bookedArray.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitData=new cardview_model_farmer_firstpage();
                visitData=User_card.get(position);
                id=visitData.getId();
                Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                if( id.equals("بروکر") || id.equals("ادویات فروش")) {
                        moveToFragment(id);
                }
                else {
                    ShowRespectiveFragment();
                }
            }
        });
        holder.bookmarks.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "bookmark", Toast.LENGTH_SHORT).show();
                    visitData = new cardview_model_farmer_firstpage();
                    visitData = User_card.get(position);
                    bookmarkReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("bookmarks");
                    bookmarkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                           // Log.d("GetBooked", visitData.getPhone());
                            if(dataSnapshot.hasChild(visitData.getPhone()))
                            {
                                holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarkis));
                                bookmarkReference.child(visitData.getPhone()).removeValue();
                                Toast.makeText(context,"unbooked",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context,"saving book",Toast.LENGTH_SHORT).show();
                                bookmarkReference.child(visitData.getPhone()).setValue(visitData);
                                int book=Integer.parseInt(visitData.getBook());
                                if(book==1)
                                {
                                    holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarked));
                                    bookmarkReference.child(visitData.getPhone()).child("book").setValue("2");
                                    Toast.makeText(context,context.getResources().getString(R.string.save), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                  //  int alpha2=holder.bookmarks.getImageAlpha();
                  //  Log.d("AlphaValue",  holder.bookmarks.getImageTintMode()+"  alpha_2");

            //        i=i+1;
            //    }
               /* else
                {
                    bookmarkReference.removeValue();
                    holder.bookmarks.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmarkis));
                    Toast.makeText(context, "successfully remove from bookmark", Toast.LENGTH_LONG).show();
                    i=0;
                }*/

            }
        });

    }

    private void ShowRespectiveFragment() {
       // Log.d("ValuesREader", visitData.getPhone());
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(visitData.getPhone()).child("personal");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.d("ValuesREader", dataSnapshot+"");
                fieldIs=dataSnapshot.getValue(fieldValue.class);
               // Log.d("ValuesREader", fieldIs+"");
                String field=fieldIs.getField();
              //  Log.d("ValuesREader", field);
                if(field.equals("crop") || field.equals("fruit")) {
                    moveToFragment(id);
                }

                else{
                    Toast.makeText(context,context.getResources().getString(R.string.not_given),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void moveToFragment(String id) {
      //  Log.d("IdOfUser", id);
        if(id.equals("کسان"))
        {
            if(user_id.equals("trader")) {
                Click_to_visit_farmerProfile farmerprofile = new Click_to_visit_farmerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.traderFrame, farmerprofile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("broker"))
            {
                Click_to_visit_farmerProfile farmerprofile = new Click_to_visit_farmerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.brokerFrame, farmerprofile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("fertilizer"))
            {
                Click_to_visit_farmerProfile farmerprofile = new Click_to_visit_farmerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Fertilizer_Frame, farmerprofile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else{
                Click_to_visit_farmerProfile farmerprofile = new Click_to_visit_farmerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame3, farmerprofile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        else if(id.equals("آڑتی"))
        {
            if(user_id.equals("farmer"))
            {
                Click_to_visit_traderProfile traderProfile = new Click_to_visit_traderProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame3, traderProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("broker"))
            {
                Click_to_visit_traderProfile traderProfile = new Click_to_visit_traderProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.brokerFrame, traderProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("fertilizer"))
            {
                Click_to_visit_traderProfile traderProfile = new Click_to_visit_traderProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Fertilizer_Frame, traderProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else
            {
                Click_to_visit_traderProfile traderProfile = new Click_to_visit_traderProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.traderFrame, traderProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        else if(id.equals("بروکر"))
        {
            if(user_id.equals("farmer"))
            {
                Click_to_visit_brokerProfile brokerProfile = new Click_to_visit_brokerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame3, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("trader"))
            {
                Click_to_visit_brokerProfile brokerProfile = new Click_to_visit_brokerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.traderFrame, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("broker"))
            {
                Click_to_visit_brokerProfile brokerProfile = new Click_to_visit_brokerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.brokerFrame, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else {
                Click_to_visit_brokerProfile brokerProfile = new Click_to_visit_brokerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Fertilizer_Frame, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        else if(id.equals("ادویات فروش"))
        {
            if(user_id.equals("farmer"))
            {
                Click_to_visit_fertilizerProfile brokerProfile = new Click_to_visit_fertilizerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame3, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("trader"))
            {
                Click_to_visit_fertilizerProfile brokerProfile = new Click_to_visit_fertilizerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.traderFrame, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else if(user_id.equals("broker"))
            {
                Click_to_visit_fertilizerProfile brokerProfile = new Click_to_visit_fertilizerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.brokerFrame, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else {
                Click_to_visit_fertilizerProfile brokerProfile = new Click_to_visit_fertilizerProfile();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Fertilizer_Frame, brokerProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }

    }

}
class fieldValue
{
    private String field;
    public fieldValue()
    {

    }
    public fieldValue(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}