package com.alex.crazyalex.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.webkit.WebView;

import com.alex.crazyalex.R;
import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.DoubanMomentStory;
import com.alex.crazyalex.bean.StringModeImpl;
import com.alex.crazyalex.bean.ZhihuDailyStory;
import com.alex.crazyalex.db.DatabaseHelper;
import com.alex.crazyalex.interfaze.OnStringListener;
import com.alex.crazyalex.util.Api;
import com.alex.crazyalex.util.NetworkState;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by SunQiang on 2017/3/18.
 */


public class DetailPresenter implements DetailContract.Presenter {

    public void setTypy(BeanType type) {
        this.type = type;
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

    private BeanType type; //类型
    private int id;
    private String title;
    private String coverUrl; //封面图片链接
    private DetailContract.View view;
    private ZhihuDailyStory mZhihuDailyStory;
    private String guokrStory;
    private DoubanMomentStory mDoubanMomentStory;
    private Context context;
    private DatabaseHelper dbHelper;
    private SharedPreferences sp;
    private Gson gson ;
    private StringModeImpl model;
    public DetailPresenter(Context  context, DetailContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModeImpl(context);
        sp  = context.getSharedPreferences("user_settings",Context.MODE_PRIVATE);
        dbHelper = new DatabaseHelper(context,"History.db",null,5);
        gson = new Gson();
    }

    @Override
    public void openInBrowser() {
        //判断是否为空
        if(checkNull()){
            view.showLoadingError();
            return;
        }
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);

            switch (type){
                case TYPE_ZHIHU:
                    intent.setData(Uri.parse(mZhihuDailyStory.getShare_url()));
                    break;
                case TYPE_DOUBAN:
                    intent.setData(Uri.parse(mDoubanMomentStory.getShort_url()));
                    break;
                case TYPE_GUOKR:
                    intent.setData(Uri.parse(Api.GUOKR_ARTICLE_LINK_V1 +id));
                    break;
            }
            context.startActivity(intent);
        }catch (android.content.ActivityNotFoundException ex){
            view.showSharingError();
        }
    }

    @Override
    public void shareAsText() {
        if(checkNull()){
            view.showSharingError();
        }
        try {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String shareText = "" + title + "";
            switch (type){
                case TYPE_ZHIHU:
                    //分享的链接
                    shareText+= mZhihuDailyStory.getShare_url();
                    break;
                case TYPE_GUOKR:
                    shareText+=Api.GUOKR_ARTICLE_LINK_V1+id;
                    break;
                case TYPE_DOUBAN:
                    shareText+= mDoubanMomentStory.getShort_url();
            }
            //将链接和文字 发送过去
            shareText = shareText + "\t\t\t" + context.getString(R.string.share_extra) ;
            //启动分享
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
            context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex){
            view.showLoading();
        }
    }

    @Override
    public void openUrl(WebView webView, String url) {
        if (checkNull()) {
            view.showLoadingError();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            switch (type) {
                case TYPE_ZHIHU:
                    intent.setData(Uri.parse(mZhihuDailyStory.getShare_url()));
                    break;
                case TYPE_GUOKR:
                    intent.setData(Uri.parse(Api.GUOKR_ARTICLE_LINK_V1 + id));
                    break;
                case TYPE_DOUBAN:
                    intent.setData(Uri.parse(mDoubanMomentStory.getShort_url()));
            }

            context.startActivity(intent);

        } catch (android.content.ActivityNotFoundException ex){
            view.showBrowserNotFoundError();
        }
    }

    /**
     * 复制文本
     */
    @Override
    public void copyText() {
        if(checkNull()){
            view.showCopyTextError();
            return;
        }
        //剪切板管理器
        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = null;
        switch (type){
            case TYPE_ZHIHU:
                clipData = ClipData.newPlainText("text", Html.fromHtml(title + "\n" + mZhihuDailyStory.getBody().toString()));
                break;
            case TYPE_GUOKR:
                clipData = clipData.newPlainText("text",Html.fromHtml(guokrStory).toString());
                break;
            case TYPE_DOUBAN:
                clipData = clipData.newPlainText("text",Html.fromHtml(title + "\n" + mDoubanMomentStory.getContent().toString()));
        }
        manager.setPrimaryClip(clipData);
        view.showTextCopied();
    }

    /**
     * 复制链接
     */
    @Override
    public void copyLink() {
        if (checkNull()){
            view.showCopyTextError();
            return;
        }

        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = null;
        switch (type){
            case TYPE_ZHIHU:
                clipData = ClipData.newPlainText("text",Html.fromHtml(mZhihuDailyStory.getShare_url()).toString());
                break;
            case TYPE_GUOKR:
                clipData = ClipData.newPlainText("text",Html.fromHtml(Api.GUOKR_ARTICLE_LINK_V1 + id).toString());
                break;
            case TYPE_DOUBAN:
                clipData = ClipData.newPlainText("text",Html.fromHtml(mDoubanMomentStory.getOriginal_url()).toString());
        }
        manager.setPrimaryClip(clipData);
        view.showTextCopied();
    }

    @Override
    public void addToOrDeleteFromBookmarks() {
        String tepTable = "";
        String tepId = "";
        switch (type){
            case TYPE_ZHIHU:
                tepTable = "Zhihu";
                tepId = "zhihu_id";
                break;
            case TYPE_GUOKR:
                tepTable = "Guokr";
                tepId = "guokr_id";
                break;
            case TYPE_DOUBAN:
                tepId = "douban_id";
                tepTable = "Douban";
                break;
            default:
                break;
        }
        //如果查询有这个id
        if(queryIfIsBookmarked()){
            //delete
            //update Zhihu set bookmark = 0 where zhihu_id = id
            ContentValues values = new ContentValues();
            values.put("bookmark",0);
            //修改的语句
            dbHelper.getWritableDatabase().update(tepTable,values,tepId+" = ?",new String[]{String.valueOf(id)});
            values.clear();
            //显示取消收藏
            view.showDeletedFromBookmarks();
        }else{
            //如果没有这个id
            //add
            //update Zhihu set bookmark = 0 where zhihu_id = id
            ContentValues values = new ContentValues();
            values.put("bookmark",1);
            dbHelper.getWritableDatabase().update(tepTable,values,tepId+" = ?",new String[]{String.valueOf(id)});
            values.clear();
            view.showAddedToBookmarks();
        }

    }


    @Override
    public boolean queryIfIsBookmarked() {
        if(id == 0 || type == null){
            view.showLoadingError();
            return false;
        }
        String tempTable = "";
        String tempId = "";
        switch (type){
            case TYPE_ZHIHU:
                tempTable = "Zhihu";
                tempId = "zhihu_id";
                break;
            case TYPE_GUOKR:
                tempTable = "Guokr";
                tempId = "guokr_id";
                break;
            case TYPE_DOUBAN:
                tempId = "douban_id";
                tempTable = "Douban";
                break;
            default:
                break;

        }

        String sql = "select * from " + tempTable + " where " + tempId + " = ?";
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery(sql,new String[]{String.valueOf(id)});
        if(cursor.moveToFirst()){
            do {
                //查询该列的值是多少
                int isBookmarked = cursor.getInt(cursor.getColumnIndex("bookmark"));
                //如果bookmark的值 是1 返回结果是被收藏 否则不被收藏
                if(isBookmarked ==1){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        return false;
    }

    @Override
    public void requestData() {
        if (id == 0 || type == null) {
            view.showLoadingError();
            return;
        }

        view.showLoading();
        view.setTitle(title);
        view.showCover(coverUrl);

        // set the web view whether to show the image
        view.setImageMode(sp.getBoolean("no_picture_mode",false));

        switch (type) {
            case TYPE_ZHIHU:
                if (NetworkState.networkConnected(context)) {
                    model.load(Api.ZHIHU_NEWS + id, new OnStringListener() {
                        @Override
                        public void onSuccess(String result) {
                            {
                                Gson gson = new Gson();
                                try {
                                    mZhihuDailyStory = gson.fromJson(result, ZhihuDailyStory.class);
                                    if (mZhihuDailyStory.getBody() == null) {
                                        view.showResultWithoutBody(mZhihuDailyStory.getShare_url());
                                    } else {
                                        view.showResult(convertZhihuContent(mZhihuDailyStory.getBody()));
                                    }
                                } catch (JsonSyntaxException e) {
                                    view.showLoadingError();
                                }
                                view.stopLoading();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            view.stopLoading();
                            view.showLoadingError();
                        }
                    });
                } else {
                    Cursor cursor = dbHelper.getReadableDatabase()
                            .query("Zhihu", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getInt(cursor.getColumnIndex("zhihu_id")) == id) {
                                String content = cursor.getString(cursor.getColumnIndex("zhihu_content"));
                                try {
                                    mZhihuDailyStory = gson.fromJson(content, ZhihuDailyStory.class);
                                } catch (JsonSyntaxException e) {
                                    view.showResult(content);
                                }
                                view.showResult(convertZhihuContent(mZhihuDailyStory.getBody()));
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                break;

            case TYPE_GUOKR:
                if (NetworkState.networkConnected(context)) {
                    model.load(Api.GUOKR_ARTICLE_LINK_V1 + id, new OnStringListener() {
                        @Override
                        public void onSuccess(String result) {
                            convertGuokrContent(result);
                            view.showResult(guokrStory);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            view.showLoadingError();
                        }
                    });
                } else {
                    Cursor cursor = dbHelper.getReadableDatabase()
                            .query("Guokr", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getInt(cursor.getColumnIndex("guokr_id")) == id) {
                                guokrStory = cursor.getString(cursor.getColumnIndex("guokr_content"));
                                convertGuokrContent(guokrStory);
                                view.showResult(guokrStory);
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                break;

            case TYPE_DOUBAN:
                if (NetworkState.networkConnected(context)) {
                    model.load(Api.DOUBAN_ARTICLE_DETAIL + id, new OnStringListener() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                mDoubanMomentStory = gson.fromJson(result, DoubanMomentStory.class);
                                view.showResult(convertDoubanContent());
                            } catch (JsonSyntaxException e) {
                                view.showLoadingError();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            view.showLoadingError();
                        }
                    });
                } else {
                    Cursor cursor = dbHelper.getReadableDatabase()
                            .rawQuery("select douban_content from Douban where douban_id = " + id, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getCount() == 1) {
                                mDoubanMomentStory = gson.fromJson(cursor.getString(0), DoubanMomentStory.class);
                                view.showResult(convertDoubanContent());
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    break;

                }
            default:
                view.stopLoading();
                view.showLoadingError();
                break;
        }

        view.stopLoading();
    }

    @Override
    public void start() {

    }
    private boolean checkNull() {
        return (type == BeanType.TYPE_ZHIHU && mZhihuDailyStory == null)
                ||(type == BeanType.TYPE_DOUBAN && mDoubanMomentStory==null)
                ||(type == BeanType.TYPE_GUOKR && guokrStory ==null);
    }
    private String convertDoubanContent() {

        if (mDoubanMomentStory.getContent() == null) {
            return null;
        }
        String css;
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_dark.css\" type=\"text/css\">";
        } else {
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_light.css\" type=\"text/css\">";
        }
        String content = mDoubanMomentStory.getContent();
        ArrayList<DoubanMomentNews.posts.thumbs> imageList = mDoubanMomentStory.getPhotos();
        for (int i = 0; i < imageList.size(); i++) {
            String old = "<img id=\"" + imageList.get(i).getTag_name() + "\" />";
            String newStr = "<img id=\"" + imageList.get(i).getTag_name() + "\" "
                    + "src=\"" + imageList.get(i).getMedium().getUrl() + "\"/>";
            content = content.replace(old, newStr);
        }
        StringBuilder builder = new StringBuilder();
        builder.append( "<!DOCTYPE html>\n");
        builder.append("<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        builder.append("<head>\n<meta charset=\"utf-8\" />\n");
        builder.append(css);
        builder.append("\n</head>\n<body>\n");
        builder.append("<div class=\"container bs-docs-container\">\n");
        builder.append("<div class=\"post-container\">\n");
        builder.append(content);
        builder.append("</div>\n</div>\n</body>\n</html>");

        return builder.toString();
    }

    private String convertZhihuContent(String preResult) {

        preResult = preResult.replace("<div class=\"img-place-holder\">", "");
        preResult = preResult.replace("<div class=\"headline\">", "");

        // 在api中，css的地址是以一个数组的形式给出，这里需要设置
        // in fact,in api,css addresses are given as an array
        // api中还有js的部分，这里不再解析js
        // javascript is included,but here I don't use it
        // 不再选择加载网络css，而是加载本地assets文件夹中的css
        // use the css file from local assets folder,not from network
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";


        // 根据主题的不同确定不同的加载内容
        // load content judging by different theme
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES){
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        }

        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preResult)
                .append("</body></html>").toString();
    }

    private void convertGuokrContent(String content) {
        // 简单粗暴的去掉下载的div部分
        this.guokrStory = content.replace("<div class=\"down\" id=\"down-footer\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"\" class=\"app-down\" id=\"app-down-footer\">下载</a>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"down-pc\" id=\"down-pc\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"http://www.guokr.com/mobile/\" class=\"app-down\">下载</a>\n" +
                "    </div>", "");

        // 替换css文件为本地文件
        guokrStory = guokrStory.replace("<link rel=\"stylesheet\" href=\"http://static.guokr.com/apps/handpick/styles/d48b771f.article.css\" />",
                "<link rel=\"stylesheet\" href=\"file:///android_asset/guokr.article.css\" />");

        // 替换js文件为本地文件
        guokrStory = guokrStory.replace("<script src=\"http://static.guokr.com/apps/handpick/scripts/9c661fc7.base.js\"></script>",
                "<script src=\"file:///android_asset/guokr.base.js\"></script>");
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES){
            guokrStory = guokrStory.replace("<div class=\"article\" id=\"contentMain\">",
                    "<div class=\"article \" id=\"contentMain\" style=\"background-color:#212b30; color:#878787\">");
        }
    }
}
