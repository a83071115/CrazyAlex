package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.R;
import com.alex.crazyalex.adapter.GuokrNewsAdapter;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/16.
 */

public class GuokrFragment extends Fragment implements GuokrContract.View {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private GuokrContract.Presenter mPresenter;
    private GuokrNewsAdapter mAdapter;
    public GuokrFragment() {
    }

    public static GuokrFragment newInstance() {
        return new GuokrFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        mPresenter.start();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }
        });

        return view;
    }

    @Override
    public void showError() {
        Snackbar.make(mRefreshLayout, R.string.loaded_failed, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.refresh();
                    }
                });
    }

    @Override
    public void showLoading() {
        //显示下拉刷新图标
        mRefreshLayout.setRefreshing(true);

    }

    @Override
    public void stopLoading() {
        //关闭下拉刷新图标
        mRefreshLayout.setRefreshing(false);

    }

    @Override
    public void showResults(ArrayList<GuokrHandpickNews.result> list) {
        if(mAdapter==null){
            // TODO: 2017/3/19 GuokrAdapter
            mAdapter = new GuokrNewsAdapter(getContext(),list);
            mAdapter.setListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    mPresenter.startReading(position);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setPresenter(GuokrContract.Presenter presenter) {
        if (presenter != null) {
            this.mPresenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        //设置固定大小
        mRecyclerView.setHasFixedSize(true);
        //设置 垂直向下排列
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置下拉刷新 显示框颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
