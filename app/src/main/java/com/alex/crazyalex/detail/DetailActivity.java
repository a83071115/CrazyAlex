package com.alex.crazyalex.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alex.crazyalex.R;
import com.alex.crazyalex.bean.BeanType;

public class DetailActivity extends AppCompatActivity {
    private DetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //判断状态是否为空
        if(savedInstanceState!=null){
            //获取fragment的状态
            fragment = (DetailFragment) getSupportFragmentManager().getFragment(savedInstanceState,"detailFragment");
        }else{
            fragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,fragment)
                    .commit();
        }
        Intent intent = getIntent();
        //实例化presenter
        DetailPresenter presenter = new DetailPresenter(DetailActivity.this,fragment);
        //获取类型  并传递给presenter
        presenter.setTypy((BeanType)intent.getSerializableExtra("type"));
        //获取id  并传递给presenter
        presenter.setId(intent.getIntExtra("id",0));
        //获取title 并传递给presenter
        presenter.setTitle(intent.getStringExtra("title"));
        //获取封面图片链接, 并传递给presenter
        presenter.setCoverUrl(intent.getStringExtra("coverUrl"));





    }

    /**
     * 保存当前状态
     * @param outState
     *
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(fragment.isAdded()){
            getSupportFragmentManager().putFragment(outState,"detailFragment",fragment);
        }
    }
}
