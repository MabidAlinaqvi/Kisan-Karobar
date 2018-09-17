package com.example.root.formarsupport;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import static com.example.root.formarsupport.farmer_add_crop.mArrayUri;

/**
 * Created by Tom Brain on 2/24/2018.
 */



public class f_cropGridViewAdapter extends BaseAdapter {
    private Context context;
    public f_cropGridViewAdapter(Context context) {
        this.context=context;
    }


//String TAG="naqvi_path";
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

        Picasso.with(context)
                .load(mArrayUri.get(i))
                .resize(100,100)
                .centerCrop()
                .into(imageView);
        return imageView;


    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }
}
