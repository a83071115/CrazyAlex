package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.R;
import com.alex.crazyalex.bean.ZhihuDailyNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ZhiHuDailyFragment extends Fragment implements ZhiHuDailyContract.View{
    public ZhiHuDailyFragment() {
    }
    public static ZhiHuDailyFragment newInstance(){
        return new ZhiHuDailyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,null);

        return view;
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showResults(ArrayList<ZhihuDailyNews.Question> list) {

    }

    @Override
    public void showPickDialog() {

    }

    @Override
    public void setPresenter(ZhiHuDailyContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {

    }
}
