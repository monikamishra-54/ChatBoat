package com.example.mychatboat;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetofitApi {

    @GET
    Call<MsgModel> getMessage(@Url String url);

}
