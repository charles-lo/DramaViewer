package com.charles.dramalist.views.dramalist;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import com.charles.dramalist.api.DramaViewModel;
import com.charles.dramalist.api.model.Datum;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.charles.dramalist.R;

import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.filter;


public class DramaListView extends AppCompatActivity {

    Context context;
    LinearLayout main;
    TextView txtNoDrama;
    ShimmerRecyclerView listDrama;
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Datum> dataDrama = new ArrayList<>();
    private DramaAdapter adapter;

    DramaViewModel viewModel;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drama_list);

        context = DramaListView.this;

        viewModel = new ViewModelProvider(this).get(DramaViewModel.class);

        main = findViewById(R.id.drama_list_main);
        txtNoDrama = findViewById(R.id.txtNoDrama);
        listDrama = findViewById(R.id.listDrama);
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.collapseActionView();
            }
            fetchData();
        });

        adapter = new DramaAdapter(context, dataDrama);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listDrama.setLayoutManager(mLayoutManager);
        listDrama.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        listDrama.setItemAnimator(new DefaultItemAnimator());
        listDrama.setAdapter(adapter);

        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setQueryHint("Search for drama name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return false;
            }
        });
        return true;
    }


    private void filterData(String keyword) {
        Predicate<Datum> validPredicate = item -> {
            assert item != null;
            return item.getName().toLowerCase().contains(keyword.toLowerCase());
        };

        if (viewModel.drama.getValue() != null) {
            List<Datum> filterData = new ArrayList(viewModel.drama.getValue());
            filter(filterData, validPredicate);
            displayTracks(filterData);
        }
        adapter.setHighLight(keyword);
        adapter.notifyDataSetChanged();
    }

    public void fetchData() {
        swipeRefreshLayout.setRefreshing(false);
        txtNoDrama.setVisibility(View.GONE);
        listDrama.setVisibility(View.VISIBLE);

        dataDrama.clear();
        adapter.notifyDataSetChanged();

        setLoadingIndicator(true);
        viewModel.fetchTracks();

        viewModel.drama.observe(this, result -> {
            if (result != null) {
                if (result.size() > 0) {
                    displayTracks(result);
                } else {
                    displayMessage("No video found, Try again.");
                }
            } else {
                displayMessage("network error, check again.");
            }
        });

    }

    public void displayTracks(List<Datum> dataTracks) {
        setLoadingIndicator(false);
        this.dataDrama.clear();
        this.dataDrama.addAll(dataTracks);
        adapter.notifyDataSetChanged();
    }

    public void displayMessage(String message) {
        setLoadingIndicator(false);
        Snackbar.make(main, message, Snackbar.LENGTH_LONG).show();
    }

    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            listDrama.showShimmerAdapter();
        } else {
            listDrama.hideShimmerAdapter();
        }
    }
}