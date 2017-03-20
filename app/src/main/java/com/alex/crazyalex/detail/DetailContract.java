package com.alex.crazyalex.detail;

import android.webkit.WebView;

import com.alex.crazyalex.BasePresenter;
import com.alex.crazyalex.Baseview;

/**
 * Created by SunQiang on 2017/3/18.
 */

public class DetailContract {

        interface View extends Baseview<Presenter> {
            /**
             * 显示正在加载
             */
            void showLoading();

            /**
             * 停止加载
             */
            void stopLoading();

            /**
             * 显示加载错误
             */
            void showLoadingError();

            /**
             * 显示分享时错误
             */
            void showSharingError();

            /**
             * 正确获取数据后显示内容
             *
             * @param result
             */
            void showResult(String result);

            /**
             * 对于body字段的消息,直接接在url的内容
             *
             * @param url
             */
            void showResultWithoutBody(String url);

            /**
             * 显示顶部大图
             *
             * @param url 大图地址
             */
            void showCover(String url);

            /**
             * 设置标题
             *
             * @param title
             */
            void setTitle(String title);

            /**
             * 设置是否显示图片
             *
             * @param showImage
             */
            void setImageMode(boolean showImage);

            /**
             * 用户选择在浏览器中打开时,如果没有安装浏览器,显示没有找到浏览器错误
             */
            void showBrowserNotFoundError();

            /**
             * 显示复制文字内容
             */
            void showTextCopied();

            /**
             * 显示复制文字失败
             */
            void showCopyTextError();

            /**
             * 添加或删除收藏
             */
            void showAddedToBookmarks();

            /**
             * 显示已从收藏夹中移除
             */
            void showDeletedFromBookmarks();

        }

        interface Presenter extends BasePresenter {

            /**
             * 在浏览器中打开
             */
            void openInBrowser();

            /**
             * 作为文字分享
             */
            void shareAsText();

            /**
             * 打开文章中的链接
             *
             * @param webView
             * @param url
             */
            void openUrl(WebView webView, String url);

            /**
             * 复制文字内容
             */
            void copyText();

            /**
             * 复制链接
             */
            void copyLink();

            /**
             * 添加至收藏夹或者从收藏夹中移除
             */
            void addToOrDeleteFromBookmarks();

            /**
             * 查询是否已经被收藏了
             *
             * @return
             */
            boolean queryIfIsBookmarked();

            /**
             * 请求数据
             */
            void requestData();

        }

}
