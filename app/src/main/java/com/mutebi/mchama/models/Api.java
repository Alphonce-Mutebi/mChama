package com.mutebi.mchama.models;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {


@Headers("Content-Type:application/json;")
@POST("user/new?")

    Call<ResponseBody> createUser(
        @Query("name") String name,
        @Query("email") String email,
        @Query("phone") String phone,
        @Query("password") String password,
        @Query("password_confirmation") String password_confirmation
    );



}
