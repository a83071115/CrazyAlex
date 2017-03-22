package com.alex.crazyalex.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/22.
 */

public class SearchFragment extends Fragment implements SearchContract.View {

    @Bind(R.id.searchView)
    SearchView mSearchView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.progressBar)
    ContentLoadingProgressBar mProgressBar;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private SearchContract.Presenter presenter;

    private BookmarsAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bookmarks, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                presenter.loadResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.loadResults(newText);
                return true;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public SearchFragment() {
    }

    @Override
    public void showResults(ArrayList<ZhihuDailyNews.Question> zhihuList, ArrayList<GuokrHandpickNews.result> guokrList, ArrayList<DoubanMomentNews.posts> doubanList, ArrayList<Integer> types) {
        if(adapter==null){
            adapter = new BookmarsAdapter(getContext(),zhihuList,guokrList,doubanList,types);
            adapter.setListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    int type = mRecyclerView.findViewHolderForAdapterPosition(position).getItemViewType();
                    if(type == BookmarsAdapter.TYPE_ZHIHU_NORMAL){
                        presenter.startReading(BeanType.TYPE_ZHIHU,position);
                    }else if(type == BookmarsAdapter.TYPE_GUOKR_NORMAL){
                        presenter.startReading(BeanType.TYPE_GUOKR,position);
                    }else if(type == BookmarsAdapter.TYPE_DOUBAN_NORMAL){
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
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        if(presenter!=null){
            this.presenter = presenter;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViews(View view) {
        ((SearchActivity)(getActivity())).setSupportActionBar(mToolbar);
        ((SearchActivity)(getActivity())).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置图标化
        mSearchView.setIconified(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
