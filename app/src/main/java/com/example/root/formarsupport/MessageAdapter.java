package com.example.root.formarsupport;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tom Brain on 5/5/2018.
 */
/*
* https://android--code.blogspot.com/2015/05/android-relativelayout-align-right.html
* */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewAdapter>  {
    private List<Messages> usermessagelist;
    private FirebaseAuth mAuth;
    private Context context;
    public MessageAdapter(List<Messages> usermessagelist, Context context) {
        this.usermessagelist = usermessagelist;
        this.context=context;
    }

    @Override
    public MessageViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_user_list,parent,false);
        mAuth=FirebaseAuth.getInstance();
        return new MessageViewAdapter(v);
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MessageViewAdapter holder, int position) {
        String message_sender_id=mAuth.getCurrentUser().getUid();
        Messages messages=usermessagelist.get(position);
        /*if(messages.getMessages().startsWith("https://"))
        {
            String fromUserId = messages.getFrom();
            if (fromUserId.equals(message_sender_id)) {
                //holder.picture.setBackgroundResource(R.drawable.message_text_background2);
                //holder.picture.setGravity(Gravity.RIGHT);
                holder.picture.setForegroundGravity(Gravity.RIGHT);
                Picasso.with(context)
                        .load(messages.getMessages())
                        .placeholder(R.drawable.picture)
                        .into(holder.picture);


            } else {
                holder.messageText.setVisibility(View.GONE);
               // holder.messageText.setBackgroundResource(R.drawable.message_text_background);
                holder.picture.setForegroundGravity(Gravity.RIGHT);
                Picasso.with(context)
                        .load(messages.getMessages())
                        .placeholder(R.drawable.picture)
                        .into(holder.picture);
            }



        }*/
            holder.messageText.setText(messages.getMessages());

            String fromUserId = messages.getFrom();
            if (fromUserId.equals(message_sender_id)) {
               /* RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.userProfileImage.getLayoutParams();
                lp.addRule(RelativeLayout.TO);*/
                holder.messageText.setBackgroundResource(R.color.sender);
                holder.messageText.setGravity(Gravity.RIGHT);
                holder.userProfileImage.setVisibility(View.VISIBLE);
                holder.replyImageview.setVisibility(View.GONE);
                if(!messages.getProfile().equals(""))
                {
                    Picasso.with(context)
                            .load(messages.getProfile())
                            .placeholder(R.drawable.profile1)
                            .into(holder.userProfileImage);
                }
                else
                {
                    holder.userProfileImage.setImageDrawable(context.getResources().getDrawable(R.drawable.profile1));
                }

            } else {
                holder.messageText.setBackgroundResource(R.color.reply);
                holder.messageText.setGravity(Gravity.LEFT);
                holder.replyImageview.setVisibility(View.VISIBLE);
                holder.userProfileImage.setVisibility(View.GONE);
                if(!messages.getProfile().equals(""))
                {
                    Picasso.with(context)
                            .load(messages.getProfile())
                            .placeholder(R.drawable.profile1)
                            .into(holder.replyImageview);
                }
                else
                {
                    holder.replyImageview.setImageDrawable(context.getResources().getDrawable(R.drawable.profile1));
                }

            }
    }

    @Override
    public int getItemCount() {
        return usermessagelist.size();
    }

    public class MessageViewAdapter extends RecyclerView.ViewHolder {
        public TextView messageText;
       // public ImageView picture;
        public CircleImageView userProfileImage,replyImageview;
        public MessageViewAdapter(View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.message_text);
       //     picture=itemView.findViewById(R.id.messageProfile);
              userProfileImage=itemView.findViewById(R.id.circleImage);
              replyImageview=itemView.findViewById(R.id.circleImage2);
        }
    }
}
