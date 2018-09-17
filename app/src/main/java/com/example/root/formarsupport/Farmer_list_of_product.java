/*
package com.example.root.formarsupport;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
*/
/**
 * Refrences
 *
 *//*


*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class Farmer_list_of_product extends Fragment {

View view;
ListView listView;
    String[] ProductNmaes;
    String[] ProductQuantity;
    String[] ProductSackprice;
    String[] Productcity;
    String[] rattingArray;
    farmer_list_adapter adapter;
    int[] images_resources = {
            R.drawable.profile,R.drawable.profile,
            R.drawable.profile,R.drawable.profile,
            R.drawable.profile,R.drawable.profile,
            R.drawable.profile,R.drawable.profile,
            R.drawable.profile
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_farmer_list_of_product, container, false);
    listView=view.findViewById(R.id.listView);
    ProductNmaes=getResources().getStringArray(R.array.productName);
    ProductQuantity=getResources().getStringArray(R.array.productquantity);
    ProductSackprice=getResources().getStringArray(R.array.productPrice);
    Productcity=getResources().getStringArray(R.array.productCity);
    rattingArray=getResources().getStringArray(R.array.rattingBar);
        adapter=new farmer_list_adapter(getActivity(),R.layout.farmer_row_layout);
        listView.setAdapter(adapter);
        for(int i=0;i<images_resources.length;i++)
        {
            farmer_list_of_product_data_provider data_provider=new farmer_list_of_product_data_provider(images_resources[i],ProductNmaes[i],ProductSackprice[i],ProductQuantity[i],Productcity[i],Float.parseFloat(rattingArray[i]));
            adapter.add(data_provider);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             Object object = listView.getItemAtPosition(i);
              //  JSONObject jsonObject = new JSONObject();
               // jsonObject.accumulate("naqvi",(Map)object);
           */
/*JSONObject jsonObject= objectToJSONObject(object);
                Log.d("PrintObject", jsonObject+"");*//*

               // String jsonString = new com.google.gson.Gson().toJson(object)
             //   String data= object.toString();
               // Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                //List<String> elephantList = Arrays.asList(naqvi.split(","));
              //  String[] animalsArray = data.split(",");
              //  Toast.makeText(getActivity(),animalsArray[1]+"   "+animalsArray[0]+"  "+animalsArray[3]+"  ",Toast.LENGTH_SHORT).show();


            }
        });
        return view;
    }
    */
/**\
     * testing method to covert from object to jasonObject
     *//*

    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject)json;
        return jsonObject;
    }

}
*/
