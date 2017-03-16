package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.bean.GuokrHandpickNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class GuokrFragment extends Fragment implements GuokrContract.View {
    public GuokrFragment() {
    }

    public static GuokrFragment newInstance(){
        return new GuokrFragment();
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
    public void Results(ArrayList<GuokrHandpickNews.result> list) {

    }

    @Override
    public void setPresenter(GuokrContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {

    }


}
