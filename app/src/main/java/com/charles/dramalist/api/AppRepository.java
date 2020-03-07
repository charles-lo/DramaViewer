package com.charles.dramalist.api;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.charles.dramalist.api.model.Datum;
import com.charles.dramalist.data.BoxManager;

import java.util.List;

import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AppRepository {
    private static final String TAG = AppRepository.class.getSimpleName();
    private static volatile AppRepository INSTANCE;

    private CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<List<Datum>> drama = new MutableLiveData<>();

    private AppRepository() {
        // Prevent form the reflection api.
        if (INSTANCE != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static AppRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) INSTANCE = new AppRepository();
            }
        }
        return INSTANCE;
    }

    LiveData<List<Datum>> getDrama() {
        return drama;
    }

    private Query<Datum> postBox = BoxManager.getStore().boxFor(Datum.class).query().build();

    void fetchDrama() {
        disposables.add(RxQuery.single(postBox)
                .observeOn(Schedulers.io())
                .flatMap(result -> {
                    drama.postValue(result);
                    return ServiceFactory.getInstance().fetchDrama();
                })
                .subscribeOn(Schedulers.io())
                .subscribe(
                        result -> drama.postValue(result.getData()),
                        throwable -> drama.postValue(null)
                ));
    }
}
