package com.charles.dramalist.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.charles.dramalist.api.model.Datum;

import java.util.List;

public class DramaViewModel extends ViewModel {

    public LiveData<List<Datum>> drama = AppRepository.getInstance().getDrama();
    public LiveData<Boolean> networkStatus = AppRepository.getInstance().getNetworkStatus();

    public void fetchDrama() {
        AppRepository.getInstance().fetchDrama();
    }

    public void setNetworkStatus(Boolean status) {
        AppRepository.getInstance().setNetworkStatus(status);
    }
}
