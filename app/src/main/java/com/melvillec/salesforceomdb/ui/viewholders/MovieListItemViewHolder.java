package com.melvillec.salesforceomdb.ui.viewholders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.melvillec.salesforceomdb.R;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.databinding.MovieListItemBinding;
import com.melvillec.salesforceomdb.ui.callbacks.IUpdateMovieCallback;
import com.squareup.picasso.Picasso;

public class MovieListItemViewHolder extends RecyclerView.ViewHolder {

    private MovieListItemBinding binding;
    private IUpdateMovieCallback callback;
    public MovieListItemViewHolder(MovieListItemBinding binding, IUpdateMovieCallback callback) {
        super(binding.getRoot());

        this.binding = binding;
        this.callback = callback;
    }

    public void configureView(MovieEntity movie) {
        binding.nameTv.setText(itemView.getResources().getString(R.string.movie_name, movie.getTitle()));
        binding.directorTv.setText(itemView.getResources().getString(R.string.movie_director, movie.getDirector()));
        binding.directorTv.setVisibility(movie.getDirector() != null && !movie.getDirector().isEmpty() ? View.VISIBLE : View.GONE);
        binding.descriptionTv.setText(itemView.getResources().getString(R.string.movie_plot, movie.getPlot()));
        binding.descriptionTv.setVisibility(movie.getPlot() != null && !movie.getPlot().isEmpty() ? View.VISIBLE : View.GONE);
        binding.yearTv.setText(itemView.getResources().getString(R.string.movie_year, movie.getYear()));
        binding.favoriteIv.setSelected(movie.isFavorite());
        binding.favoriteIv.setOnClickListener(v -> {
            binding.favoriteIv.setSelected(!movie.isFavorite());
            movie.setFavorite(!movie.isFavorite());

            if (callback != null) {
                callback.onMovieUpdated(movie);
            }
        });

        Picasso.get().load(movie.getPoster())
                .placeholder(R.drawable.ic_placeholder_gray_24dp)
                .into(binding.posterIv);
    }
}
