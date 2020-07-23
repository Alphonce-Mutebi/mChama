package com.mutebi.mchama.Retrofit;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://mchamatest.jeffreykingori.dev/api/v1/";

    public static ApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
