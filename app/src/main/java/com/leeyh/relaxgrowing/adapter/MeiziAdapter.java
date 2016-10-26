package com.leeyh.relaxgrowing.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leeyh.relaxgrowing.GankConfig;
import com.leeyh.relaxgrowing.R;
import com.leeyh.relaxgrowing.model.entity.Meizi;
import com.leeyh.relaxgrowing.ui.activity.GankActivity;
import com.leeyh.relaxgrowing.ui.activity.MeiZhiActivity;
import com.leeyh.relaxgrowing.ui.widget.RatioImageView;
import com.leeyh.relaxgrowing.util.DateUtil;
import com.leeyh.relaxgrowing.util.ShareElement;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leeyh on 16.10.24.
 */

public class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziHolder> {

    List<Meizi> list;
    Context context;
    int lastPosition = 0;

    public MeiziAdapter(List<Meizi> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meizi, parent, false);
        return new MeiziHolder(view);
    }

    @Override
    public void onBindViewHolder(MeiziHolder holder, int position) {
        Meizi meizi = list.get(position);
        holder.card.setTag(meizi);
        int red = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        holder.ivMeizi.setBackgroundColor(Color.argb(204, red, green, blue));
        Glide.with(context)
                .load(meizi.url)
                .crossFade()
                .into(holder.ivMeizi);

        holder.tvWho.setText(meizi.who);
        holder.tvAvatar.setText(meizi.who.substring(0, 1).toUpperCase());
        holder.tvDesc.setText(meizi.desc);
        holder.tvTime.setText(DateUtil.toDateTimeStr(meizi.publishedAt));
        holder.tvAvatar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showItemAnimation(MeiziHolder holder, int position) {
        if (position > lastPosition) {
            lastPosition = position;
            ObjectAnimator.ofFloat(holder.card, "translationY", 1f * holder.card.getHeight(), 0f)
                    .setDuration(500)
                    .start();
        }
    }


    public class MeiziHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_meizi)
        RatioImageView ivMeizi;
        @Bind(R.id.tv_who)
        TextView tvWho;
        @Bind(R.id.tv_avatar)
        TextView tvAvatar;
        @Bind(R.id.tv_desc)
        TextView tvDesc;
        @Bind(R.id.tv_time)
        TextView tvTime;

        @OnClick(R.id.iv_meizi)
        void meiziClick() {
            ShareElement.shareDrawable = ivMeizi.getDrawable();
            Intent intent = new Intent(context, MeiZhiActivity.class);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) context, ivMeizi, GankConfig.TRANSLATE_GIRL_VIEW);
            ActivityCompat.startActivity((Activity) context, intent, optionsCompat.toBundle());
        }

        @OnClick(R.id.rl_gank)
        void gankClick() {
            ShareElement.shareDrawable = ivMeizi.getDrawable();
            Intent intent = new Intent(context, GankActivity.class);
            intent.putExtra(GankConfig.MEIZI, (Serializable) card.getTag());
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) context, ivMeizi, GankConfig.TRANSLATE_GIRL_VIEW);
            ActivityCompat.startActivity((Activity) context, intent, optionsCompat.toBundle());
        }

        View card;

        public MeiziHolder(View itemView) {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
            ivMeizi.setOriginalSize(300, 150);
        }
    }
}
