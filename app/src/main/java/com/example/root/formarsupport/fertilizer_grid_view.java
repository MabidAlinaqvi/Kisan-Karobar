package com.example.root.formarsupport;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static com.example.root.formarsupport.Fertilizer_medic_info.mArrayfariti;


/**
 * Created by Tom Brain on 5/15/2018.
 */

public class fertilizer_grid_view extends BaseAdapter{
    private Context context;
    public fertilizer_grid_view(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {

        return mArrayfariti.size();
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
                .load(mArrayfariti.get(i))
                .resize(100,100)
                .centerCrop()
                .into(imageView);
        return imageView;

    }
}