package com.alex.crazyalex.homepage;

import com.alex.crazyalex.BasePresenter;
import com.alex.crazyalex.Baseview;
import com.alex.crazyalex.bean.GuokrHandpickNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface GuokrContract {
    /**
     * 果壳的界面显示接口
     */
    interface View extends Baseview<Presenter>{
        /**
         * 显示错误
         */
        void showError();
        /**
         * 显示加载
         */
        void showLoading();
        /**
         * 停止加载
         */
        void stopLoading();
        /**
         * 显示结果
         */
        void Results(ArrayList<GuokrHandpickNews.result> list);
    }

    /**
     * 果壳的界面操作接口
     */
    interface Presenter extends BasePresenter{
        /**
         * 加载更多
         */
        void loadMore(long date);
        /**
         * 刷新数据
         */
        void refresh();

        /**
         * 显示详情界面
         * @param position 点击的位置
         */
        void startReading(int position);
        /**
         * 随便看看
         */
        void feelLucky();
        /**
         * 请求数据
         */
        /**
         * 请求数据
         * @param date
         */
        void loadPosts(long date, boolean clearing);

    }
}
