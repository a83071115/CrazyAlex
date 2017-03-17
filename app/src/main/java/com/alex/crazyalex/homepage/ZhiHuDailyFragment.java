package com.alex.crazyalex.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import static com.alex.crazyalex.R.id.recyclerView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ZhiHuDailyFragment extends Fragment implements ZhiHuDailyContract.View{
    private ZhiHuDailyNewsAdapter adapter;
    private ZhiHuDailyContract.Presenter presenter;
    private RecyclerView mRecyclerView;
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

