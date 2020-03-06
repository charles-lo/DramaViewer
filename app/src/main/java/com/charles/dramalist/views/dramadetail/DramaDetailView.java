package com.charles.dramalist.views.dramadetail;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.dramalist.R;
import com.charles.dramalist.api.model.Datum;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

/**
 *
 */

public class DramaDetailView extends AppCompatActivity {

    Context context;
    LinearLayout main;
    ImageView imgThumb;
    TextView txtDramaName, txtRating, txtCreatedAt, txtTotalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drama_detail);

        context = DramaDetailView.this;

        main = findViewById(R.id.drama_detail_main);
        imgThumb = findViewById(R.id.imgThumbDetail);
        txtDramaName = findViewById(R.id.name_detail);
        txtRating = findViewById(R.id.rating_detail);
        txtCreatedAt = findViewById(R.id.created_at_detail);
        txtTotalView = findViewById(R.id.total_views);

        try {
            displayDrama((Datum) getIntent().getSerializableExtra("drama"));
        } catch (Exception e) {
            displayMessage("Problem while getting drama info, Try again.");
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
        Snackbar.make(main, message, Snackbar.LENGTH_LONG).show();
    }

    public void displayDrama(Datum datum) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(datum.getName());
        }
        Glide.with(context).load(datum.getThumb()).placeholder(R.drawable.ic_logo).into(imgThumb);

        txtDramaName.setText(datum.getName());
        txtRating.setText(String.format("rating : %s", datum.getRating()));
        txtCreatedAt.setText(String.format("created time : %s", datum.getCreatedAt()));
        txtTotalView.setText(String.format("total view : %s", datum.getTotalViews()));
    }
}