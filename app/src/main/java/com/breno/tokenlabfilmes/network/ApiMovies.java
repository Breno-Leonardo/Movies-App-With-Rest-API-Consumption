package com.breno.tokenlabfilmes.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiMovies {

    private static MovieService INSTANCE;

    public static MovieService getInstance() {
        if (INSTANCE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://desafio-mobile.nyc3.digitaloceanspaces.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            INSTANCE = retrofit.create(MovieService.class);
        }
        return INSTANCE;
    }


}
