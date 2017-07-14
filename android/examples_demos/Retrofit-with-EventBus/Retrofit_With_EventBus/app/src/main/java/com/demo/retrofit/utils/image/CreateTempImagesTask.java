package com.demo.retrofit.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTask for creating temporary image files from uris.
 */
public class CreateTempImagesTask extends AsyncTask<Void, Void, Void> {
    private Context appCtx;
    private List<Uri> uris;
    private CreateTempImagesFinishedEvent event;
    private boolean validateImages;
    private int minImageWidth;
    private int minImageHeight;

    private static final int MIN_IMAGE_PX = 800;

    public CreateTempImagesTask(Context ctx, List<Uri> uris,
                                CreateTempImagesFinishedEvent event,
                                boolean validateImages, int minImageWidth, int minImageHeight) {
        this.appCtx = ctx.getApplicationContext();
        this.uris = uris;
        this.event = event;
        this.validateImages = validateImages;
        this.minImageWidth = minImageWidth;
        this.minImageHeight = minImageHeight;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ImageUtil imageHelper = new ImageUtil(appCtx, minImageWidth, minImageHeight);
        event.bitmapPaths = new ArrayList<>(uris.size());
        event.originalFilePaths = new ArrayList<>(uris.size());

        try {
            for (Uri uri : uris) {
                if (!validateImages || imageHelper.isPictureValidForUpload(uri)) {
                    event.bitmapPaths.add(imageHelper
                            .createDownscaledImage(uri,
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    true));
                    event.originalFilePaths.add(
                            imageHelper.createImageWithDimension(uri,
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    true,
                                    MIN_IMAGE_PX,
                                    MIN_IMAGE_PX));
                }
            }
        } catch (IOException e) {
            event.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        appCtx = null;
        EventBus.getDefault().post(event);
    }
}
