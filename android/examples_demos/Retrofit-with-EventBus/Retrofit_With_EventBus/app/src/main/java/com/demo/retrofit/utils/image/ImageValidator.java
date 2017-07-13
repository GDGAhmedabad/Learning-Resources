package com.demo.retrofit.utils.image;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageValidator {

    private static final String LOGTAG = "ImageValidator";

    /**
     * Check if the Picture is valid for uploading. Currently we received this
     * information:<br>
     * Minimale Bildbreite 320 Pixel.<br>
     * Minimale Bildhöhe 107 Pixel.<br>
     * Das Seitenverhältnis muss zwischen 3:1 und 1:3 liegen
     *
     * @param filepath
     * @return
     */

    public static boolean isPictureValidForUpload(final String filepath) {
        if (filepath == null) {
            throw new IllegalArgumentException("filepath cannot be null");
        }

        final Options opt = new Options();
        opt.inJustDecodeBounds = true;

        // if (filepath.startsWith("http://"))
        // // already uploaded images are considered valid
        // return true;

        if (filepath.startsWith("file://")) {
            BitmapFactory.decodeFile(filepath.replace("file://", ""), opt);
        } else {
            BitmapFactory.decodeFile(filepath, opt);
        }

        if (opt.outWidth < 320) {
            Log.e(LOGTAG, "width too small");
            return false;
        }

        if (opt.outHeight < 107) {
            Log.e(LOGTAG, "height too small");
            return false;
        }

        double aspect = opt.outWidth / opt.outHeight;
        if (aspect > 3 || aspect < (1 / 3)) {
            Log.e(LOGTAG, "aspect ratio wrong");
            return false;
        }

        return true;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // DocumentProvider
            if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String path = getDataColumn(context, uri, null, null);

            // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            // final int takeFlags = new Intent().getFlags()
            // & (Intent.FLAG_GRANT_READ_URI_PERMISSION
            // | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
            // context.getContentResolver().takePersistableUriPermission(uri,
            // takeFlags);
            // }

            if (TextUtils.isEmpty(path)) {
                path = uri.toString();
            }

            if (new File(path).exists())
                return path;

            Bitmap bitmap = null;

            if (uri.toString() != null
                    && (uri.toString().contains("docs.file") || isPicasaPhotoUri(uri))) {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap == null) {
                bitmap = BitmapFactory.decodeFile(path);
            }

            File dir = new File(Environment.getExternalStorageDirectory() + "//");

            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }

            String newPath = dir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";

            if (storeImage(bitmap, newPath)) {
                path = newPath;
            }

            return path;
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";// android.provider.MediaStore.Files.FileColumns.DATA
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri he Uri to check.
     * @return Whether the Uri authority is PicasaProvider.
     */
    public static boolean isPicasaPhotoUri(Uri uri) {
        return "com.sec.android.gallery3d.provider".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Save image to sdcard from Bitmap
     *
     * @param imageData
     * @param filePath
     * @return Whether saving Image is successful
     */
    public static boolean storeImage(Bitmap imageData, String filePath) {
        if (imageData == null) {
            return false;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            imageData.compress(CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            Log.e(LOGTAG, "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }

}
