package com.charles.dramalist.views.dramalist;

import android.app.SearchManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.charles.dramalist.R;
import com.charles.dramalist.api.DramaViewModel;
import com.charles.dramalist.api.model.Datum;
import com.charles.dramalist.receiver.ConnectivityStatusReceiver;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.prefs.Prefs;

import static org.apache.commons.collections4.CollectionUtils.filter;


public class DramaListView extends AppCompatActivity {
    static final String TAG = DramaListView.class.getSimpleName();
    static final String KEYWORD = "keyword";

    Context context;
    RelativeLayout main;
    TextView txtNoDrama;
    ShimmerRecyclerView listDrama;
    SwipeRefreshLayout swipeRefreshLayout;
    Snackbar snackBar;
    SearchView searchView;
    Menu menuView;

    private List<Datum> dataDrama = new ArrayList<>();
    private DramaAdapter adapter;
    String keyWord;

    ConnectivityStatusReceiver connectivityStatusReceiver;
    DramaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drama_list);
        viewModel = new ViewModelProvider(this).get(DramaViewModel.class);
        context = DramaListView.this;
        keyWord = Prefs.with(this).read(KEYWORD);

        main = findViewById(R.id.drama_list_main);
        txtNoDrama = findViewById(R.id.txtNoDrama);
        listDrama = findViewById(R.id.listDrama);
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this::fetchData);
        adapter = new DramaAdapter(context, dataDrama);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listDrama.setLayoutManager(mLayoutManager);
        listDrama.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        listDrama.setItemAnimator(new DefaultItemAnimator());
        listDrama.setAdapter(adapter);

        registerConnectivityMonitor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter.getItemCount() < 1) {
            fetchData();
        }
    }

    @Override
    protected void onStop() {
        Prefs.with(this).write(KEYWORD, keyWord);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectivityStatusReceiver != null) {
            // unregister receiver
            unregisterReceiver(connectivityStatusReceiver);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuView = menu;
        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setQueryHint(getString(R.string.search_drama_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                keyWord = newText;
                return false;
            }
        });
        initViewMode();
        return true;
    }

    private void initViewMode() {
        viewModel.drama.observe(this, result -> {
            if (result != null) {
                txtNoDrama.setVisibility(View.GONE);
                if (result.size() > 0) {
                    displayDrama(result);
                    if (searchView != null && !TextUtils.isEmpty(keyWord)) {
                        String word = keyWord;
                        filterData(word);
                        menuView.performIdentifierAction(R.id.action_search, 0);
                        searchView.setQuery(word, true);
                    }
                } else {
                    displayMessage(getString(R.string.error_no_drama), Snackbar.LENGTH_LONG);
                    dataDrama.clear();
                }
            } else {
                displayMessage(getString(R.string.error_network), Snackbar.LENGTH_INDEFINITE);
                if (adapter.getItemCount() < 1) {
                    txtNoDrama.setVisibility(View.VISIBLE);
                    dataDrama.clear();
                }
            }
            adapter.notifyDataSetChanged();
        });
        viewModel.networkStatus.observe(this, result -> {
            if (result != null) {
                if (result) {
                    fetchData();
                    if (snackBar != null) {
                        snackBar.dismiss();
                    }
                } else {
                    displayMessage(getString(R.string.error_network), Snackbar.LENGTH_INDEFINITE);
                }
            }
        });
    }

    private void registerConnectivityMonitor() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityStatusReceiver = new ConnectivityStatusReceiver();
        registerReceiver(connectivityStatusReceiver, intentFilter);
    }

    private void filterData(String keyword) {
        Predicate<Datum> validPredicate = item -> {
            assert item != null;
            return item.getName().toLowerCase().contains(keyword.toLowerCase());
        };

        if (viewModel.drama.getValue() != null) {
            List<Datum> filterData = new ArrayList<>(viewModel.drama.getValue());
            filter(filterData, validPredicate);
            displayDrama(filterData);
        }
        adapter.setHighLight(keyword);
        adapter.notifyDataSetChanged();
    }

    public void fetchData() {
        swipeRefreshLayout.setRefreshing(false);
        setLoadingIndicator(true);
        viewModel.fetchDrama();
    }

    public void displayDrama(List<Datum> data) {
        setLoadingIndicator(false);
        this.dataDrama.clear();
        this.dataDrama.addAll(data);
        adapter.notifyDataSetChanged();
    }

    public void displayMessage(String message, int duration) {
        setLoadingIndicator(false);
        snackBar = Snackbar.make(main, message, duration);
        snackBar.show();
    }

    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            listDrama.showShimmerAdapter();
        } else {
            listDrama.hideShimmerAdapter();
        }
    }
}