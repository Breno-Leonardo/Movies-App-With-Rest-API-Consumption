package com.breno.tokenlabfilmes.view;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.breno.tokenlabfilmes.R;
import com.breno.tokenlabfilmes.databinding.ActivityDetailedMovieBinding;
import com.breno.tokenlabfilmes.model.DetailedMovie;
import com.breno.tokenlabfilmes.ui.viewmodel.DetailedMovieViewModel;
import com.squareup.picasso.Picasso;


public class MovieActivity extends AppCompatActivity {
    private ActivityDetailedMovieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        binding = ActivityDetailedMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DetailedMovieViewModel viewModel = getViewModel();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getViewModel().currentDetailedMovieID.setValue(extras.getString("id"));
        }
        viewModel.getDetailedMovie();// get detailedMovie


        viewModel.currentDetailedMovie.observe(this, Observer -> {
            if (Observer != null) {
                LinearLayout movieLayout = binding.containerDetailedFilm;
                movieLayout.setVisibility(View.VISIBLE);

                TextView vote = binding.movieDetaliedVoteAverage,
                        tittle = binding.movieDetailedTittle,
                        overview = binding.movieDetailedOverview,
                        genres = binding.movieDetaliedGenres,
                        voteCount = binding.movieDetaliedVoteCount,
                        runtime = binding.movieDetaliedRuntime,
                        date = binding.movieDetaliedReleaseDate;
                ImageView posterDetailedMovie = binding.posterDetailedMovie;
                DetailedMovie movie = viewModel.currentDetailedMovie.getValue();

                vote.setText(getString(R.string.vote_average) + " " + movie.getVote_average() + "/10");
                date.setText(getString(R.string.release_date) + " " + movie.getRelease_date().replace("-", "/"));
                runtime.setText(getString(R.string.runtime) + " " + movie.getRuntime() + " " + getString(R.string.minutes));
                voteCount.setText(getString(R.string.vote_count) + " " + movie.getVote_count());
                overview.setText(getString(R.string.overview) + movie.getOverview());
                tittle.setText(movie.getTitle());
                for (int i = 0; i < movie.getGenres().length; i++) {
                    if (i > 0)
                        genres.setText(genres.getText() + ", " + movie.getGenres()[i]);
                    else
                        genres.setText(genres.getText() + " " + movie.getGenres()[i]);

                }
                Picasso.get().load(movie.getPoster_url()).resize(250, 400).into(posterDetailedMovie);
                viewModel.saveOnCacheDetailedMovie();
                showDetailedMovie();
            }
        });

        viewModel.responseDetailedMovie.observe(this, Observer -> {
              if (!Observer) {
                   showError();
                    viewModel.loadDetailedMovieOnCache();
            }
        });


    }

    public DetailedMovieViewModel getViewModel() {
        return new ViewModelProvider(this).get(DetailedMovieViewModel.class);
    }

    public void showError(){
        binding.containerWaitDetailedMovie.setVisibility(View.GONE);
        binding.containerErrorDetailedMovie.setVisibility(View.VISIBLE);
    }
    public void showDetailedMovie(){
        binding.containerInfosDetailedMovie.setVisibility(View.GONE);
        binding.containerDetailedFilm.setVisibility(View.VISIBLE);
    }
}


