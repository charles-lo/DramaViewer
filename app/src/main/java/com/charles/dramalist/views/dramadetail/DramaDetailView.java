package com.charles.dramalist.views.dramadetail;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.charles.dramalist.R;
import com.charles.dramalist.api.model.Datum;
import com.charles.dramalist.databinding.DramaDetailBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 *
 */

public class DramaDetailView extends AppCompatActivity {

    Context context;
    DramaDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DramaDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = DramaDetailView.this;

        try {
            displayDrama((Datum) getIntent().getSerializableExtra("drama"));
        } catch (Exception e) {
            displayMessage(getString(R.string.error_detail));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayMessage(String message) {
        Snackbar.make(binding.dramaDetailMain, message, Snackbar.LENGTH_LONG).show();
    }

    public void displayDrama(Datum datum) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(datum.getName());
        }
        Glide.with(context).load(datum.getThumb()).placeholder(R.drawable.ic_logo).into(binding.imgThumbDetail);

        binding.nameDetail.setText(datum.getName());
        binding.ratingDetail.setText(String.format(getString(R.string.rating), datum.getRating()));
        binding.createdAtDetail.setText(String.format(getString(R.string.created_time), datum.getCreatedAt()));
        binding.totalViews.setText(String.format(getString(R.string.total_view), datum.getTotalViews()));
    }
}