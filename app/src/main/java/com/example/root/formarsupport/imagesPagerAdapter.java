package com.example.root.formarsupport;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Tom Brain on 4/28/2018.
 */

public class imagesPagerAdapter extends PagerAdapter {
Context context;
ArrayList<String> imagesUrl;
LayoutInflater layoutInflater;
    public imagesPagerAdapter() {
    }

    public imagesPagerAdapter(Context context, ArrayList<String> imagesUrl)
    {
        this.context=context;
        this.imagesUrl=imagesUrl;
    }

    @Override
    public int getCount() {
        return imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pagerimagelayout,container,false);

        ImageView imageView = (ImageView) view.findViewById(R.id.pagerimage);
            Picasso.with(context)
                    .load(imagesUrl.get(position))
                    .placeholder(R.drawable.gallery2)
                    .into(imageView);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
