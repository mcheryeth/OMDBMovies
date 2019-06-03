package com.melvillec.salesforceomdb.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.melvillec.salesforceomdb.R;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.databinding.MovieSearchListFragmentBinding;
import com.melvillec.salesforceomdb.factory.ViewModelFactory;
import com.melvillec.salesforceomdb.ui.adapters.MovieListRecyclerAdapter;
import com.melvillec.salesforceomdb.ui.custom.RecyclerItemClickListener;
import com.melvillec.salesforceomdb.ui.custom.RecyclerViewPaginator;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieSearchListViewModel;
import com.melvillec.salesforceomdb.utils.IntentRouter;
import com.melvillec.salesforceomdb.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieSearchListFragment extends Fragment implements SearchView.OnQueryTextListener, RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    @Inject
    ViewModelFactory viewModelFactory;

    private MovieSearchListFragmentBinding dataBinding;
    private MovieSearchListViewModel movieSearchListViewModel;
    private MovieListRecyclerAdapter movieListRecyclerAdapter;

    public static MovieSearchListFragment newInstance() {
        MovieSearchListFragment fragment = new MovieSearchListFragment();
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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        initializeUI();

        return dataBinding.getRoot();
    }

    private void initializeUI() {
        initializeSearchView();

        dataBinding.moviesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBinding.moviesRv.setHasFixedSize(true);
        movieListRecyclerAdapter = new MovieListRecyclerAdapter();
        dataBinding.moviesRv.setAdapter(movieListRecyclerAdapter);

        dataBinding.moviesRv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));

        /* RecyclerViewPaginator to handle pagination */
        dataBinding.moviesRv.addOnScrollListener(new RecyclerViewPaginator(dataBinding.moviesRv) {
            @Override
            public boolean isLastPage() {
                return movieSearchListViewModel.isLastPage();
            }

            @Override
            public void loadMore(Long page) {
                movieSearchListViewModel.loadMovies(getSearchQuery(), page);
            }

            @Override
            public void loadFirstData(Long page) {
                //no-op
            }
        });

        dataBinding.swipeContainer.setOnRefreshListener(() -> fetchMovies(getSearchQuery()));
        dataBinding.swipeContainer.setColorSchemeResources(R.color.colorAccent);
    }

    private String getSearchQuery() {
        return dataBinding.searchView.getQuery().toString();
    }

    private void initializeSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        dataBinding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        dataBinding.searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        dataBinding.searchView.setIconifiedByDefault(false);
        dataBinding.searchView.setOnQueryTextListener(this);

        EditText searchEditText = dataBinding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    protected void initializeViewModel() {
        movieSearchListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieSearchListViewModel.class);
        movieSearchListViewModel.getMoviesLiveData().observe(this, resource -> {
            if (resource.isLoading()) {
                //no-op
            } else if (resource.isSuccess()) {
                updateMovieList(resource.data);
            } else {
                handleErrorResponse(resource.message);
            }
        });
    }

    private void fetchMovies(String searchQuery) {
        movieListRecyclerAdapter.clearItems();

        if (searchQuery != null && !searchQuery.isEmpty()) {
            displayProgressBar();
            movieSearchListViewModel.loadMovies(searchQuery, 1L);
        } else {
            dataBinding.swipeContainer.setRefreshing(false);
        }
    }

    protected void updateMovieList(List<MovieEntity> movieEntities) {
        hideProgressBar();
        dataBinding.swipeContainer.setRefreshing(false);
        dataBinding.moviesRv.setVisibility(View.VISIBLE);
        movieListRecyclerAdapter.setItems(movieEntities);
    }

    protected void handleErrorResponse(String message) {
        hideProgressBar();
        dataBinding.swipeContainer.setRefreshing(false);
        if (message == null || message.length() == 0) {
            message = getString(R.string.error_movie_fetch);
        }
        Snackbar.make(dataBinding.moviesRv, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void displayProgressBar() {
        dataBinding.moviesRv.setVisibility(View.GONE);
        dataBinding.progressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        dataBinding.moviesRv.setVisibility(View.VISIBLE);
        dataBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Utils.hideKeyboard(getActivity());
        fetchMovies(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemClick(View parentView, View childView, int position) {
        movieSearchListViewModel.onStop();
        IntentRouter.launchDetailActivity(requireActivity(),
                movieListRecyclerAdapter.getItem(position).getImdbID());
    }
}