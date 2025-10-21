package com.example.teste1.view.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {


    private static final String BASE_URL = "https://abby-unbeaued-tyron.ngrok-free.dev/testeapi_php/";
    private static Retrofit retrofit = null;




    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://abby-unbeaued-tyron.ngrok-free.dev/testeapi_php/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
