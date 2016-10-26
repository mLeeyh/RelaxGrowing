package com.leeyh.relaxgrowing.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.leeyh.relaxgrowing.R;
import com.leeyh.relaxgrowing.adapter.MeiziAdapter;
import com.leeyh.relaxgrowing.model.entity.Meizi;
import com.leeyh.relaxgrowing.presenter.MainPresenter;
import com.leeyh.relaxgrowing.ui.iView.iMainView;
import com.leeyh.relaxgrowing.ui.widget.LMRecyclerView;
import com.leeyh.relaxgrowing.util.SPDataUtil;
import com.leeyh.relaxgrowing.util.TipUtil;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends ToolBarActivity<MainPresenter> implements iMainView,
        SwipeRefreshLayout.OnRefreshListener, LMRecyclerView.LoadMoreListener {

    private List<Meizi> meizis;
    private MeiziAdapter adapter;
    private MainPresenter presenter;
    private int page = 1;
    private boolean isRefresh = true;
    private boolean canLoading = true;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view)
    LMRecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @OnClick(R.id.fab)
    void fabClick() {
        presenter.toGanHuoActivity();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        presenter = new MainPresenter(this, this);
        presenter.init();
    }

    @Override
    public void initView() {
        meizis = SPDataUtil.getFirstPageGirls(this);
        if (meizis == null) meizis = new ArrayList<>();
        adapter = new MeiziAdapter(meizis, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.applyFloatingActionButton(fab);
        recyclerView.setLoadMoreListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                presenter.fetchMeiziData(page);
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void showProgress() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showErrorView() {
        canLoading = true;
        TipUtil.showTipWithAction(fab, getString(R.string.load_failed), getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.fetchMeiziData(page);
            }
        });
    }

    @Override
    public void showNoMoreData() {
        canLoading = false;
        TipUtil.showSnackTip(fab, getString(R.string.no_more_data));

    }

    @Override
    public void showMeiziList(List<Meizi> meiziList) {
        canLoading = true;
        page++;
        if (isRefresh) {
            SPDataUtil.saveFirstPageGirls(this, meiziList);
            meiziList.clear();
            meiziList.addAll(meiziList);
            adapter.notifyDataSetChanged();
            isRefresh = false;
        } else {
            meizis.addAll(meiziList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.release();
    }

    @Override
    protected boolean canBack() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUmeng();
    }

    private void setupUmeng() {
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_show_girls:
                presenter.toListGirlsActivity();
                break;
            case R.id.action_about:
                presenter.toAboutActivity();
                break;
            case R.id.action_github_hot:
                String url = getString(R.string.url_github_trending);
                String title = getString(R.string.action_github_trending);
                presenter.openGitHubHot(title,url);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        presenter.fetchMeiziData(page);
    }

    @Override
    public void loadMore() {
        if (canLoading) {
            presenter.fetchMeiziData(page);
            canLoading = false;
        }
    }
}
