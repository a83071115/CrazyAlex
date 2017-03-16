package com.alex.crazyalex.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alex.crazyalex.R;
import com.alex.crazyalex.adapter.MainPagerAdapter;

import java.util.Random;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MainFragment extends Fragment {
    private Context mContext;
    private Context context;
    private MainPagerAdapter adapter;

    private TabLayout tabLayout;

    private ZhiHuDailyFragment zhihuDailyFragment;
    private GuokrFragment guokrFragment;
    private DouBanMomentFragment doubanMomentFragment;

    private ZhiHuDailyPresenter zhihuDailyPresenter;
    private GuokrPresenter guokrPresenter;
    private DouBanMomentPresenter doubanMomentPresenter;

    public MainFragment() {}

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();

        // Fragment状态恢复
        if (savedInstanceState != null) {
            FragmentManager manager = getChildFragmentManager();
            zhihuDailyFragment = (ZhiHuDailyFragment) manager.getFragment(savedInstanceState, "zhihu");
            guokrFragment = (GuokrFragment) manager.getFragment(savedInstanceState, "guokr");
            doubanMomentFragment = (DouBanMomentFragment) manager.getFragment(savedInstanceState, "douban");
        } else {
            // 创建View实例
            zhihuDailyFragment = ZhiHuDailyFragment.newInstance();
            guokrFragment = GuokrFragment.newInstance();
            doubanMomentFragment = DouBanMomentFragment.newInstance();
        }

        // 创建Presenter实例
        zhihuDailyPresenter = new ZhiHuDailyPresenter(context, zhihuDailyFragment);
        guokrPresenter = new GuokrPresenter(context, guokrFragment);
        doubanMomentPresenter = new DouBanMomentPresenter(context, doubanMomentFragment);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        // 初始化控件
        initViews(view);

        // 显示菜单
        setHasOptionsMenu(true);

        // 当tab layout位置为果壳精选时，隐藏fab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                if (tab.getPosition() == 1) {
                    fab.hide();
                } else {
                    fab.show();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        return view;
    }


    // 初始化控件
    private void initViews(View view) {

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        // 设置离线数为3
        viewPager.setOffscreenPageLimit(3);

        adapter = new MainPagerAdapter(
                getChildFragmentManager(),
                context,
                zhihuDailyFragment,
                guokrFragment,
                doubanMomentFragment);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_feel_lucky) {
            feelLucky();
        }
        return true;
    }

    // 保存状态
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getChildFragmentManager();
        manager.putFragment(outState, "zhihu", zhihuDailyFragment);
        manager.putFragment(outState, "guokr", guokrFragment);
        manager.putFragment(outState, "douban", doubanMomentFragment);
    }

    // 随便看看
    public void feelLucky() {
        Random random = new Random();
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                zhihuDailyPresenter.feelLucky();
                break;
            case 1:
                guokrPresenter.feelLucky();
                break;
            default:
                doubanMomentPresenter.feelLucky();
                break;
        }
    }

    public MainPagerAdapter getAdapter() {
        return adapter;
    }
}

