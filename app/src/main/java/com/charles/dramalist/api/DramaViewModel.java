package com.charles.dramalist.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.charles.dramalist.api.model.Datum;

import java.util.List;

public class DramaViewModel extends ViewModel {

    public LiveData<List<Datum>> drama = AppRepository.getInstance().getDrama();

    public void fetchTracks() {
        AppRepository.getInstance().fetchTracks();
    }
}
