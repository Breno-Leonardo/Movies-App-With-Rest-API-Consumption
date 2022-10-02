package com.breno.tokenlabfilmes.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.breno.tokenlabfilmes.model.DetailedMovie;
import com.breno.tokenlabfilmes.network.ApiMovies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedMovieViewModel extends AndroidViewModel {
    private final Context context;

    public MutableLiveData<String> currentDetailedMovieID = new MutableLiveData<>(null);
    public MutableLiveData<DetailedMovie> currentDetailedMovie = new MutableLiveData<>(null);
    public MutableLiveData<Boolean> responseDetailedMovie = new MutableLiveData<>();

    public DetailedMovieViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public Boolean getDetailedMovie() {

        ApiMovies.getInstance().getDetailedMovie(String.valueOf(currentDetailedMovieID.getValue())).
                enqueue(new Callback<DetailedMovie>() {
                    @Override
                    public void onResponse(@NonNull Call<DetailedMovie> call, @NonNull Response<DetailedMovie> response) {
                        currentDetailedMovie.setValue(response.body());
                        responseDetailedMovie.setValue(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<DetailedMovie> call, @NonNull Throwable t) {
                        responseDetailedMovie.setValue(false);

                    }
                });
        return responseDetailedMovie.getValue();

    }

    public boolean saveOnCacheDetailedMovie() {
        String json = new Gson().toJson(currentDetailedMovie.getValue());
        ObjectOutput out = null;
        boolean response = true;
        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(), "") + String.valueOf(currentDetailedMovieID.getValue()) + ".srl"));
        } catch (IOException e) {
            e.printStackTrace();
            response = false;
        }
        try {
            if (out != null)
                out.writeObject(json);
        } catch (IOException e) {
            e.printStackTrace();
            response = false;
        }
        try {
            if (out != null)
                out.close();
        } catch (IOException e) {
            e.printStackTrace();
            response = false;
        }
        return response;
    }

    public boolean loadDetailedMovieOnCache() {
        ObjectInputStream in;
        boolean response = true;

        try {
            in = new ObjectInputStream(new FileInputStream(new File(context.getCacheDir(), "") + String.valueOf(currentDetailedMovieID.getValue()) + ".srl"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Gson gsonResp = new GsonBuilder().setPrettyPrinting().create();
        try {
            DetailedMovie movie = gsonResp.fromJson(String.valueOf(in.readObject()), DetailedMovie.class);
            currentDetailedMovie.setValue(movie);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response = false;
        } catch (IOException e) {
            e.printStackTrace();
            response = false;
        }
        return response;
    }


}