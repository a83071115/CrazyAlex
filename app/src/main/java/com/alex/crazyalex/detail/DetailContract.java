package com.alex.crazyalex.detail;

import android.webkit.WebView;

import com.alex.crazyalex.BasePresenter;
import com.alex.crazyalex.Baseview;

/**
 * Created by SunQiang on 2017/3/18.
 */

public class DetailContract {
    interface View extends Baseview<Presenter>{
        /**
         * 加载数据
         */
        void showLoading();
        /**
         * 停止加载
         */
        void stopLoading();
        /**
         * 加载错误
         */
        void showLoadingError();
        /**
         * 分享错误
         */
        void showSharingError();

        /**
         * 联网请求
         * @param result  请求数据
         */
        void showResult(String result);

        /**
         * 显示内容
         * @param url 内容url
         */
        void showResultWithoutBody(String url);

        /**
         * 显示封面
         * @param url  内容url
         */
        void showCover(String url);

        /**
         * 设置标题
         * @param title 标题
         */
        void setTitle(String title);

        /**
         * 显示图片模式
         * @param showImage
         */
        void setImageMode(boolean showImage);

        /**
         * 网页没有找到
         */
        void showBrowserNotFoundError();

        /**
         * 显示文本复制
         */
        void showTextCopied();

        /**
         * 显示复制文本错误
         */
        void showCopyTextError();

        /**
         * 添加到收藏夹
         */
        void showAddedToBookmarks();

        /**
         * 取消收藏
         */
        void showDeletedFromBookmarks();

    }
    interface  Presenter extends BasePresenter{
        /**
         * 打开浏览器
         */
        void openInBrowser();
        /**
         * 作为文本分享
         */
        void shareAsText();

        /**
         * 打开WebView
         * @param webView
         * @param url
         */
        void openUrl(WebView webView,String url);

        /**
         * 复制文本
         */
        void copyText();

        /**
         *复制链接
         */
        void copyLink();

        /**
         * 添加或者删除收藏
         */
        void addToOrDeleteFromBookmars();

        /**
         * 判断是否在收藏
         * @return
         */
        boolean queryIfIsBookmarked();

        /**
         * 返回数据
         */
        void requestData();
    }
}
