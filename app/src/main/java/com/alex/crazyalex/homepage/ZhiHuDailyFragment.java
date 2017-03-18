package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.R;
import com.alex.crazyalex.adapter.ZhiHuDailyNewsAdapter;
import com.alex.crazyalex.bean.ZhihuDailyNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import static com.alex.crazyalex.R.id.recyclerView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ZhiHuDailyFragment extends Fragment implements ZhiHuDailyContract.View{
    private ZhiHuDailyNewsAdapter adapter;
    private ZhiHuDailyContract.Presenter presenter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private TabLayout mTabLayout;
    private SwipeRefreshLayout refresh;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    public ZhiHuDailyFragment() {
    }
    public static ZhiHuDailyFragment newInstance(){
        return new ZhiHuDailyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,null);
        Log.e("alex","zhihuonCreateView");
        initViews(view);
        presenter.start();
        //SwipeRefreshLayout 刷新数据的监听
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        //RecyclerView 监听滚动事件
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //是否滑动到最后
            boolean isSlidingToLast = false;
            /**
             * 当RecyclerView滚动发生改变状态
             * @param recyclerView
             * @param newState
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //当不滚动时
                if(newState==RecyclerView.SCROLL_STATE_IDLE) {   //RecyclerView.SCROLL_STATE_IDLE 滚动状态状态静止
                    //获取最后一个'完全'显示的item position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition(); //获取最后一个完全显示的item
                    int totalItemCount = manager.getItemCount();  //获取当前的行数

                    //判断是否滚动到底部并且是向下滑动
                    if(lastVisibleItem ==(totalItemCount-1)&&isSlidingToLast){
                        Calendar c = Calendar.getInstance();
                        c.set(mYear,mMonth,--mDay);
                        presenter.loadMore(c.getTimeInMillis());

                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //判断是否滑动在最后
                isSlidingToLast = dy>0;
                //隐藏或者显示fab
                if(dy>0){
                    fab.hide();
                }else{
                    fab.show();
                }
            }
        });
        /**
         * 直接将豆瓣精选的点击事件放在知乎的部分
         * 因为fab是属于activity的view
         * 按通常的做法,在每个fragment中去设置监听时间会导致先前设置的listener失效
         * 尝试将监听放置到main pager adapter中,这样做会引起fragment中recycler view和fab的监听冲突
         * fab并不能获取到点击事件
         * 根据tab layout 的位置选择显示不同的dialog
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTabLayout.getSelectedTabPosition()==0){
                    Calendar now = Calendar.getInstance();
                    now.set(mYear,mMonth,mDay);
                    DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        }
                    },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));

                }
            }
        });

        return view;
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showResults(ArrayList<ZhihuDailyNews.Question> list) {
        Log.e("alex","showResults");
        if(adapter == null){
            adapter = new ZhiHuDailyNewsAdapter(getContext(),list);
            adapter.setOnItemClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    presenter.startReading(position);
                }
            });
            mRecyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showPickDialog() {

    }

    @Override
    public void setPresenter(ZhiHuDailyContract.Presenter presenter) {
        if(presenter!=null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}

