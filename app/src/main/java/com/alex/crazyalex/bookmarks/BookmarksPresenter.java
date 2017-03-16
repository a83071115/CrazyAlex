package com.alex.crazyalex.bookmarks;

import android.content.Context;

import com.alex.crazyalex.bean.BeanType;

/**
 * Created by Administrator on 2017/3/16.
 */

public class BookmarksPresenter implements BookmarksContract.Presenter {
    private Context mContext;
    private BookmarksContract.View mView;
    public BookmarksPresenter(Context context, BookmarksContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void loadResults(boolean refresh) {

    }

    @Override
    public void startReading(BeanType type, int position) {

    }

    @Override
    public void checkForFreshData() {

    }

    @Override
    public void feelLucky() {

    }

    @Override
    public void start() {

    }
}
