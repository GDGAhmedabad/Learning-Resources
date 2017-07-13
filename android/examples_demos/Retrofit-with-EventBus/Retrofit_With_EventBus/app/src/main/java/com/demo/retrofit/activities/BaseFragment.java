package com.demo.retrofit.activities;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.demo.retrofit.R;
import com.demo.retrofit.RetroFitApp;
import com.demo.retrofit.network.ApiClient;
import com.demo.retrofit.utils.PermissionResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This BaseFragment class is mainly used for avoiding most of the boiler-plate code.
 */
public class BaseFragment extends Fragment {

    private int KEY_PERMISSION = 0;
    private PermissionResult permissionResult;
    private String permissionsAsk[];

    private EventBus mEventBus;
    protected ApiClient mApiClient;

    private Dialog mDialog;
    private Handler mHandler;
    private Context mContext;

    private View mView;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEventBus = EventBus.getDefault();
        mApiClient = getApp().getApiClient();

        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    public RetroFitApp getApp() {
        return (RetroFitApp) getActivity().getApplication();
    }

    /**
     * Shows toast message.
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Initialize Loading Dialog
     */
    protected void initDialog(Context context) {
        this.mContext = context;
        mDialog = new Dialog(mContext); // this or YourActivity
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.loader_layout, null, false);
        mDialog.setContentView(mView);

        mHandler = new Handler();
    }

    protected void dismissProgress() {
        if (mHandler != null && mDialog != null) {
            mHandler.post(() -> mDialog.dismiss());
        }
    }

    protected void showProgress() {
        if (mHandler != null && mDialog != null) {

            mHandler.post(() -> {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
//        hideKeyboard(edt);
            });
        }
    }

    /**
     * Check if the Application required Permission is granted.
     *
     * @param context
     * @param permission
     * @return
     */
    @SuppressWarnings({"MissingPermission"})
    public boolean isPermissionGranted(Context context, String permission) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) ||
                (ContextCompat.checkSelfPermission(context, permission) ==
                        PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Check if the Application required Permissions are granted.
     *
     * @param context
     * @param permissions
     * @return
     */
    public boolean isPermissionsGranted(Context context, String permissions[]) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        boolean granted = true;
        for (String permission : permissions) {
            if (!(ContextCompat.checkSelfPermission(
                    context, permission) == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }

        return granted;
    }

    /**
     * Ask Permission to be granted.
     *
     * @param permissionAsk
     */
    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();

        for (String aPermissionAsk : permissionAsk) {
            if (!isPermissionGranted(getActivity(), aPermissionAsk)) {
                permissionsNotGranted.add(aPermissionAsk);
            }
        }

        if (permissionsNotGranted.isEmpty()) {

            if (permissionResult != null)
                permissionResult.permissionGranted();

        } else {

            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            FragmentCompat.requestPermissions(this, arrayPermissionNotGranted, KEY_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != KEY_PERMISSION) {
            return;
        }

        List<String> permissionDenied = new LinkedList<>();
        boolean granted = true;

        for (int i = 0; i < grantResults.length; i++) {

            if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                granted = false;
                permissionDenied.add(permissions[i]);
            }
        }

        if (permissionResult != null) {
            if (granted) {
                permissionResult.permissionGranted();
            } else {
                for (String s : permissionDenied) {
                    if (!shouldShowRequestPermissionRationale(s)) {
                        permissionResult.permissionForeverDenied();
                        return;
                    }
                }
                permissionResult.permissionDenied();
            }
        }
    }

    public void askCompactPermission(String permission, PermissionResult permissionResult) {
        KEY_PERMISSION = 200;
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);
    }

    public void askCompactPermissions(String permissions[], PermissionResult permissionResult) {
        KEY_PERMISSION = 200;
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    public void onStop() {
        mEventBus.unregister(this);
        dismissProgress();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this);
        }
        dismissProgress();
        super.onDestroy();
    }

    public void showSnackBar(View layout, String msg) {
        Snackbar.make(layout, msg, Snackbar.LENGTH_LONG).show();
    }
}
