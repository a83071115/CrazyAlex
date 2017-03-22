package com.alex.crazyalex.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alex.crazyalex.adapter.BookmarsAdapter;
import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.ZhihuDailyNews;
import com.alex.crazyalex.db.DatabaseHelper;
import com.alex.crazyalex.detail.DetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_DOUBAN_NORMAL;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_DOUBAN_WITH_HEADER;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_GUOKR_NORMAL;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_GUOKR_WITH_HEADER;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_ZHIHU_NORMAL;
import static com.alex.crazyalex.adapter.BookmarsAdapter.TYPE_ZHIHU_WITH_HEADER;

/**
 * Created by Administrator on 2017/3/16.
 */

public class BookmarksPresenter implements BookmarksContract.Presenter {
    private Context mContext;
    private BookmarksContract.View mView;
    private Gson gson;
    private ArrayList<ZhihuDailyNews.Question> zhihuList;

    private ArrayList<GuokrHandpickNews.result> guokrList;

    private ArrayList<DoubanMomentNews.posts> doubanList;

    //给RecyclerView显示的数据
    private ArrayList<Integer> type ;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;
    public BookmarksPresenter(Context context, BookmarksContract.View view) {
        this.mContext = context;
        this.mView = view;
        gson = new Gson();
        dbHelper = new DatabaseHelper(mContext,"History.db",null,5);
        db = dbHelper.getWritableDatabase();
        this.mView.setPresenter(this);
        zhihuList = new ArrayList<>();
        guokrList = new ArrayList<>();
        doubanList = new ArrayList<>();
        type = new ArrayList<>();
    }

    @Override
    public void loadResults(boolean refresh) {
        if(!refresh){
            mView.showLoading();
        }else{
            zhihuList.clear();
            guokrList.clear();
            doubanList.clear();
            type.clear();
        }
        checkForFreshData();
        //将得到的list总长度  传递给adapter
        mView.showResults(zhihuList,guokrList,doubanList,type);
        mView.stopLoading();
    }

    @Override
    public void startReading(BeanType type, int position) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        switch (type){
            case TYPE_ZHIHU:
                ZhihuDailyNews.Question q = zhihuList.get(position - 1);
                intent.putExtra("type",BeanType.TYPE_ZHIHU);
                intent.putExtra("id",q.getId());
                intent.putExtra("title",q.getTitle());
                intent.putExtra("coverUrl",q.getImages().get(0));
                break;
            case TYPE_GUOKR:
                GuokrHandpickNews.result result = guokrList.get(position - 2 - zhihuList.size());
                intent.putExtra("type",BeanType.TYPE_GUOKR);
                intent.putExtra("id",result.getId());
                intent.putExtra("title",result.getTitle());
                intent.putExtra("coverUrl",result.getHeadline_img());
                break;
            case TYPE_DOUBAN:
                DoubanMomentNews.posts posts = doubanList.get(position - zhihuList.size() - guokrList.size() - 3);
                intent.putExtra("type",BeanType.TYPE_DOUBAN);
                intent.putExtra("id",posts.getId());
                intent.putExtra("title",posts.getTitle());
                if(posts.getThumbs().size() == 0){
                    intent.putExtra("coverUrl","");
                }else{
                    intent.putExtra("coverUrl",posts.getThumbs().get(0).getMedium().getUrl());
                }
                break;
                default:
                    break;
        }
        mContext.startActivity(intent);
    }


    /**
     * 检查数据库是否有数据
     * 然后将数据加载
     * 将类型传给type显示的Adapter上
     */
    @Override
    public void checkForFreshData() {
        type.add(TYPE_ZHIHU_WITH_HEADER);
        //select * from Zhihu where bookmark = 1 ;
        Cursor cursor = db.rawQuery("select * from Zhihu where bookmark = ? ",new String[]{"1"} );

        if(cursor.moveToFirst()){
            do {

                ZhihuDailyNews.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")),ZhihuDailyNews.Question.class);
                zhihuList.add(question);
                type.add(TYPE_ZHIHU_NORMAL);
            }while (cursor.moveToNext());
        }
        type.add(TYPE_GUOKR_WITH_HEADER);
        cursor = db.rawQuery("select * from Guokr where bookmark = ?" ,new String[]{"1"});
        if(cursor.moveToFirst()){
            do {
                GuokrHandpickNews.result result = gson.fromJson(cursor.getString(cursor.getColumnIndex("guokr_news")),GuokrHandpickNews.result.class);
                guokrList.add(result);
                type.add(TYPE_GUOKR_NORMAL);
            }while (cursor.moveToNext());
        }
        type.add(TYPE_DOUBAN_WITH_HEADER);
        cursor = db.rawQuery("select * from Douban where bookmark = ? ", new String[]{"1"});
        if(cursor.moveToFirst()){
            do {
                DoubanMomentNews.posts posts = gson.fromJson(cursor.getString(cursor.getColumnIndex("douban_news")),DoubanMomentNews.posts.class);

                doubanList.add(posts);
                type.add(TYPE_DOUBAN_NORMAL);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void feelLucky() {
        Random random = new Random();
        int p = random.nextInt(type.size());
        while (true){
            if(type.get(p)== BookmarsAdapter.TYPE_ZHIHU_NORMAL){
                startReading(BeanType.TYPE_ZHIHU,p);
                break;
            } else if(type.get(p) == BookmarsAdapter.TYPE_GUOKR_NORMAL){
                startReading(BeanType.TYPE_GUOKR,p);
                break;
            } else if (type.get(p) == BookmarsAdapter.TYPE_DOUBAN_NORMAL){
                startReading(BeanType.TYPE_DOUBAN,p);
                break;
            } else {
                p = random.nextInt(type.size());
            }
        }
    }

    @Override
    public void start() {

    }
}
