package com.alex.crazyalex.search;

import com.alex.crazyalex.BasePresenter;
import com.alex.crazyalex.Baseview;
import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.ZhihuDailyNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/22.
 */

public class SearchContract {
    interface View extends Baseview<Presenter>{
        void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList,
                         ArrayList<GuokrHandpickNews.result> guokrList,
                         ArrayList<DoubanMomentNews.posts> doubanList,
                         ArrayList<Integer> types);

        void showLoading();

        void stopLoading();


    }

    interface  Presenter extends BasePresenter{

        void loadResults(String queryWords);
        void startReading(BeanType type ,int position);
    }
}
