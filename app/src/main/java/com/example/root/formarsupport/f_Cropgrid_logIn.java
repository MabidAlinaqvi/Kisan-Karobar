package com.example.root.formarsupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.BaseAdapter;

/**
 * Created by Tom Brain on 3/16/2018.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by Tom Brain on 2/24/2018.
  */


public class f_Cropgrid_logIn extends BaseAdapter {
    private Context context;
    private Uri gridimageUri;
    private ArrayList<Uri> Uri_Array;
    public f_Cropgrid_logIn(Context context, ArrayList<Uri> Uri_Array) {
        this.context=context;
        this.Uri_Array=Uri_Array;
    }

    @Override
    public int getCount() {
        return Uri_Array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    String TAG="naqvi_path";
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setImageURI(null);
       /* InputStream inputStream= null;
        try {
            inputStream = context.getContentResolver().openInputStream(mArrayfarmerUri.get(i));
            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        Log.d("naqviArray", Uri_Array.get(i)+"        kjhkjhkh");
        Glide.with(context)
                .load(Uri_Array.get(i))
                .into(imageView);
       /* Log.d("mArrayfarmerUri", mArrayfarmerUri.get(i)+"");
        Picasso.with(context)
                .load(mArrayfarmerUri.get(i))
                .resize(100,100)
                .centerCrop()
                .into(imageView);*/
        //imageView.setImageURI();
        return imageView;

    }
}

