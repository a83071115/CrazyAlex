package com.alex.crazyalex.homepage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.StringModeImpl;
import com.alex.crazyalex.bean.ZhihuDailyNews;
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

public class ZhiHuDailyPresenter implements ZhiHuDailyContract.Presenter {
    private ZhiHuDailyContract.View mView;
    private Context mContext;
    private StringModeImpl mMode;
    private Gson mGson = new Gson();

    private DateFormatter mFormatter = new DateFormatter();
    private ArrayList<ZhihuDailyNews.Question> list = new ArrayList<>();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public ZhiHuDailyPresenter(Context context, ZhiHuDailyContract.View view) {
        this.mContext = context;
        this.mView = view;
        this.mView.setPresenter(this);
        mMode = new StringModeImpl(mContext);
        dbHelper = new DatabaseHelper(mContext,"History.db",null,5);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 加载数据
     * @param date  根据日期来更新数据
     * @param clearing  是否刷新数据
     */
    @Override
    public void loadPosts(long date, final boolean clearing) {
        String url = Api.ZHIHU_HISTORY + mFormatter.ZhiHuDailyDeteFormat(date);
        if(clearing){
            mView.showLoading();
        }
        /**
         * 加载网络,并且添加到网络请求队列中
         */
        if(NetworkState.networkConnected(mContext)){
            mMode.load(url, new OnStringListener() {
                @Override
                public void onSuccess(String result) {
                    Log.e("alex","成功");
                    try {
                        ZhihuDailyNews post = mGson.fromJson(result,ZhihuDailyNews.class);
                        /**
                         * ContentValues 和HashTable类似都是一种存储的机制 但是两者最大的区别就在于，contenvalues只能存储基本类型的数据，像string，int之类的，不能存储对象这种东西，而HashTable却可以存储对象。

                         在忘数据库中插入数据的时候，首先应该有一个ContentValues的对象所以：

                         ContentValues initialValues = new ContentValues();

                         initialValues.put(key,values);

                         SQLiteDataBase sdb ;

                         sdb.insert(database_name,null,initialValues);

                         插入成功就返回记录的id否则返回-1
                         */
                        ContentValues values = new ContentValues();
                        if(clearing){
                            list.clear();
                        }
                        for (ZhihuDailyNews.Question item: post.getStories()){
                            list.add(item);
                            //查询ID是否存在
                            if(!queryIfIDExists(item.getId())){
                                //开始事务
                                db.beginTransaction();
                                try {
                                    DateFormat format = new SimpleDateFormat("yyyyMMdd");
                                    Date date = format.parse(post.getDate());
                                    values.put("zhihu_id",item.getId());
                                    values.put("zhihu_news",mGson.toJson(item));
                                    values.put("zhihu_content","");
                                    values.put("zhihu_time",date.getTime()/1000);
                                    //添加到数据库
                                    db.insert("Zhihu",null,values);
                                    //清除values值
                                    values.clear();
                                    //提交事务
                                    db.setTransactionSuccessful();
                                } catch (Exception e){
                                    e.printStackTrace();
                                } finally {
                                    db.endTransaction();
                                }
                                //启动服务 并且将类型和id传递过去
                                Intent intent = new Intent("com.alex.crazyalex.LOCAL_BROADCAST");
                                intent.putExtra("type", CacheService.TYPE_ZHIHU);
                                intent.putExtra("id",item.getId());
                                //发送广播
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                            }

                        }
                        mView.showResults(list);
                    } catch (JsonSyntaxException e){
                        mView.showError();
                    }
                    mView.showLoading();

                }


                @Override
                public void onError(VolleyError error) {
                    mView.stopLoading();
                    mView.showError();
                    Log.e("alex","失败");
                }
            });

        }else{  //如果没有联网
            if(clearing){
                list.clear();
                //查询数据库
                Cursor cursor = db.query("Zhihu",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        //读取数据库中的zhihu_news然后解析json
                        ZhihuDailyNews.Question question = mGson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")),ZhihuDailyNews.Question.class);
                        list.add(question);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                //停止加载动作
                mView.stopLoading();
                //显示结果
                mView.showResults(list);
            }else{
                mView.showError();
            }

        }

    }

    /**
     * 判断表里面是否存在该ID
     * @param id 传入的新闻id
     * @return 返回是否存在   true 存在  false 不存在
     */
    private boolean queryIfIDExists(int id) {
        Cursor cursor = db.query("Zhihu",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                if(id==cursor.getInt(cursor.getColumnIndex("zhihu_id"))){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return false;

    }

    @Override
    public void refresh() {
        loadPosts(Calendar.getInstance().getTimeInMillis(),true);
    }

    /**
     * 下拉加载更多
     * @param date  插入的日期  后面的false 是不需要清除原来的数据, 加载在list后面  相当于 list.add(list)
     */
    @Override
    public void loadMore(long date) {
        loadPosts(date,false);
    }

    /**
     * 开始阅读详细信息
     * @param position  item所在的位置
     */
    @Override
    public void startReading(int position) {
        //跳转到DetailActivity页面 , 并且将值传递过去
        mContext.startActivity(new Intent(mContext,DetailActivity.class)
            .putExtra("id",list.get(position).getId())
            .putExtra("type", BeanType.TYPE_ZHIHU)
            .putExtra("title",list.get(position).getTitle())
            .putExtra("coverUrl",list.get(position).getImages().get(0)));
    }

    /**
     * 随便看看 , 随机点击一个item 进行观看
     */
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
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }
}
