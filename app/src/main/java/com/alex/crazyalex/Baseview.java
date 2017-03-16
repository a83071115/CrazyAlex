package com.alex.crazyalex;

import android.view.View;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface Baseview<T> {
    /**
     * 为View设置Presenter
     */

    void setPresenter(T presenter);
    /**
     *  初始化界面控制
     */
    void initViews(View view);
//    /**
//     * 显示错误时
//     */
//    void showError();
//    /**
//     * 显示加载时
//     */
//    void showLoading();
//    /**
//     * 停止加载时
//     */
//    void stopLoading();
}
