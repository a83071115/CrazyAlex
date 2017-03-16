package com.alex.crazyalex.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断当前的网络状态,是否有网络连接,WIFI或者移动数据
 * Created by Administrator on 2017/3/16.
 */

public class NetworkState {
    /**
     * 检查是否连接网络
     */
    public static boolean networkConnected(Context context){
        if(context!=null){
            //得到连接管理者
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态
            NetworkInfo info = manager.getActiveNetworkInfo();
            //判断是否连接网络
            if(info != null){
                return info.isAvailable();
            }
        }
        return false;
    }
    /**
     * 检查移动网络是否连接
     */
    public static boolean mobileDataConnected(Context context){
        if(context!=null){
            //得到连接管理者
            ConnectivityManager manager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(info!=null){
                //判断网络状态是否为移动网络
                if(info.getType()==ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        }
        return false;
    }
    /**
     * 检查WIFI网络是否连接
     */
    public static boolean wifiConnected(Context context){
        if(context!=null){
            //得到连接管理者
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(info!=null){
                if(info.getType()==ConnectivityManager.TYPE_WIFI)
                    return true;
            }
        }
        return false;
    }
}
