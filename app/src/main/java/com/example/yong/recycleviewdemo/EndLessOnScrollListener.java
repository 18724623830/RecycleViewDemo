package com.example.yong.recycleviewdemo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by yong on 2018/7/9.
 */

public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLinearLayoutManager;
    //当前的全部条目数量
    private int totalItemCount;
    //记录上一个totalItemCount
    private int prviousTotal;
    //当前屏幕可见的条目数
    private int visibleItemCount;
    //当前可见的第一个条目
    private int fristVisibleItem;
    private int currentPage=1;
    private boolean loading=true;
    public EndLessOnScrollListener(LinearLayoutManager manager){
        mLinearLayoutManager=manager;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
//        Log.d("song>>>>>","执行onScrolled");
//
//        totalItemCount=mLinearLayoutManager.getItemCount();
//        visibleItemCount=recyclerView.getChildCount();
//        fristVisibleItem=mLinearLayoutManager.findFirstVisibleItemPosition();
//        if(loading){
//            if(totalItemCount>prviousTotal){
//                loading=false;
//                prviousTotal=totalItemCount;
//            }
//        }
//        if(!loading&&totalItemCount-visibleItemCount<fristVisibleItem){
//            currentPage++;
//            onLoadMore(currentPage);
//            loading=true;
//        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState==RecyclerView.SCROLL_STATE_IDLE){
            Log.d("song>>>>>","执行SCROLL_STATE_IDLE");
            totalItemCount=mLinearLayoutManager.getItemCount();
            visibleItemCount=recyclerView.getChildCount();
            fristVisibleItem=mLinearLayoutManager.findFirstVisibleItemPosition();
            if(loading){
                if(totalItemCount>prviousTotal){
                    loading=false;
                    prviousTotal=totalItemCount;
                }
            }
            if(!loading&&totalItemCount-visibleItemCount<=fristVisibleItem){
                currentPage++;
                onLoadMore(currentPage);
                loading=true;
            }
        }

    }

    public abstract void onLoadMore(int currentPage);
}
