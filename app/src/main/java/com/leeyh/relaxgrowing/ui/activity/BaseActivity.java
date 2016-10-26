package com.leeyh.relaxgrowing.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.leeyh.relaxgrowing.presenter.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by leeyh on 16.10.22.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity{
    protected String TAG = this.getClass().getSimpleName();
    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initPresenter();
        Log.i(TAG, "onCreate");

    }

    protected abstract void initPresenter();

    protected abstract int getLayoutResId();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
