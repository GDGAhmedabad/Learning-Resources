package com.pranay.rxjavastart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

/**
 * A placeholder fragment containing a simple view.
 */
public class NormalRxAndroidTaskFragment extends Fragment {

    private String TAG = NormalRxAndroidTaskFragment.class.getSimpleName();
    private Observable<Integer> testObjservable;

    public NormalRxAndroidTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainrxMethod();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    /**
     * TestRxJava Method
     */
    private void mainrxMethod() {
        /*====== 1. Define an Observable ===*/
        /*
        * We’ve declared a field integerObservable that holds an Observable parameterized by Integer.
        * Observable.just() method creates an Observable from parameters passed in –
        * this is the simplest way to create an object that will emit those ints in a ReactiveX way.
        * Now integerObservable is ready to do its job,
        * but it’s waiting for some interested party to subscribe to it.
        * */
        testObjservable = Observable.just(2, 5, 8, 9, 6, 9);


        //=== 3. Subscribe Observer to Observable ===
        testObjservable.subscribe(integerObserver);

        /* Inline the Observer code */
        /*testObjservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"Value:" + integer);
            }
        });*/

        usingOperator();
        usingOperatorComplicatedWay();
    }

    //=== 2. Define an Observer ===
    Observer<Integer> integerObserver = new Observer<Integer>() {

        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError");
        }

        @Override
        public void onNext(Integer integer) {
            Log.d(TAG, "onNext:" + integer);
        }
    };


    /*Calculating square of numbers, ReactiveX way by using below code*/
    private void usingOperator() {
        Log.d(TAG, "usingOperator");
        testObjservable.map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer * integer;
            }
        }).subscribe(integerObserver);
    }

    private void usingOperatorComplicatedWay() {
        Log.d(TAG, "usingOperatorComplicatedWay");
        testObjservable.map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return Integer.toBinaryString(integer);
            }
        }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.endsWith("1");
            }
        }).map(new Func1<String, Integer>() {

            @Override
            public Integer call(String s) {
                return Integer.parseInt(s, 2);
            }
        }).subscribe(integerObserver);
    }
}
