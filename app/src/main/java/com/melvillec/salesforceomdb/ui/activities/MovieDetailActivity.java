package com.melvillec.salesforceomdb.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.melvillec.salesforceomdb.R;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.databinding.MovieDetailActivityBinding;
import com.melvillec.salesforceomdb.factory.ViewModelFactory;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.melvillec.salesforceomdb.utils.IntentRouter.INTENT_EXTRA_IMDB_ID;

public class MovieDetailActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private MovieDetailActivityBinding binding;
    private MovieDetailViewModel movieDetailViewModel;
    private String imdbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initializeUI();
        initializeViewModel();
    }

    private void initializeUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.white));

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        imdbID = getIntent().getExtras().getString(INTENT_EXTRA_IMDB_ID);
    }

    private void initializeViewModel() {
        movieDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel.class);
        movieDetailViewModel.getMovieLiveData().observe(this, resource -> {
            if (resource.data != null) {
                updateViews(resource.data);
            } else {
                handleErrorResponse();
            }
        });

        //Fetch movie details
        fetchMovieDetails();
    }

    private void updateViews(MovieEntity movieEntity) {
        hideProgressBar();

        binding.collapsingToolbar.setTitle(movieEntity.getTitle());
        binding.detailsTv.setText(getString(R.string.movie_details_text_area, movieEntity.getDetails()));
        binding.plotTv.setText(getString(R.string.movie_detail_plot, movieEntity.getPlot()));
        binding.favoriteIv.setSelected(movieEntity.isFavorite());
        binding.favoriteIv.setOnClickListener(v -> {
            binding.favoriteIv.setSelected(!movieEntity.isFavorite());
            movieEntity.setFavorite(!movieEntity.isFavorite());

            movieDetailViewModel.onMovieUpdated(movieEntity);
        });

        Picasso.get().load(movieEntity.getPoster())
                .placeholder(R.drawable.ic_placeholder_gray_24dp)
                .into(binding.detailIv);
    }

    private void handleErrorResponse() {
        hideProgressBar();
        Snackbar.make(binding.scrollView, getString(R.string.error_movie_fetch), Snackbar.LENGTH_SHORT).show();
    }

    private void fetchMovieDetails() {
        displayProgressBar();
        movieDetailViewModel.getMovieDetails(imdbID);
    }

    private void displayProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }
}
