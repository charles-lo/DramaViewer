package com.charles.dramalist.api;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.charles.dramalist.api.model.Datum;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AppRepository {
    private static final String TAG = AppRepository.class.getSimpleName();
    private static volatile AppRepository INSTANCE;

    private Context context;
    private CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<List<Datum>> drama = new MutableLiveData<>();

    private AppRepository(Context context) {
        this.context = context;
        // Prevent form the reflection api.
        if (INSTANCE != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    static AppRepository getInstance() {
        return INSTANCE;
    }

    public static AppRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) INSTANCE = new AppRepository(context);
            }
        }
        return INSTANCE;
    }

    LiveData<List<Datum>> getDrama() {
        return drama;
    }

    void fetchDrama() {
        APIService iTuneService = ServiceFactory.getInstance();
        disposables.add(iTuneService.fetchDrama()
                .subscribeOn(Schedulers.io()).subscribe(
                        result -> drama.postValue(result.getData()),
                        throwable -> drama.postValue(null)
                ));
    }
}
