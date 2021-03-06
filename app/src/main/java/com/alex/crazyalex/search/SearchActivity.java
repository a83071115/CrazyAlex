package com.alex.crazyalex.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alex.crazyalex.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SearchFragment fragment = SearchFragment.newInstance();


        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();

        new SearchPresenter(this,fragment);
    }


}
