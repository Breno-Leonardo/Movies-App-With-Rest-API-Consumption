package com.breno.tokenlabfilmes.network;

import com.breno.tokenlabfilmes.model.DetailedMovie;
import com.breno.tokenlabfilmes.model.SummaryMovie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {

        @GET("/movies-v2")
        Call<List<SummaryMovie>> getSummaryMovies();

        @GET("/movies-v2/{id}")
        Call<DetailedMovie> getDetailedMovie(@Path("id") String id);

}
