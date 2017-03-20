package com.alex.crazyalex.customtabs;

import android.support.customtabs.CustomTabsClient;

/**
 * Created by Administrator on 2017/3/20.
 */

public interface ServiceConnectionCallback {
    /**
     * 连接时调用服务
     * @param client
     */
    void onServiceConnected(CustomTabsClient client);


    /**
     * 断开连接时调用服务
     */
    void onServiceDisconnected();

}
