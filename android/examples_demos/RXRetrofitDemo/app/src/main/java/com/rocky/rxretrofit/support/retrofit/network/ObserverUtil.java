package com.rocky.rxretrofit.support.retrofit.network;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ObserverUtil {
    /**
     * @param observable          make it via ApiClient.getClient().create(Class_Name).method()
     * @param compositeDisposable get it from base Activity or base Fragment
     * @param apiNames            APIName which will help identify it
     * @param callback            to receive callback
     */
    public static <T> void subscribeToList(Observable<List<T>> observable,
                                           CompositeDisposable compositeDisposable,
                                           WebserviceBuilder.ApiNames apiNames,
                                           ListCallback callback) {
        makeObservable(observable)
                .flatMap(new Function<List<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull List<T> ts) throws Exception {
                        return Observable.fromIterable(ts);
                    }
                })
                .subscribe(getObserver(compositeDisposable, apiNames, callback));
    }

    /**
     * @param observable make it via ApiClient.getClient().create(Class_Name).method()
     * @return Observable which helps in adding other functions like flatMap(), filter()
     */
    public static <T> Observable<T> makeObservable(Observable<T> observable) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    /**
     * @return Observer with which observable will subscribe
     */
    public static <T> Observer<T> getObserver(final CompositeDisposable compositeDisposable,
                                              final WebserviceBuilder.ApiNames apiNames, final ListCallback callback) {
        return new Observer<T>() {

            @Override
            public void onSubscribe(Disposable d) {
                if (compositeDisposable != null) compositeDisposable.add(d);
            }

            @Override
            public void onNext(T value) {
                if (callback != null) callback.onListNext(value, apiNames);
            }


            @Override
            public void onError(Throwable e) {
                if (callback != null) callback.onFailure(e, apiNames);
            }

            @Override
            public void onComplete() {
                if (callback != null) callback.onListComplete(apiNames);
            }
        };
    }

    /**
     * @param observable          make it via ApiClient.getClient().create(Class_Name).method()
     * @param compositeDisposable get it from base Activity or base Fragment
     * @param apiNames            APIName which will help identify it
     * @param singleCallback      to receive callback
     */
    public static <T> void subscribeToSingle(Observable<T> observable,
                                             CompositeDisposable compositeDisposable,
                                             WebserviceBuilder.ApiNames apiNames,
                                             SingleCallback singleCallback) {
        makeSingle(observable).subscribe(getSingleObserver(compositeDisposable, apiNames, singleCallback));
    }

    /**
     * @param observable make it via ApiClient.getClient().create(Class_Name).method()
     * @return Single which helps in adding other functions like flatMap(), filter()
     */
    public static <T> Single<T> makeSingle(Observable<T> observable) {
        return Single.fromObservable(observable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    /**
     * @return SingleObserver with which observable will subscribe
     */
    public static <T> SingleObserver<T> getSingleObserver(final CompositeDisposable compositeDisposable,
                                                          final WebserviceBuilder.ApiNames apiNames,
                                                          final SingleCallback tSingleCallback) {
        return new SingleObserver<T>() {

            @Override
            public void onSubscribe(Disposable d) {
                if (compositeDisposable != null) compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull T t) {
                if (tSingleCallback != null) tSingleCallback.onSingleSuccess(t, apiNames);
            }


            @Override
            public void onError(Throwable e) {
                if (tSingleCallback != null) tSingleCallback.onFailure(e, apiNames);
            }
        };
    }
}
