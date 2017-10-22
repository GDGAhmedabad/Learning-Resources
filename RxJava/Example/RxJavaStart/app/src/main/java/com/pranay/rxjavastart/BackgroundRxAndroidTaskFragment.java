package com.pranay.rxjavastart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class BackgroundRxAndroidTaskFragment extends Fragment {

    private String TAG = BackgroundRxAndroidTaskFragment.class.getSimpleName();
    private View rootView;

    public BackgroundRxAndroidTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        callBackgroundTaskObjservable();
        return rootView;
    }

    private void callBackgroundTaskObjservable() {
        Observable<Integer> backgroundTaskObjservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                int start = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exception) {
                        subscriber.onError(exception);
                    }

                    subscriber.onNext(start++);

                    if (start == 10) {
                        break;
                    }

                }
                subscriber.onCompleted(); // task completed
            }
        });

        // normal call
        //backgroundTaskObjservable.subscribe(integerObserver);


        //Using schedulers
        //backgroundTaskObjservable.subscribeOn(Schedulers.computation()).subscribe(integerObserver);

        //Using schedulers with AndroidSchedulers
        /*
        *  RxAndroid provides custom Scheduler specific to Android, to ensure that any work that
        *  needs to be done in UI Thread, can be simply declared this way.
        *  The following code requests computing the data on the background thread,
        * but results will be delivered on UI Thread,
        *  so we can safely update UI from Observer’s callbacks*/
        final Subscription subscription = backgroundTaskObjservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerObserver);
        rootView.findViewById(R.id.tvHelloWorld).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
        });
    }

    Observer<Integer> integerObserver = new Observer<Integer>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError", e);
        }

        @Override
        public void onNext(Integer integer) {
            Log.d(TAG, "onNext: " + integer);
        }
    };
}
