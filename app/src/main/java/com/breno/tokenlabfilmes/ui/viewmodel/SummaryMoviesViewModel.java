package com.breno.tokenlabfilmes.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.breno.tokenlabfilmes.model.SummaryMovie;
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
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryMoviesViewModel extends AndroidViewModel {
    private final Context context;

    public MutableLiveData<List<SummaryMovie>> summaryMovies = new MutableLiveData<>(null);
    public MutableLiveData<Boolean> responseSummaryMovies = new MutableLiveData<>(false);

    public SummaryMoviesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public Boolean getMovies() {

        ApiMovies.getInstance().getSummaryMovies().
                enqueue(new Callback<List<SummaryMovie>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SummaryMovie>> call, @NonNull Response<List<SummaryMovie>> response) {
                        responseSummaryMovies.setValue(true);
                        summaryMovies.setValue(response.body());
                        System.out.println("ApiMovies.onResponse code" + response.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SummaryMovie>> call, @NonNull Throwable t) {
                        responseSummaryMovies.setValue(false);


                    }
                });
        return responseSummaryMovies.getValue();
    }

    public boolean saveOnCacheSummaryMovies() {
        String json = new Gson().toJson(summaryMovies.getValue());
        ObjectOutput out = null;
        boolean response = true;
        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(), "") + "summaryMoviesCache.srl"));
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

    public boolean loadSummaryMoviesOnCache() {
        ObjectInputStream in;
        boolean output = true;

        try {
            in = new ObjectInputStream(new FileInputStream(new File(context.getCacheDir(), "") + "summaryMoviesCache.srl"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Gson gsonResp = new GsonBuilder().setPrettyPrinting().create();
        try {
            SummaryMovie[] movies = gsonResp.fromJson(String.valueOf(in.readObject()), SummaryMovie[].class);
            summaryMovies.setValue(Arrays.asList(movies));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            output = false;
        } catch (IOException e) {
            e.printStackTrace();
            output = false;
        }
        return output;
    }

}