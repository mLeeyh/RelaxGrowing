package com.leeyh.relaxgrowing.presenter;

import android.content.Context;
import android.content.Intent;
import android.database.Observable;

import com.leeyh.relaxgrowing.GankConfig;
import com.leeyh.relaxgrowing.http.RgClient;
import com.leeyh.relaxgrowing.http.RgRetrofit;
import com.leeyh.relaxgrowing.model.FunnyData;
import com.leeyh.relaxgrowing.model.MeiziData;
import com.leeyh.relaxgrowing.model.entity.Gank;
import com.leeyh.relaxgrowing.ui.activity.AboutActivity;
import com.leeyh.relaxgrowing.ui.activity.GanHuoActivity;
import com.leeyh.relaxgrowing.ui.activity.ListGirlsActivity;
import com.leeyh.relaxgrowing.ui.activity.WebActivity;
import com.leeyh.relaxgrowing.ui.iView.iMainView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by leeyh on 16.10.22.
 */

public class MainPresenter extends BasePresenter<iMainView> {

    public MainPresenter(Context context, iMainView iView) {
        super(context, iView);
    }

    @Override
    public void release() {
        subscription.unsubscribe();
    }

    public void fetchMeiziData(int page) {
        iView.showProgress();
        subscription = rx.Observable.zip(RgClient.getGankRetrofitInstance().getMeiziData(page),
                RgClient.getGankRetrofitInstance().getFunnyData(page),
                new Func2<MeiziData, FunnyData, MeiziData>() {
                    @Override
                    public MeiziData call(MeiziData meiziData, FunnyData funnyData) {
                        return createMeiziDataWithVidioDesc(meiziData, funnyData);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MeiziData>() {

                    @Override
                    public void call(MeiziData meiziData) {

                        if (meiziData.results.size() == 0) {
                            iView.showNoMoreData();
                        } else {
                            iView.showMeiziList(meiziData.results);
                        }
                        iView.hideProgress();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        iView.showErrorView();
                        iView.hideProgress();
                    }
                });
    }

    private MeiziData createMeiziDataWithVidioDesc(MeiziData meiziData, FunnyData funnyData) {

        int size = Math.min(meiziData.results.size(), funnyData.results.size());
        for (int i = 0; i < size; i++) {
            meiziData.results.get(i).desc = meiziData.results.get(i).desc + funnyData.results.get(i).desc;
            meiziData.results.get(i).who = funnyData.results.get(i).who;
        }
        return meiziData;
    }

    public void toGanHuoActivity(){
        Intent intent = new Intent(context, GanHuoActivity.class);
        context.startActivity(intent);
    }

    public void toAboutActivity(){
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    public void openGitHubHot(String desc,String url){
        Gank gank=new Gank();
        gank.setDesc(desc);
        gank.setUrl(url);
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(GankConfig.GANK, gank);
        context.startActivity(intent);
    }

    public void toListGirlsActivity(){
        Intent intent = new Intent(context, ListGirlsActivity.class);
        context.startActivity(intent);
    }

}
