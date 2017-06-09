package com.wendu.rongclouddemo.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/6/2.
 */

public class HttpUtils {

    private static RuquestSrevice ruquestSrevice;

    public static RuquestSrevice init(){
        if(ruquestSrevice==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.203.105:8080")
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            ruquestSrevice = retrofit.create(RuquestSrevice.class);
        }
        return  ruquestSrevice;
    }

}
