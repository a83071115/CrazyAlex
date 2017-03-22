package com.alex.crazyalex.search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.ZhihuDailyNews;
import com.alex.crazyalex.db.DatabaseHelper;
import com.alex.crazyalex.detail.DetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_DOUBAN_NORMAL;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_DOUBAN_WITH_HEADER;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_GUOKR_NORMAL;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_GUOKR_WITH_HEADER;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_ZHIHU_NORMAL;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_ZHIHU_WITH_HEADER;

/**
 * Created by Administrator on 2017/3/22.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View view;
    private Context context;
    private Gson gson;
    private ArrayList<ZhihuDailyNews.Question> zhihuList;
    private ArrayList<GuokrHandpickNews.result> guokrList;
    private ArrayList<DoubanMomentNews.posts> doubanList;
    private ArrayList<Integer> types;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;


    public SearchPresenter(Context context, SearchContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        gson = new Gson();
        zhihuList = new ArrayList<>();
        guokrList = new ArrayList<>();
        doubanList = new ArrayList<>();
        types = new ArrayList<>();
        dbHelper = new DatabaseHelper(context,"History.db",null,5);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void loadResults(String queryWords) {
        zhihuList.clear();
        guokrList.clear();
        doubanList.clear();
        types.clear();
        types.add(TYPE_ZHIHU_WITH_HEADER);
        Cursor cursor = db.rawQuery("select * from Zhihu where bookmark = 1 and zhihu_news like '%" + queryWords + "%'",null);
        if(cursor.moveToFirst()){
            do {
                ZhihuDailyNews.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")),ZhihuDailyNews.Question.class);
                zhihuList.add(question);
                types.add(TYPE_ZHIHU_NORMAL);
            }while (cursor.moveToNext());
        }

        types.add(TYPE_GUOKR_WITH_HEADER);
        cursor = db.rawQuery("select * from Guokr where bookmark = 1 and guokr_news like '%" + queryWords + "%'",null);
        if(cursor.moveToFirst()){
            do {
                GuokrHandpickNews.result result = gson.fromJson(cursor.getString(cursor.getColumnIndex("guokr_news")),GuokrHandpickNews.result.class);
                guokrList.add(result);
                types.add(TYPE_GUOKR_NORMAL);
            } while (cursor.moveToNext());
        }
        types.add(TYPE_DOUBAN_WITH_HEADER);
        cursor = db.rawQuery("select * from Douban where bookmark = 1 and douban_news like '%" + queryWords + "%'",null);
        if(cursor.moveToFirst()){
            do {
                DoubanMomentNews.posts posts = gson.fromJson(cursor.getString(cursor.getColumnIndex("douban_news")),DoubanMomentNews.posts.class);
                doubanList.add(posts);
                types.add(TYPE_DOUBAN_NORMAL);
            }while (cursor.moveToNext());
        }
        cursor.close();
        view.showResults(zhihuList,guokrList,doubanList,types);
    }


    @Override
    public void startReading(BeanType type, int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        switch (type){
            case TYPE_ZHIHU:
                ZhihuDailyNews.Question question = zhihuList.get(position - 1);
                intent.putExtra("id",question.getId());
                intent.putExtra("title",question.getTitle());
                intent.putExtra("type",BeanType.TYPE_ZHIHU);
                intent.putExtra("coverUrl",question.getImages().get(0));
                break;
            case TYPE_GUOKR:
                GuokrHandpickNews.result result = guokrList.get(position - zhihuList.size() - 2);
                intent.putExtra("type",BeanType.TYPE_GUOKR);
                intent.putExtra("id",result.getId());
                intent.putExtra("title",result.getTitle());
                intent.putExtra("coverUrl",result.getHeadline_img());
                break;
            case TYPE_DOUBAN:
                DoubanMomentNews.posts posts= doubanList.get(position - zhihuList.size() - guokrList.size() - 3);
                intent.putExtra("type",BeanType.TYPE_DOUBAN);
                intent.putExtra("id",posts.getId());
                intent.putExtra("title",posts.getTitle());
                if(posts.getThumbs().size()== 0){
                    intent.putExtra("coverUrl","");
                }else{
                    intent.putExtra("coverUrl",posts.getThumbs().get(0).getMedium().getUrl());
                }
                break;
            default:
                break;
        }
        context.startActivity(intent);
    }

    @Override
    public void start() {

    }
}
