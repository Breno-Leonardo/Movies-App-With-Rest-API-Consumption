package com.breno.tokenlabfilmes.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;

import com.breno.tokenlabfilmes.databinding.ActivityMainBinding;
import com.breno.tokenlabfilmes.ui.viewmodel.SummaryMoviesViewModel;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SummaryMoviesViewModel viewModel = getViewModel();
        viewModel.getMovies();// get summary movies

        viewModel.summaryMovies.observe(this, Observer -> {
            if (Observer != null) {
                RecyclerView recyclerView = binding.recyclerViewMovies;
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new Adapter(viewModel.summaryMovies.getValue()));
                viewModel.saveOnCacheSummaryMovies();
                if (Boolean.FALSE.equals(viewModel.responseSummaryMovies.getValue())) {//cache
                    showMoviesOffline();
                }
            }
        });

        viewModel.responseSummaryMovies.observe(this, Observer -> {
            if (Observer) {
                showMoviesOnline();
            } else {
                if (binding.containerSummaryMovies.getVisibility() != View.VISIBLE) {//
                    showError();
                    viewModel.loadSummaryMoviesOnCache();
                }


                new CountDownTimer(5000, 5000) {//trying to reconnect
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        System.out.println("Tentando conectar");
                        viewModel.getMovies();// get summary movies
                        start();
                        this.cancel();//avoid creating multiple timers
                    }
                }.start();
            }
        });


    }

    public SummaryMoviesViewModel getViewModel() {
        return new ViewModelProvider(this).get(SummaryMoviesViewModel.class);
    }

    public void showError() {
        binding.containerWait.setVisibility(View.GONE);
        binding.containerError.setVisibility(View.VISIBLE);
    }

    public void showMoviesOnline() {
        binding.containerInfos.setVisibility(View.GONE);
        binding.containerSummaryMovies.setVisibility(View.VISIBLE);
        binding.alertOffline.setVisibility(View.GONE);
    }

    public void showMoviesOffline() {
        binding.containerInfos.setVisibility(View.GONE);
        binding.containerSummaryMovies.setVisibility(View.VISIBLE);
        binding.alertOffline.setVisibility(View.VISIBLE);
    }

}