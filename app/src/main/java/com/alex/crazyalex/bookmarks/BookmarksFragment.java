package com.alex.crazyalex.bookmarks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.ZhihuDailyNews;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class BookmarksFragment extends Fragment implements BookmarksContract.View {


    public static BookmarksFragment newInstance(){
        return new BookmarksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList, ArrayList<GuokrHandpickNews.result> guokrList, ArrayList<DoubanMomentNews.posts> doubanList, ArrayList<Intent> types) {

    }

    @Override
    public void notifyDataChanged() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void setPresenter(BookmarksContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {

    }
}
