package com.alex.crazyalex.homepage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.StringModeImpl;
import com.alex.crazyalex.db.DatabaseHelper;
import com.alex.crazyalex.detail.DetailActivity;
import com.alex.crazyalex.interfaze.OnStringListener;
import com.alex.crazyalex.service.CacheService;
import com.alex.crazyalex.util.Api;
import com.alex.crazyalex.util.NetworkState;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/16.
 */

public class GuokrPresenter implements GuokrContract.Presenter {
    private Context mContext;
    private GuokrContract.View mView;
    //联网请求
    private StringModeImpl mStringMode;
    //GSON
    private Gson gson;
    //数据
    private ArrayList<GuokrHandpickNews.result> list = new ArrayList<>();

    //数据库保存
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public GuokrPresenter(Context context, GuokrContract.View view) {
        this.mContext = context;
        this.mView = view;
        //实例化volley
        mStringMode = new StringModeImpl(context);
        //实例化数据库
        dbHelper = new DatabaseHelper(context,"History.db",null,5);
        //得到可写数据库
        db = dbHelper.getWritableDatabase();
        //实例化Gson
        gson = new Gson();
        view.setPresenter(this);
    }



    @Override
    public void refresh() {
        loadPosts();
    }

    /**
     * 开启阅读  并传入数据
     * @param position 点击的位置
     */
    @Override
    public void startReading(int position) {
        GuokrHandpickNews.result item = list.get(position);
        mContext.startActivity(new Intent(mContext, DetailActivity.class)
                .putExtra("type", BeanType.TYPE_GUOKR)
                .putExtra("id",item.getId())
                .putExtra("title",item.getTitle())
                .putExtra("coverUrl",item.getHeadline_img())
                    );
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
    public void loadPosts() {
        mView.showLoading();
        if(NetworkState.networkConnected(mContext)){
            mStringMode.load(Api.GUOKR_ARTICLES, new OnStringListener() {
                @Override
                public void onSuccess(String result) {
                    //由于果壳并没有按照日期加载的api
                    //所以不存在加载指定日期内容的操作,当要请求数据时一定是在进行刷新
                    list.clear();
                    try{
                        GuokrHandpickNews question = gson.fromJson(result,GuokrHandpickNews.class);
                        for (GuokrHandpickNews.result re: question.getResult()
                             ) {
                            list.add(re);
                            if(!queryIfIDExists(re.getId())){
                                try{
                                    //开始事务控制
                                    db.beginTransaction();
                                    //创建集合
                                    ContentValues values = new ContentValues();
                                    values.put("guokr_id",re.getId());
                                    values.put("guokr_news",gson.toJson(re));
                                    values.put("guokr_content","");
                                    values.put("guokr_time",(long)re.getDate_picked());
                                    //添加到数据库
                                    db.insert("Guokr",null,values);
                                    //清除values
                                    values.clear();
                                    //设置事务成功
                                    db.setTransactionSuccessful();
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }finally {
                                    //开闭事务控制
                                    db.endTransaction();
                                }

                            }
                            Intent intent = new Intent("com.alex.crazyalex.LOCAL_BROADCAST");
                            intent.putExtra("type", CacheService.TYPE_GUOKR);
                            intent.putExtra("id",re.getId());
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                        mView.showResults(list);
                    } catch (JsonSyntaxException e){
                        mView.showError();
                    }
                    mView.stopLoading();
                }

                @Override
                public void onError(VolleyError error) {
                    mView.stopLoading();
                    mView.showError();
                }
            });
        }else{
            //如果没有联网 , 就在数据库里面请求之前存储的gson
            Cursor cursor = db.query("Guokr",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do {
                    GuokrHandpickNews.result result = gson.fromJson(cursor.getString(cursor.getColumnIndex("guokr_news")),GuokrHandpickNews.result.class);
                    list.add(result);
                }while (cursor.moveToNext());
            }
            cursor.close();
            mView.stopLoading();
            mView.showResults(list);
            //当第一次安卓应用,并且没有打开网络时
            //此时既无法网络加载,也无法加载本地
            if(list.isEmpty()){
                mView.showError();
            }
        }
    }

    @Override
    public void start() {
        loadPosts();
    }

    /**
     * 查询是否存在此id
     */
    private boolean queryIfIDExists(int id){
        Cursor cursor = db.query("Guokr",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                if(id==cursor.getInt(cursor.getColumnIndex("guokr_id"))){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }
}
