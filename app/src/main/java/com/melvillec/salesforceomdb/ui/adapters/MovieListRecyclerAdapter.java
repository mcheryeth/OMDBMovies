package com.melvillec.salesforceomdb.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.databinding.MovieListItemBinding;
import com.melvillec.salesforceomdb.ui.viewholders.MovieListItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MovieListRecyclerAdapter extends RecyclerView.Adapter<MovieListItemViewHolder> {

    private List<MovieEntity> moviesList;
    public MovieListRecyclerAdapter() {
        this.moviesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieListItemBinding itemBinding = MovieListItemBinding.inflate(layoutInflater, parent, false);
        return new MovieListItemViewHolder(itemBinding);
    }

    public void setItems(List<MovieEntity> movies) {
        int startPosition = this.moviesList.size();
        this.moviesList.addAll(movies);
        notifyItemRangeChanged(startPosition, movies.size());
    }

    public void clearItems() {
        this.moviesList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public MovieEntity getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListItemViewHolder viewHolder, int position) {
        viewHolder.configureView(getItem(position));
    }
}
