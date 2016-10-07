package com.cba.ebanktest.services;

import com.cba.ebanktest.models.Balance;
import com.cba.ebanktest.models.Transfer;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by tung on 8/14/2016.
 */
public class AccountService {

    private static final String FORUM_SERVER_URL = "http://private-b9c3f-ambbanking.apiary-mock.com";
    private BankAccountApi mForumApi;

    public AccountService() {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(FORUM_SERVER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mForumApi = restAdapter.create(BankAccountApi.class);
    }

    public BankAccountApi getApi() {

        return mForumApi;
    }

    public interface BankAccountApi {
        @GET("/accountBalance")
        Observable<List<Balance>> getAccountBalance();

        @POST("/initTransfer")
        Observable<ResponseBody> transferMoney(@Body Transfer transfer);
    }


}
