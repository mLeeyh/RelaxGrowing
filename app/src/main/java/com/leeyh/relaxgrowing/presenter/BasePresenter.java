package com.leeyh.relaxgrowing.presenter;

import android.content.Context;

import com.leeyh.relaxgrowing.ui.iView.iBaseView;

import rx.Subscription;

/**
 * Created by leeyh on 16.10.22.
 */

public abstract class BasePresenter<T extends iBaseView> {
    protected Subscription subscription;
    protected Context context;
    protected T iView;

    public BasePresenter(Context context, T iView) {
        this.context = context;
        this.iView = iView;
    }

    public void init() {
        iView.initView();
    }

    public abstract void release();
}
