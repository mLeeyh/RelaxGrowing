package com.leeyh.relaxgrowing.http;

import com.leeyh.relaxgrowing.GankConfig;
import com.leeyh.relaxgrowing.model.FunnyData;
import com.leeyh.relaxgrowing.model.GanHuoData;
import com.leeyh.relaxgrowing.model.GankData;
import com.leeyh.relaxgrowing.model.MeiziData;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by leeyh on 16.10.22.
 */

public interface RgRetrofit {

    // http://gank.io/api/data/数据类型/请求个数/第几页
    @GET(value = "data/福利/" + GankConfig.MEIZI_SIZE + "/{page}")
    Observable<MeiziData> getMeiziData(@Path("page") int page);
    @GET("data/休息视频/" + GankConfig.MEIZI_SIZE + "/{page}")
    Observable<FunnyData> getFunnyData(@Path("page") int page);

    //请求某天干货数据
    @GET("day/{year}/{month}/{day}")
    Observable<GankData> getDailyData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    //请求不同类型干货（通用）
    @GET("data/{type}/"+GankConfig.GANK_SIZE+"/{page}")
    Observable<GanHuoData> getGanHuoData(@Path("type") String type, @Path("page") int page);
}
