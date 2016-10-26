package com.leeyh.relaxgrowing.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 添加加载更多功能
 * Created by leeyh on 16.10.24.
 */

public class LMRecyclerView extends RecyclerView {
    private boolean isScrollingToBottom = true;
    private FloatingActionButton floatingActionButton;
    private LoadMoreListener listener;

    public LMRecyclerView(Context context) {
        super(context);
    }

    public LMRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LMRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void applyFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        isScrollingToBottom = dy > 0;
        Log.i("onScrolled", "onScrolled: dy " + dy);
        if (floatingActionButton != null) {
            if (isScrollingToBottom) {
                if (floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                }
            } else {
                if (!floatingActionButton.isShown()) {
                    floatingActionButton.show();
                }
            }
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (screenState == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            if(lastVisibleItem == (totalItemCount-1 )&& isScrollingToBottom){
                if(listener != null){
                    listener.loadMore();
                }
            }
        }
    }

    public interface LoadMoreListener {
        void loadMore();
    }
}
