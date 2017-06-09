package com.wendu.rongclouddemo.http;

import com.wendu.rongclouddemo.bean.LoginResult;
import com.wendu.rongclouddemo.bean.Registed;
import com.wendu.rongclouddemo.bean.Registing;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xs on 2017/6/2.
 */

public interface RuquestSrevice {
    //name=xxx&price=100&number=20
//    @GET("book")
//    Call<Result> getResult(@Query("name")String name, @Query("price")String price, @Query("number")String number);

    @GET("app/isRegist")
    Call<Registed> isRegist(@Query("userName") String name);

    @GET("app/regist")
    Observable<Registing> regist(@Query("userName") String name, @Query("pwd") String pwd);

    @GET("app/login")
    Call<LoginResult> login(@Query("userName") String name, @Query("pwd") String pwd);

}
