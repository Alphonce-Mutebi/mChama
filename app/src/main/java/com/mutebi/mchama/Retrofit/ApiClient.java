package com.mutebi.mchama.Retrofit;
import com.mutebi.mchama.models.Api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL ="https://mchamatest.jeffreykingori.dev/api/v1/";

    private static ApiClient apiClient;
    private Retrofit retrofit;

    private ApiClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public static synchronized  ApiClient getApiClient(){
        if(apiClient == null){
            apiClient = new ApiClient();
        }
        return apiClient;

    }

    public Api getApi(){
        return retrofit.create(Api.class);

    }









}
