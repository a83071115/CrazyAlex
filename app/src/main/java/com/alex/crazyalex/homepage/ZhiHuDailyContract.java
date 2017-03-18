package com.alex.crazyalex.homepage;

import com.alex.crazyalex.BasePresenter;
import com.alex.crazyalex.Baseview;
import com.alex.crazyalex.bean.ZhihuDailyNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface ZhiHuDailyContract {
    /**
     * 知乎界面的显示接口
     */
    interface View extends Baseview<Presenter>{
        /**
         * 显示错误时
         */
        void showError();
        /**
         * 显示加载时
         */
        void showLoading();
        /**
         * 停止加载时
         */
        void stopLoading();


        /**
         * 显示结果
         * @param list 知乎的接口Url
         */
        void showResults(ArrayList<ZhihuDailyNews.Question> list);

    }

    /**
     * 知乎界面的操作接口
     */
    interface Presenter extends BasePresenter{
        /**
         * 请求数据
         * @param date
         */
        void loadPosts(long date,boolean clearing);
        /**
         * 刷新数据
         */
        void refresh();
        /**
         * 加载更多
         */
        void loadMore(long date);
        /**
         * 显示详情
         */
        void startReading(int position);
        /**
         * 随便看看
         */
        void feelLucky();
    }
}
