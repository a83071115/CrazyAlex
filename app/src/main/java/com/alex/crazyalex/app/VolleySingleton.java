package com.alex.crazyalex.app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 创建VolleySingleton,即Volley的单例.这样,整个应用就可以只维护一个请求队列,加入新的网络请求也会更加的方便
 * Created by Administrator on 2017/3/16.
 */

public class VolleySingleton {
    public static VolleySingleton volleySingleton;

    private RequestQueue mRequestQueue;
    private VolleySingleton(Context context){
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * 单例模式
     * @param context 上下文
     * @return
     */
    public static synchronized VolleySingleton getVolleySingleton(Context context){
        if(volleySingleton==null){
            volleySingleton = new VolleySingleton(context);
        }
        return volleySingleton;
    }
    /**
     * 请求队列
     */
    public RequestQueue getRequestQueue(){
        return mRequestQueue ;
    }
    /**
     * 添加队列请求
     */
    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
