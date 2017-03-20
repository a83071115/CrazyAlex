package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.R;
import com.alex.crazyalex.adapter.MainPagerAdapter;
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
                    //生成日期选择对话框 并设置点击事件
                    DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            //设置个临时的日历
                            Calendar temp = Calendar.getInstance();
                            //将临时日历清除
                            temp.clear();
                            //将用户选择的年月日设置给临时日历
                            temp.set(year, monthOfYear, dayOfMonth);
                            //加载数据,用时期加载
                            presenter.loadPosts(temp.getTimeInMillis(), true);
                        }
                    },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                    //对话框设置当前的日期时间
                    dialog.setMaxDate(Calendar.getInstance());
                    //获取日期对象
                    Calendar minDate = Calendar.getInstance();
                    // 2013.5.20是知乎日报api首次上线
                    minDate.set(2013, 5, 20);
                    //设置日期的最小日期 , 只能点击到2013年5月20日
                    dialog.setMinDate(minDate);
                    //将日期选择器 不能点
                    dialog.vibrate(false);

                    dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
                }else if(mTabLayout.getSelectedTabPosition()==2){
                    ViewPager p = (ViewPager) getActivity().findViewById(R.id.view_pager);
                    MainPagerAdapter ad = (MainPagerAdapter) p.getAdapter();
                    ad.getDouBanMomentFragment().showPickDialog();

                }
            }
        });

        return view;
    }

    /**
     * 显示加载错误
     */
    @Override
    public void showError() {
        Snackbar.make(fab,R.string.loaded_failed,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.refresh();
                    }
                });
    }

    @Override
    public void showLoading() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                //显示下拉刷新 圆圈
                refresh.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopLoading() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                //关闭下拉刷新 圆圈
                refresh.setRefreshing(false);
            }
        });
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
    public void setPresenter(ZhiHuDailyContract.Presenter presenter) {
        if(presenter!=null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(recyclerView);
        //设置固定大小
        mRecyclerView.setHasFixedSize(true);
        //设置线性的 垂直排列
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        /**
         * 直接将豆瓣精选的点击事件放在知乎的部分
         * 因为fab是属于activity的view
         * 所以在findviewbyid中 如要中getActivity()来点
         * 按通常的做法,在每个fragment中去设置监听时间会导致先前设置的listener失效
         * 尝试将监听放置到main pager adapter中,这样做会引起fragment中recycler view和fab的监听冲突
         * fab并不能获取到点击事件
         * 根据tab layout 的位置选择显示不同的dialog
         */
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        //设置fab的渐变颜色
        fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));

        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        //设置下拉刷新的按钮的颜色
        refresh.setColorSchemeResources(R.color.colorPrimary);

    }

}

