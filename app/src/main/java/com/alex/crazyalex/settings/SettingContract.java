package com.alex.crazyalex.settings;


import android.support.v7.preference.Preference;

import com.alex.crazyalex.BasePresenter;
import com.alex.crazyalex.Baseview;

/**
 * Created by Administrator on 2017/3/22.
 */

public interface SettingContract {

    interface View extends Baseview<Presenter>{
        /**
         * 清除图片缓存
         */
        void showCleanGlideCacheDone();
    }
    interface Presenter extends BasePresenter{
        /**
         * 没有图片模式
         * @param preference
         */
        void setNoPictureMode(Preference preference);

        /**
         * 在系统浏览器打开
         * @param preference
         */
        void setInAppBrowser(Preference preference);

        /**
         * 清除图片缓存
         */
        void cleanGlideCache();

        /**
         * 设置文章保存时间
         * @param preference
         * @param newValue
         */
        void setTimeOfSavingArticles(Preference preference , Object newValue);

        /**
         *
         * @return
         */
        String getTimeSummary();
    }

}
