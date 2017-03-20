package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.R;
import com.alex.crazyalex.adapter.DoubanMomentAdapter;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/16.
 */

public class DouBanMomentFragment extends Fragment implements DouBanMomentContract.View {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private FloatingActionButton fab ;
    private DouBanMomentContract.Presenter presenter;
    private DoubanMomentAdapter mAdapter;

    /**
     * 实例化年月日
     */
    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


    public DouBanMomentFragment() {
    }

    public static DouBanMomentFragment newInstance() {
        return new DouBanMomentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        presenter.start();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //当不滚动时
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    //获取最后一个完全显示的itemposition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    //判断是否滚到到底部而且是向下滑动
                    if(lastVisibleItem == (totalItemCount-1)&& isSlidingToLast){
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
                isSlidingToLast = dy>0;
                //隐藏或者显示fab
                if(dy > 0){
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

        return view;
    }

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
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showResults(ArrayList<DoubanMomentNews.posts> list) {
        if(mAdapter==null){
            mAdapter = new DoubanMomentAdapter(list,getContext());
            mAdapter.setListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    presenter.startReading(position);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showPickDialog() {
        Calendar now = Calendar.getInstance();
        now.set(mYear,mMonth,mDay);
        DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar temp = Calendar.getInstance();
                temp.clear();
                temp.set(year,monthOfYear,dayOfMonth);
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                presenter.loadPosts(temp.getTimeInMillis(),true);

            }
        },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
        //设置最大日期
        dialog.setMaxDate(Calendar.getInstance());
        Calendar minDate = Calendar.getInstance();
        minDate.set(2014,5,12);
        //设置最小日期
        dialog.setMinDate(minDate);
        //不设置震动
        dialog.vibrate(false);
        dialog.show(getActivity().getFragmentManager(),"DatePickerDialog");

    }


    @Override
    public void setPresenter(DouBanMomentContract.Presenter presenter) {
        if(presenter!=null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        //因为fab是activity的控件 ,所以要用activity来实例化他
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        //设置fab的渐变颜色
        fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));

        //设置固定大小
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置下拉刷新的按钮颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
