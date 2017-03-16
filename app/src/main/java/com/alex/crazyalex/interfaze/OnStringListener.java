package com.alex.crazyalex.interfaze;

import com.android.volley.VolleyError;

/**
 * 使用Gson转换Json,所以我们只需要返回类型为String即可
 * Created by Administrator on 2017/3/16.
 */

public interface OnStringListener {
    /**
     * 请求成功时回调
     */
    void onSuccess(String result);

    /**
     * 请求失败时回调
     * @param error 错误信息
     */
    void onError(VolleyError error);
}
