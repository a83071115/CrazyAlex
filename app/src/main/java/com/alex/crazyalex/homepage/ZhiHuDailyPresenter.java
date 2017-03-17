package com.alex.crazyalex.homepage;

import android.content.Context;
import android.util.Log;

import com.alex.crazyalex.bean.StringModeImpl;
import com.alex.crazyalex.bean.ZhihuDailyNews;
import com.alex.crazyalex.interfaze.OnStringListener;
import com.alex.crazyalex.util.Api;
import com.alex.crazyalex.util.DateFormatter;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ZhiHuDailyPresenter implements ZhiHuDailyContract.Presenter {
    private ZhiHuDailyContract.View mView;
    private Context mContext;
    private StringModeImpl mMode;
    private Gson mGson = new Gson();

    private DateFormatter mFormatter = new DateFormatter();
    private ArrayList<ZhihuDailyNews.Question> list = new ArrayList<>();
    public ZhiHuDailyPresenter(Context context, ZhiHuDailyContract.View view) {
        this.mContext = context;
        this.mView = view;
        this.mView.setPresenter(this);
        mMode = new StringModeImpl(mContext);
    }

    @Override
    public void loadPosts(long date, final boolean clearing) {
        String url = Api.ZHIHU_HISTORY + mFormatter.ZhiHuDailyDeteFormat(date);
        Log.e("alex",url);
        mMode.load(url, new OnStringListener() {
            @Override
            public void onSuccess(String result) {
                Log.e("alex","成功");
                try {
                    ZhihuDailyNews post = mGson.fromJson(result,ZhihuDailyNews.class);
                    if(clearing){
                        list.clear();
                    }
                    for (ZhihuDailyNews.Question item: post.getStories()){
                        list.add(item);
                    }
                    mView.showResults(list);
                } catch (JsonSyntaxException e){
                    mView.showError();
                }
                mView.showLoading();

            }


            @Override
            public void onError(VolleyError error) {
                mView.stopLoading();
                mView.showError();
                Log.e("alex","失败");
            }
        });
    }

    @Override
    public void refresh() {

    }

    @Override
    public void loadMore(long date) {

    }

    @Override
    public void startReading(int position) {

    }

    @Override
    public void feelLucky() {

    }

    @Override
    public void start() {
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }
}
