package com.alex.crazyalex.detail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alex.crazyalex.R;

/**
 * Created by SunQiang on 2017/3/18.
 */

public class DetailFragment extends Fragment implements DetailContract.View{

    private ImageView imageView;
    private WebView webView;
    private NestedScrollView scrollView;
    private CollapsingToolbarLayout toolbarLayout;
    private SwipeRefreshLayout refreshLayout;
    private Context mContext;
    private DetailContract.Presenter presenter;

    public DetailFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.universal_read_layout,container,false);
        initViews(view);
        //设置有选项菜单
        setHasOptionsMenu(true);
        presenter.requestData();
        view.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置平滑滚动坐标
                scrollView.smoothScrollBy(0,0);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.requestData();
            }
        });

        return  view;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showSharingError() {

    }

    @Override
    public void showResult(String result) {

    }

    @Override
    public void showResultWithoutBody(String url) {

    }

    @Override
    public void showCover(String url) {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setImageMode(boolean showImage) {

    }

    @Override
    public void showBrowserNotFoundError() {

    }

    @Override
    public void showTextCopied() {

    }

    @Override
    public void showCopyTextError() {

    }

    @Override
    public void showAddedToBookmarks() {

    }

    @Override
    public void showDeletedFromBookmarks() {

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        //设置下拉刷新的按钮颜色
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        webView = (WebView) view.findViewById(R.id.web_view);
        //设置webview的滚动条消失
        webView.setScrollbarFadingEnabled(true);

        DetailActivity activity = (DetailActivity) getActivity();

        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) view.findViewById(R.id.image_view);
        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        //能够和js交互
        webView.getSettings().setJavaScriptEnabled(true);
        //缩放,设置不能缩放可以防止页面上出现放大和缩小的图标
        webView.getSettings().setBuiltInZoomControls(false);
        //缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        webView.getSettings().setAppCacheEnabled(false);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                presenter.openUrl(view,url);
                return true;
            }
        });

    }
    private void setCollapsingToolbarLayoutTitle(String title){
        toolbarLayout.setTitle(title);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }
}
