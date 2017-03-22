package com.alex.crazyalex.bookmarks;

import com.alex.crazyalex.BasePresenter;
import com.alex.crazyalex.Baseview;
import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.ZhihuDailyNews;

import java.util.ArrayList;

/**
 * 收藏的接口
 * Created by Administrator on 2017/3/16.
 */

public interface BookmarksContract {
    /**
     * 收藏界面显示的接口
     */
    interface View extends Baseview<Presenter>{
        /**
         * 显示数据
         * @param zhihuList 知乎
         * @param guokrList 果壳
         * @param doubanList 豆瓣
         * @param types 类型
         */
        void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList,
                          ArrayList<GuokrHandpickNews.result> guokrList,
                          ArrayList<DoubanMomentNews.posts> doubanList,
                          ArrayList<Integer> types
                          );

        /**
         * 更新数据
         */
        void notifyDataChanged();
        /**
         * 显示加载
         */
        void showLoading();
        /**
         * 停止加载
         */
        void stopLoading();
    }

    /**
     * 收藏界面操作的接口
     */
    interface Presenter extends BasePresenter{
        /**
         * 加载数据
         * @param refresh 是否刷新
         */
        void loadResults(boolean refresh);

        /**
         * 显示详情页面
         * @param type 显示的类型
         * @param position 当前的位置
         */
        void startReading(BeanType type,int position);

        /**
         * 检查最新数据
         */
        void checkForFreshData();

        /**
         * 随便看看
         */
        void feelLucky();
    }
}
