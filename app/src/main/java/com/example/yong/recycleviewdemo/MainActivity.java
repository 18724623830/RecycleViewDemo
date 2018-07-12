package com.example.yong.recycleviewdemo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String URL = "http://gank.io/api/";
    private int page = 1;
    private List<ReBeean.ResultsBean> mData = new ArrayList<>();
    private int flag = 0;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = findViewById(R.id.layout_swipe_refresh);
        mRecyclerView = findViewById(R.id.recyclerview);

        myAdapter = new MyAdapter(MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(myAdapter);

        initData();
        //刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = 1;
                page = 1;
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        //下拉加载更多
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                flag=-1;
                page=currentPage;
                initData();
            }
        });
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        MyService request = retrofit.create(MyService.class);
        request.getResquest("Android", 30, page).enqueue(new Callback<ReBeean>() {
            @Override
            public void onResponse(Call<ReBeean> call, Response<ReBeean> response) {
                if (flag == 1) {
                    mData = response.body().getResults();
                    myAdapter.setData(mData);
                    Toast.makeText(MainActivity.this, "以获取最新数据", Toast.LENGTH_SHORT).show();
                }else if (flag == 0) {
                    mData = response.body().getResults();
                    myAdapter.setData(mData);
                }else if(flag==-1){
                    mData.addAll(response.body().getResults());
                    myAdapter.setData(mData);
                }
            }

            @Override
            public void onFailure(Call<ReBeean> call, Throwable t) {
                Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
