package com.alex.crazyalex;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface BasePresenter {
    /**
     * 获取数据并改变界面显示,在todo-mvp的项目中的调用时机为Fragment的onResume()方法中
     */
    void start();
}
