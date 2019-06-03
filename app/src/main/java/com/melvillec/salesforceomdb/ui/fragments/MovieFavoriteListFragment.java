package com.melvillec.salesforceomdb.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.melvillec.salesforceomdb.R;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.databinding.MovieFavoriteListFragmentBinding;
import com.melvillec.salesforceomdb.factory.ViewModelFactory;
import com.melvillec.salesforceomdb.ui.adapters.MovieListRecyclerAdapter;
import com.melvillec.salesforceomdb.ui.custom.RecyclerItemClickListener;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieFavoriteListViewModel;
import com.melvillec.salesforceomdb.utils.IntentRouter;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * OMDB Favorites fragment
 */
public class MovieFavoriteListFragment extends Fragment implements RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    @Inject
    ViewModelFactory viewModelFactory;

    private MovieFavoriteListFragmentBinding dataBinding;
    private MovieFavoriteListViewModel movieFavoriteListViewModel;
    private MovieListRecyclerAdapter movieListRecyclerAdapter;

    public static MovieFavoriteListFragment newInstance() {
        MovieFavoriteListFragment fragment = new MovieFavoriteListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);

        initializeViewModel();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);

        initializeUI();
        fetchFavorites();

        return dataBinding.getRoot();
    }

    private void initializeUI() {
        dataBinding.moviesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBinding.moviesRv.setHasFixedSize(true);
        movieListRecyclerAdapter = new MovieListRecyclerAdapter();
        dataBinding.moviesRv.setAdapter(movieListRecyclerAdapter);

        dataBinding.moviesRv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));

        dataBinding.swipeContainer.setOnRefreshListener(this::fetchFavorites);
        dataBinding.swipeContainer.setColorSchemeResources(R.color.colorAccent);
    }

    private void initializeViewModel() {
        movieFavoriteListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieFavoriteListViewModel.class);
        movieFavoriteListViewModel.getMoviesLiveData().observe(this, resource -> {
            if (resource.isLoading()) {
                //no-op
            } else if (resource.isSuccess()) {
                updateMovieList(resource.data);
            } else {
                handleErrorResponse(resource.message);
            }
        });
    }

    private void fetchFavorites() {
        movieListRecyclerAdapter.clearItems();
        displayProgressBar();
        movieFavoriteListViewModel.fetchFavorites();
    }

    private void updateMovieList(List<MovieEntity> movieEntities) {
        hideProgressBar();
        dataBinding.swipeContainer.setRefreshing(false);
        dataBinding.moviesRv.setVisibility(View.VISIBLE);
        movieListRecyclerAdapter.setItems(movieEntities);
    }

    private void handleErrorResponse(String message) {
        hideProgressBar();
        dataBinding.swipeContainer.setRefreshing(false);
        if (message == null || message.length() == 0) {
            message = getString(R.string.error_movie_fetch);
        }
        Snackbar.make(dataBinding.moviesRv, message, Snackbar.LENGTH_SHORT).show();
    }

    private void displayProgressBar() {
        dataBinding.moviesRv.setVisibility(View.GONE);
        dataBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        dataBinding.moviesRv.setVisibility(View.VISIBLE);
        dataBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View parentView, View childView, int position) {
        movieFavoriteListViewModel.onStop();
        IntentRouter.launchDetailActivity(requireActivity(),
                movieListRecyclerAdapter.getItem(position).getImdbID());
    }
}