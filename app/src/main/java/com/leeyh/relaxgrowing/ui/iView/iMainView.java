package com.leeyh.relaxgrowing.ui.iView;

import com.leeyh.relaxgrowing.model.entity.Meizi;

import java.util.List;

/**
 * Created by leeyh on 16.10.22.
 */

public interface iMainView extends iBaseView {
    void showProgress();
    void hideProgress();
    void showErrorView();
    void showNoMoreData();
    void showMeiziList(List<Meizi> meiziList);
}
