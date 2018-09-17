package com.example.root.formarsupport;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by root on 12/14/17.
 */
//https://www.youtube.com/watch?v=bbwmviiqd3s
public class Make_Request_Queue {
    private static Make_Request_Queue mInstance;  // why created this
    private RequestQueue requestQueue;
    private static Context mCtx;
    private Make_Request_Queue(Context context)
    {
        mCtx=context;
        requestQueue=getRequestQueue();
    }
    public static synchronized Make_Request_Queue getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance=new Make_Request_Queue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addtorequestque(Request<T> request)
    {
        requestQueue.add(request);
    }
}
