package com.alex.crazyalex.bean;

import android.content.Context;

import com.alex.crazyalex.app.VolleySingleton;
import com.alex.crazyalex.interfaze.OnStringListener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Administrator on 2017/3/16.
 */

public class StringModeImpl {
    private Context mContext;

    public StringModeImpl(Context context){
        this.mContext = context;
    }
    public void load(String url, final OnStringListener listener){
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        VolleySingleton.getVolleySingleton(mContext).addToRequestQueue(request);
    }
}
