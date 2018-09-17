/*
package com.example.root.formarsupport;

*/
/**
 * Created by Tom Brain on 3/16/2018.
 *//*


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static com.example.root.formarsupport.Farmer_fruit_click_Own_listItem.ArrayFruitUri;


*/
/**
 *
 * Created by Tom Brain on 2/24/2018.
 *//*


public class f_Fruitgrid_logIn extends BaseAdapter {
    private Context context;
    public f_Fruitgrid_logIn(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {
        return ArrayFruitUri.size();
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

        Picasso.with(context)
                .load(ArrayFruitUri.get(i))
                .resize(100,100)
                .centerCrop()
                .into(imageView);
        //imageView.setImageURI();
        return imageView;

    }
}

*/
