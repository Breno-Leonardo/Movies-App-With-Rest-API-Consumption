package com.breno.tokenlabfilmes.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.breno.tokenlabfilmes.R;
import com.breno.tokenlabfilmes.model.SummaryMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MoviesViewHolder> {

    private List<SummaryMovie> movies;

    public Adapter(List<SummaryMovie> summaryMovies) {

        this.movies = summaryMovies;
    }


    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_movie, parent, false);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.tittle.setText(movies.get(position).getTitle());
        holder.vote.setText(String.valueOf(movies.get(position).getVote_average()));
        int size = Math.min(movies.get(position).getGenres().length, holder.types.length);
        for (int i = 0; i < size; i++) {
            holder.types[i].setVisibility(View.VISIBLE);
            holder.types[i].setText(movies.get(position).getGenres()[i]);
        }
        holder.container.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MovieActivity.class);
            intent.putExtra("id", String.valueOf(movies.get(position).getId()));
            v.getContext().startActivity(intent);
        });
        //        Picasso.get().load(movies.get(position).getPoster_url()).resize((int) holder.container.getResources().getDimension(R.dimen.cardW),(int) holder.container.getResources().getDimension(R.dimen.cardH)).into(holder.imagePoster);
        Picasso.get().load(movies.get(position).getPoster_url()).resize(250,400).into(holder.imagePoster);

    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        }

        return 0;
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {

        TextView vote, tittle;
        TextView types[];
        LinearLayout container;
        ImageView imagePoster;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster=itemView.findViewById(R.id.poster_movie_summary);
            tittle = itemView.findViewById(R.id.movie_title_summary);
            vote = itemView.findViewById(R.id.vote_summary);
            types = new TextView[]{itemView.findViewById(R.id.movie_type_1), itemView.findViewById(R.id.movie_type_2), itemView.findViewById(R.id.movie_type_3)};
            container = itemView.findViewById(R.id.containerMovie);

        }
    }
}
