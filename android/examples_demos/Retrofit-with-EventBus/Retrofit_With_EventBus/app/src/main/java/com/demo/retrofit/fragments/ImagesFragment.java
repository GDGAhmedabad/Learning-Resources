package com.demo.retrofit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.retrofit.R;
import com.demo.retrofit.activities.BaseFragment;
import com.demo.retrofit.adapters.MyImageRecyclerViewAdapter;
import com.demo.retrofit.network.event.ApiErrorEvent;
import com.demo.retrofit.network.event.ApiErrorWithMessageEvent;
import com.demo.retrofit.network.response.ImageListResponse;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ImagesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LOGTAG = "ImagesFragment";
    private static final String IMAGE_LIST_REQUEST_TAG = LOGTAG + ".imageListRequest";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.list)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private MyImageRecyclerViewAdapter mImageRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImagesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ImagesFragment newInstance(int columnCount) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mImageRecyclerViewAdapter = new MyImageRecyclerViewAdapter(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(mImageRecyclerViewAdapter);
        swipeLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog(getActivity());
        loadImages();
    }

    // ============================================================================================
    // EventBus callbacks
    // ============================================================================================

    /**
     * Response of Image list.
     *
     * @param imageListResponse ImageListResponse
     */
    @Subscribe
    public void onEventMainThread(ImageListResponse imageListResponse) {
        switch (imageListResponse.getRequestTag()) {
            case IMAGE_LIST_REQUEST_TAG:
                dismissProgress();
                mImageRecyclerViewAdapter.updateData(imageListResponse.getImageResultList());
                swipeLayout.setRefreshing(false);
                break;

            default:
                break;
        }
    }


    /**
     * EventBus listener. An API call failed. No error message was returned.
     *
     * @param event ApiErrorEvent
     */
    @Subscribe
    public void onEventMainThread(ApiErrorEvent event) {
        switch (event.getRequestTag()) {
            case IMAGE_LIST_REQUEST_TAG:
                dismissProgress();
                showToast(getString(R.string.error_server_problem));
                swipeLayout.setRefreshing(false);

                break;

            default:
                break;
        }
    }

    /**
     * EventBus listener. An API call failed. An error message was returned.
     *
     * @param event ApiErrorWithMessageEvent Contains the error message.
     */
    @Subscribe
    public void onEventMainThread(ApiErrorWithMessageEvent event) {
        switch (event.getRequestTag()) {
            case IMAGE_LIST_REQUEST_TAG:
                dismissProgress();
                showToast(event.getResultMsgUser());
                swipeLayout.setRefreshing(false);
                break;

            default:
                break;
        }
    }

    private void loadImages() {
        showProgress();

        if (!mApiClient.isRequestRunning(IMAGE_LIST_REQUEST_TAG)) {
            mApiClient.getImageList(IMAGE_LIST_REQUEST_TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        loadImages();
    }
}
