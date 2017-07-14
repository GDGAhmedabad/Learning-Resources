package com.demo.retrofit.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.retrofit.R;
import com.demo.retrofit.RetroFitApp;
import com.demo.retrofit.network.ApiClient;
import com.demo.retrofit.network.event.ApiErrorEvent;
import com.demo.retrofit.utils.Helper;
import com.demo.retrofit.utils.PermissionResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This BaseActivity class is mainly used for avoiding most of the boiler-plate code.
 * <p/>
 * Created by deardhruv on 26/03/17.
 */
public class BaseActivity extends AppCompatActivity {

    private static final int SHOW_KEYBOARD_DELAY = 300;

    private int KEY_PERMISSION = 0;
    private PermissionResult permissionResult;
    private String permissionsAsk[];

    private EventBus mEventBus;
    protected ApiClient mApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mEventBus = EventBus.getDefault();
        mApiClient = getApp().getApiClient();

        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    public RetroFitApp getApp() {
        return (RetroFitApp) getApplication();
    }

    /**
     * Center the Toolbar Title.
     *
     * @param toolbar
     */
    protected void centerTitle(Toolbar toolbar) {
        final CharSequence originalTitle = toolbar.getTitle();
        TextView mTitle = toolbar.findViewById(R.id.toolbarTitle);
        mTitle.setText(originalTitle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
    }

    /**
     * Open KeyBoard
     *
     * @param v
     */
    protected void showKeyBoardOnView(View v) {
        v.requestFocus();
        Helper.showKeyBoard(this, v);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        v.requestFocus();
    }

    protected void showKeyBoardOnViewDelayed(final View v) {

        new Handler().postDelayed(() -> runOnUiThread(() -> showKeyBoardOnView(v)), SHOW_KEYBOARD_DELAY);
    }

    /**
     * Hide KeyBoard
     *
     * @param v
     */
    protected void hideKeyboard(View v) {
        Helper.hideKeyboard(this, v);
        v.clearFocus();
    }


    protected void focusOnView(final ScrollView scrollView, final View v) {
        new Handler().post(() -> scrollView.smoothScrollTo(0, v.getBottom()));
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this);
        }
        super.onDestroy();
    }

    /**
     * Shows toast message.
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
                (ActivityCompat.checkSelfPermission(context, permission) ==
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
            if (!(ActivityCompat.checkSelfPermission(
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
            if (!isPermissionGranted(this, aPermissionAsk)) {
                permissionsNotGranted.add(aPermissionAsk);
            }
        }

        if (permissionsNotGranted.isEmpty()) {

            if (permissionResult != null)
                permissionResult.permissionGranted();

        } else {

            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            ActivityCompat.requestPermissions(this,
                    arrayPermissionNotGranted, KEY_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

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
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, s)) {
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

    @Subscribe
    public void onEventMainThread(ApiErrorEvent apiErrorEvent) {

    }

}
