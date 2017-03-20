package com.alex.crazyalex.homepage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.StringModeImpl;
import com.alex.crazyalex.db.DatabaseHelper;
import com.alex.crazyalex.detail.DetailActivity;
import com.alex.crazyalex.interfaze.OnStringListener;
import com.alex.crazyalex.service.CacheService;
import com.alex.crazyalex.util.Api;
import com.alex.crazyalex.util.DateFormatter;
import com.alex.crazyalex.util.NetworkState;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/16.
 */

public class DouBanMomentPresenter implements DouBanMomentContract.Presenter {
    private Context mContext;
    private DouBanMomentContract.View mView;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Gson gson = new Gson();
    private StringModeImpl mMode;
    private ArrayList<DoubanMomentNews.posts> list = new ArrayList<>();
    public DouBanMomentPresenter(Context context, DouBanMomentContract.View view) {
        this.mContext = context;
        this.mView = view;
        this.mView.setPresenter(this);
        mMode = new StringModeImpl(mContext);
        dbHelper = new DatabaseHelper(mContext,"History.db",null,5);
        //得到可写数据库
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void loadMore(long date) {
        loadPosts(date,false);
    }

    /**
     * 加载数据
     * @param date 传入的日期
     * @param clearing  是否清除之前的数据
     */
    @Override
    public void loadPosts(long date, final boolean clearing) {
        if(clearing){
            list.clear();
        }
        if(NetworkState.networkConnected(mContext)){
            mMode.load(Api.DOUBAN_MOMENT + new DateFormatter().DoubanDateFormat(date), new OnStringListener() {
                @Override
                public void onSuccess(String result) {
                    try{
                        DoubanMomentNews post = gson.fromJson(result,DoubanMomentNews.class);
                        ContentValues values = new ContentValues();
                        if(clearing){
                            list.clear();
                        }
                        for (DoubanMomentNews.posts item: post.getPosts()
                             ) {
                            list.add(item);
                            if(!queryIfIDExists(item.getId())){
                                db.beginTransaction();
                                try{
                                    values.put("douban_id",item.getId());
                                    values.put("douban_news",gson.toJson(item));
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = format.parse(item.getPublished_time());
                                    values.put("douban_time",date.getTime()/1000);
                                    values.put("douban_content","");
                                    db.insert("Douban",null,values);
                                    db.setTransactionSuccessful();
                                } catch (Exception e){
                                    e.printStackTrace();
                                }finally {
                                    db.endTransaction();
                                }
                            }
                            Intent intent = new Intent("com.alex.crazyalex.LOCAL_BROADCAST");
                            intent.putExtra("type",CacheService.TYPE_DOUBAN);
                            intent.putExtra("id",item.getId());
                            //发送广播
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                        mView.showResults(list);
                    }catch (JsonSyntaxException e){
                        mView.showError();
                    }
                    mView.stopLoading();

                }

                @Override
                public void onError(VolleyError error) {
                    mView.showError();
                    mView.stopLoading();
                }
            });
        }else{
            if(clearing){
                list.clear();
                Cursor cursor = db.query("DouBan",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        DoubanMomentNews.posts post = gson.fromJson(cursor.getString(cursor.getColumnIndex("douban_news")),DoubanMomentNews.posts.class);
                        list.add(post);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                mView.stopLoading();
                mView.showResults(list);

            }
        }
    }

    @Override
    public void refresh() {
        loadPosts(Calendar.getInstance().getTimeInMillis(),true);
    }

    @Override
    public void startReading(int position) {
        DoubanMomentNews.posts item = list.get(position);
        Intent intent = new Intent(mContext, DetailActivity.class);

        intent.putExtra("type", BeanType.TYPE_DOUBAN);
        intent.putExtra("id",item.getId());
        intent.putExtra("title",item.getTitle());
        if(item.getThumbs().size()==0){
            intent.putExtra("coverUrl","");
        }else{
            intent.putExtra("coverUrl",item.getThumbs().get(0).getMedium().getUrl());
        }
        mContext.startActivity(intent);
    }

    @Override
    public void feelLucky() {
        if(list.isEmpty()){
            mView.showError();
            return;
        }
        startReading(new Random().nextInt(list.size()));
    }

    @Override
    public void start() {
        refresh();
    }
    private boolean queryIfIDExists(int id){
        Cursor cursor = db.query("Douban",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                if(id==cursor.getInt(cursor.getColumnIndex("douban_id"))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }
}
