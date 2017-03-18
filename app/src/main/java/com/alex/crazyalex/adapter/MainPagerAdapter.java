package com.alex.crazyalex.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alex.crazyalex.homepage.DouBanMomentFragment;
import com.alex.crazyalex.homepage.GuokrFragment;
import com.alex.crazyalex.homepage.ZhiHuDailyFragment;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    //标题
    private String[] titles;
    private Context mContext;

    public DouBanMomentFragment getDouBanMomentFragment() {
        return mDouBanMomentFragment;
    }

    public GuokrFragment getGuokrFragment() {
        return mGuokrFragment;
    }

    public ZhiHuDailyFragment getZhiHuDailyFragment() {
        return mZhiHuDailyFragment;
    }

    private ZhiHuDailyFragment mZhiHuDailyFragment;
    private GuokrFragment mGuokrFragment;
    private DouBanMomentFragment mDouBanMomentFragment;

    /**
     * 构造器
     * @param fm
     * @param context
     * @param zhiHuDailyFragment
     * @param guokrFragment
     * @param douBanMomentFragment
     */
    public MainPagerAdapter(FragmentManager fm
                            ,Context context
                            ,ZhiHuDailyFragment zhiHuDailyFragment
                            ,GuokrFragment guokrFragment
                            ,DouBanMomentFragment douBanMomentFragment) {
        super(fm);
        this.mContext = context;
        titles = new String[]{
                "知乎日报",
                "果壳精选",
                "豆瓣一刻"
        };

        this.mDouBanMomentFragment = douBanMomentFragment;
        this.mGuokrFragment = guokrFragment;
        this.mZhiHuDailyFragment = zhiHuDailyFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==1){
            return mGuokrFragment;
        }else if(position==2){
            return mDouBanMomentFragment;
        }
        return mZhiHuDailyFragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
