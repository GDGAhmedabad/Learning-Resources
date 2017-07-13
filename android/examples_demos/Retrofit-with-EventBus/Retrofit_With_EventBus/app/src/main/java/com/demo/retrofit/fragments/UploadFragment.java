package com.demo.retrofit.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.retrofit.BuildConfig;
import com.demo.retrofit.R;
import com.demo.retrofit.activities.BaseFragment;
import com.demo.retrofit.network.event.ApiErrorEvent;
import com.demo.retrofit.network.event.ApiErrorWithMessageEvent;
import com.demo.retrofit.network.response.AbstractApiResponse;
import com.demo.retrofit.utils.PermissionResult;
import com.demo.retrofit.utils.PermissionUtils;
import com.demo.retrofit.utils.image.CreateTempImagesFinishedEvent;
import com.demo.retrofit.utils.image.CreateTempImagesTask;
import com.demo.retrofit.utils.image.ImageUtil;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends BaseFragment {

    private static final String LOG = "UploadFragment";
    private static final String UPLOAD_FILE_REQUEST_TAG = LOG + ".uploadFileRequest";

    private static final int CAMERA_PIC_CODE = 1002;
    private static final int GALLERY_PIC_CODE = 1003;

    @BindView(R.id.imgUpload)
    ImageView imgUpload;
    @BindView(R.id.btnUpload)
    Button btnUpload;

    Unbinder unbinder;

    private String selectedImagePath;

    private int minImageWidth = 256;
    private int minImageHeight = 256;

    private String chooseFromGallery = "Gallery";
    private String chooseFromCamera = "Camera";
    private String chooseImageTitle = "Select Image From";
    private String cameraPicFilePath;
    protected ImageUtil imageHelper;

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance() {
        return new UploadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageHelper = new ImageUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog(getActivity());
    }

    @OnClick({R.id.imgUpload, R.id.btnUpload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgUpload:
                showImageChooserDialog();
                break;
            case R.id.btnUpload:
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        if (TextUtils.isEmpty(selectedImagePath)) {
            showToast(getString(R.string.str_plz_select_img));
        } else {
            File file = new File(selectedImagePath);
            if (!file.exists()) {
                showToast(getString(R.string.str_plz_select_img));
                return;
            }

            if (!mApiClient.isRequestRunning(UPLOAD_FILE_REQUEST_TAG)) {
                showProgress();
                mApiClient.uploadImage(UPLOAD_FILE_REQUEST_TAG, selectedImagePath);
            }

        }
    }

    protected void showImageChooserDialog() {
        AlertDialog.Builder chooseImageDialog = new AlertDialog.Builder(getActivity());
        chooseImageDialog.setTitle(chooseImageTitle);
        chooseImageDialog.setPositiveButton(chooseFromCamera,
                (dialogInterface, i) -> startCamera());
        chooseImageDialog.setNegativeButton(chooseFromGallery,
                (dialogInterface, i) -> openGallery());
        chooseImageDialog.show();
    }

    private void startCamera() {
        if (updatePermissions()) {
            File file = imageHelper.getOutputMediaFile();

            if (file == null) {
                showToast(getString(R.string.str_file_not_found));
                return;
            }

            cameraPicFilePath = file.getPath();
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_PIC_CODE);
        }
    }

    private void openGallery() {
        Intent getContentIntent = new Intent(Intent.ACTION_PICK);
        getContentIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getContentIntent.addCategory(Intent.CATEGORY_OPENABLE)
                .setAction(Intent.ACTION_GET_CONTENT)
                .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                .setType("image/*");
        startActivityForResult(getContentIntent, GALLERY_PIC_CODE);
    }

    private void handleCameraPicResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            try {
                boolean isValid = imageHelper.isPictureValidForUpload(cameraPicFilePath);

                if (isValid) {
                    // Send a broadcast to the MediaScanner to make the file show up in the gallery.
                    Uri uri = Uri.fromFile(new File(cameraPicFilePath));
                    getActivity().sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    addImages(uri);
                } else {
                    showToast(getString(R.string.error_invalid_image));
                }

            } catch (IOException e) {
                showToast(getString(R.string.error_image_processing));
            }

        } else if (resultCode != RESULT_CANCELED) {
            showToast(getString(R.string.error_unknown));
        }
    }


    private void handleGalleryPicResult(int resultCode, Intent resultData) {
        if (resultCode == RESULT_OK && resultData != null) {

            showProgress();
            if (resultData.getData() != null) {
                addImages(resultData.getData());
            } else {
                if (resultData.getClipData() != null &&
                        resultData.getClipData().getItemCount() > 0) {
                    for (int i = 0; i < resultData.getClipData().getItemCount(); i++) {
                        addImages(resultData.getClipData().getItemAt(i).getUri());
                    }
                }
            }
        } else if (resultCode != RESULT_CANCELED) {
            showToast(getString(R.string.error_unknown));
        }
    }

    private void addImages(Uri uri) {
        try {
            boolean validateImages = imageHelper.isPictureValidForUpload(uri);
            CreateTempImagesFinishedEvent event = new CreateTempImagesFinishedEvent();
            List<Uri> uris = new ArrayList<>(1);
            uris.add(uri);

            CreateTempImagesTask createTempImagesTask = new CreateTempImagesTask(
                    getActivity(), uris, event, validateImages, minImageWidth, minImageHeight);

            createTempImagesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            dismissProgress();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_PIC_CODE:
                    handleCameraPicResult(resultCode);
                    break;
                case GALLERY_PIC_CODE:
                    handleGalleryPicResult(resultCode, data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private boolean updatePermissions() {

        boolean isGranted = isPermissionsGranted(
                getActivity(), new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                        PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
                        PermissionUtils.Manifest_CAMERA});
        if (!isGranted) {
            askCompactPermissions(new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                            PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
                            PermissionUtils.Manifest_CAMERA},
                    new PermissionResult() {
                        @Override
                        public void permissionGranted() {
                            startCamera();
                        }

                        @Override
                        public void permissionDenied() {
                            showSnackBar(imgUpload.getRootView(),
                                    getString(R.string.str_allow_permission_for_image));
                        }

                        @Override
                        public void permissionForeverDenied() {
                            showSnackBar(imgUpload.getRootView(),
                                    getString(R.string.str_allow_permission_from_setting));
                        }
                    });
        }
        return isGranted;
    }

    // ============================================================================================
    // EventBus callbacks
    // ============================================================================================

    @Subscribe
    public void onEventMainThread(CreateTempImagesFinishedEvent event) {
        dismissProgress();
        selectedImagePath = event.bitmapPaths.get(0);
        Glide.with(getActivity())
                .load(selectedImagePath)
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imgUpload);

    }

    /**
     * Response of Uploaded File
     *
     * @param apiResponse UploadFileResponse
     */
    @Subscribe
    public void onEventMainThread(AbstractApiResponse apiResponse) {
        switch (apiResponse.getRequestTag()) {
            case UPLOAD_FILE_REQUEST_TAG:
                dismissProgress();
                showToast(apiResponse.getMessage());

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
            case UPLOAD_FILE_REQUEST_TAG:
                dismissProgress();
                showToast(getString(R.string.error_server_problem));
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
            case UPLOAD_FILE_REQUEST_TAG:
                dismissProgress();
                showToast(event.getResultMsgUser());
                break;

            default:
                break;
        }
    }

}
