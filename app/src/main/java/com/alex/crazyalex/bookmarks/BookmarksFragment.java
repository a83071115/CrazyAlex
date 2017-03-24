package com.alex.crazyalex.bookmarks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.R;
import com.alex.crazyalex.adapter.BookmarsAdapter;
import com.alex.crazyalex.bean.BeanType;
import com.alex.crazyalex.bean.DoubanMomentNews;
import com.alex.crazyalex.bean.GuokrHandpickNews;
import com.alex.crazyalex.bean.ZhihuDailyNews;
import com.alex.crazyalex.interfaze.OnRecyclerViewOnClickListener;
import com.alex.crazyalex.search.SearchActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.alex.crazyalex.R.id.recyclerView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class BookmarksFragment extends Fragment implements BookmarksContract.View {


    @Bind(recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private BookmarksContract.Presenter presenter;

    public BookmarsAdapter adapter;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initViews(view);

        setHasOptionsMenu(true);

        presenter.loadResults(false);


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadResults(true);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookmarks,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_search){
            startActivity(new Intent(getActivity(), SearchActivity.class));
        }else if(id == R.id.action_feel_lucky){
            presenter.feelLucky();
        }
        return true;
    }

    @Override
    public void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList, ArrayList<GuokrHandpickNews.result> guokrList, ArrayList<DoubanMomentNews.posts> doubanList, ArrayList<Integer> types) {
        if(adapter==null){
            adapter = new BookmarsAdapter(getActivity(),zhihuList,guokrList,doubanList,types);
            adapter.setListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    Log.e("alex",position+"position");
                    int type = mRecyclerView.findViewHolderForAdapterPosition(position).getItemViewType();
                    if(type == BookmarsAdapter.TYPE_ZHIHU_NORMAL){
                        presenter.startReading(BeanType.TYPE_ZHIHU,position);
                    }else if(type ==BookmarsAdapter.TYPE_GUOKR_NORMAL){
                        presenter.startReading(BeanType.TYPE_GUOKR,position);
                    }else if(type ==BookmarsAdapter.TYPE_DOUBAN_NORMAL){
                        presenter.startReading(BeanType.TYPE_DOUBAN,position);
                    }

                }
            });
            mRecyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataChanged() {
        presenter.loadResults(true);
        adapter.notifyDataSetChanged();
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
    public void setPresenter(BookmarksContract.Presenter presenter) {
        if(presenter!=null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
