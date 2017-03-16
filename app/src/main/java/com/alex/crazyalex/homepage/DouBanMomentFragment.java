package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.bean.DoubanMomentNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class DouBanMomentFragment extends Fragment implements DouBanMomentContract.View {
    public DouBanMomentFragment() {
    }

    public static DouBanMomentFragment newInstance(){
        return new DouBanMomentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
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
    public void showResults(ArrayList<DoubanMomentNews.posts> list) {

    }

    @Override
    public void showPickDialog() {

    }


    @Override
    public void setPresenter(DouBanMomentContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {

    }
}
