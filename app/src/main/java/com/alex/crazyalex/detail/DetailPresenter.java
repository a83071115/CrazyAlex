package com.alex.crazyalex.detail;

import android.webkit.WebView;

import com.alex.crazyalex.bean.BeanType;

/**
 * Created by SunQiang on 2017/3/18.
 */

public class DetailPresenter implements DetailContract.Presenter {
    public void setTypy(BeanType typy) {
        this.typy = typy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    private BeanType typy; //类型
    private int id;
    private String title;
    private String coverUrl; //封面图片链接
    public DetailPresenter(DetailActivity activity, DetailContract.View view) {

    }

    @Override
    public void openInBrowser() {

    }

    @Override
    public void shareAsText() {

    }

    @Override
    public void openUrl(WebView webView, String url) {

    }

    @Override
    public void copyText() {

    }

    @Override
    public void copyLink() {

    }

    @Override
    public void addToOrDeleteFromBookmars() {

    }

    @Override
    public boolean queryIfIsBookmarked() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @Override
    public void start() {

    }
}
