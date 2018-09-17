/*
package com.example.root.formarsupport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.data.DataHolder;

import org.w3c.dom.UserDataHandler;

import java.util.ArrayList;

*/
/**
 * Created by Tom Brain on 3/13/2018.
 *//*


public class farmer_list_adapter extends ArrayAdapter {
    ArrayList list=new ArrayList();
    public farmer_list_adapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    public static class DataHandler{
        ImageView personImage;
        TextView Pprice,Pname,Pcity,Pquant;
        RatingBar rating;
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row=convertView;
        DataHandler handler;
        if(row==null)
        {
            LayoutInflater inflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.farmer_row_layout,parent,false);
            handler=new DataHandler();
            handler.personImage=row.findViewById(R.id.personImage);
            handler.Pname=row.findViewById(R.id.productName);
            handler.Pcity=row.findViewById(R.id.Usercity);
            handler.Pquant=row.findViewById(R.id.ProductQuant);
            handler.Pprice=row.findViewById(R.id.priceOfSack);
            handler.rating=row.findViewById(R.id.ratingBar);
            row.setTag(handler);
        }
        else{
            handler=(DataHandler)row.getTag();
        }
        farmer_list_of_product_data_provider dataProvider;
        dataProvider=(farmer_list_of_product_data_provider) this.getItem(position);
        handler.personImage.setImageResource(dataProvider.getPersonImage());
        handler.Pname.setText(dataProvider.getPname());
        handler.Pcity.setText(dataProvider.getPcity());
        handler.Pprice.setText(dataProvider.getPprice());
        handler.Pquant.setText(dataProvider.getPquantity());
        handler.rating.setRating(dataProvider.getRatingBar());
        return row;
    }
}
*/
