package com.mutebi.mchama.Retrofit;

import com.mutebi.mchama.models.Data;
import com.mutebi.mchama.models.Data_;
import com.mutebi.mchama.models.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("user/oauth")
    @FormUrlEncoded
    Call<Data> authenticateUser(@Field("email") String email,
                                @Field("password") String password );


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
