package com.demo.retrofit.utils.image;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.demo.retrofit.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Provides functions for scaling, saving and accessing images.
 */
public class ImageUtil {

    private static final String LOGTAG = "ImageUtil";

    /**
     * The format for a timestamp in the form "yyyyMMdd_HHmmss". Used for creating image file
     * names.
     */
    public static final SimpleDateFormat TIMESTAMP_FORMAT =
            new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);

    /**
     * The prefix for creating image file names.
     */
    public static final String IMG_PREFIX = "img_";

    /**
     * The prefix for creating temporary image file names.
     */
    public static final String TMP_IMG_PREFIX = "tmpimg_";

    /**
     * Folder name of the ad images which are stored the old way. Kept for deleting cached images
     * for existing ads.
     */
    private static final String IMAGES_PATH = "imgs";

    private Context appCtx;
    private int targetImageWidth;
    private int targetImageHeight;

    /**
     * Creates a new ImageUtil.
     *
     * @param context           The Android context.
     * @param targetImageWidth  The minimum image width.
     * @param targetImageHeight The minimum image height.
     */
    public ImageUtil(Context context, int targetImageWidth, int targetImageHeight) {
        this.appCtx = context.getApplicationContext();
        this.targetImageWidth = targetImageWidth;
        this.targetImageHeight = targetImageHeight;
    }

    /**
     * Creates a new ImageUtil. Sets the default minimum image width and height used to downsize
     * images.
     */
    public ImageUtil(Context appCtx) {
        this.appCtx = appCtx;
        this.targetImageWidth = 600;
        this.targetImageHeight = 200;
    }

    /**
     * Opens a stream on to the content of the passed uri and validates the picture dimensions.
     *
     * @param imgUri The image uri.
     * @throws FileNotFoundException Thrown if the file with the passed uri does not exist.
     */
    public boolean isPictureValidForUpload(final Uri imgUri) throws FileNotFoundException {
        BitmapFactory.Options opts = getImageDimensions(imgUri);
        int orientation = getOrientation(imgUri);
        return isPictureDimensionsValid(opts.outWidth, opts.outHeight, orientation);
    }

    /**
     * Does the same as {@link #isPictureValidForUpload(Uri)} but takes a file path instead of an
     * Uri.
     *
     * @param filePath The path to the image.
     * @throws IOException Thrown if the file cannot be found.
     */
    public boolean isPictureValidForUpload(final String filePath) throws IOException {
        BitmapFactory.Options opts = getImageDimensions(filePath);
        ExifInterface exif = new ExifInterface(filePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        // Translate the orientation constant to degrees.
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                orientation = 90;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                orientation = 270;
                break;

            default:
                break;
        }

        return isPictureDimensionsValid(opts.outWidth, opts.outHeight, orientation);
    }

    /**
     * Check if the passed picture has valid size and aspect ratio. Minimum width is 320 px, minimum
     * height 107 px. The aspect ratio must be between 3:1 and 1:3.
     *
     * @param width       The width of the image.
     * @param height      The height of the image.
     * @param orientation The orientation of the image.
     * @return True if the dimensions are valid, false otherwise.
     */
    private boolean isPictureDimensionsValid(double width, double height, int orientation) {
        if (orientation == 90 || orientation == 270) {
            // The image will be rotated later and width and height will be swapped.
            double temp = width;
            //noinspection SuspiciousNameCombination
            width = height;
            height = temp;
        }

        if (width < 320 || height < 107) {
            return false;
        }

        double aspect = width / height;
        return !(aspect > 3 || aspect < (1 / 3));
    }

    /**
     * @param imgUri The image uri.
     * @return The dimensions of the passed image wrapped in the BitmapFactory.Options object.
     * @throws FileNotFoundException Thrown if the file with the passed uri does not exist.
     */
    private BitmapFactory.Options getImageDimensions(Uri imgUri) throws FileNotFoundException {
        InputStream is = appCtx.getContentResolver().openInputStream(imgUri);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                // Do not care.
            }
        }
        return opts;
    }

    /**
     * Does the same as {@link #getImageDimensions(Uri)} but takes a file path instead of an Uri.
     *
     * @param filePath The file path of the image.
     */
    private BitmapFactory.Options getImageDimensions(String filePath) throws FileNotFoundException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        return opts;
    }

    /**
     * Create a File for saving an image.
     */
    public File getOutputMediaFile() {
        if (!isExternalStorageWritable()) {
            return null;
        }

        File mediaStorageDir = getMediaStorageDirectory();

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(LOGTAG, "Failed to create directory " + appCtx.getString(R.string.app_name));
                return null;
            }
        }

        String timeStamp = TIMESTAMP_FORMAT.format(new Date());
        String fileName = IMG_PREFIX + timeStamp + ".jpg";
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public void deleteLocalImages() {
        File mediaStorageDir = getMediaStorageDirectory();
        if (mediaStorageDir.exists()) {
            deleteRecursive(mediaStorageDir);
        }

        if (appCtx.getFilesDir().exists()) {
            deleteRecursive(mediaStorageDir);
        }

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private File getMediaStorageDirectory() {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appCtx.getString(R.string.app_name));
    }

    /**
     * Saves the passed bitmap in the app internal folder using the passed file name.
     *
     * @param bitmap   The bitmap to save.
     * @param fileName The file name for the bitmap.
     * @return The path to the saved bitmap.
     * @throws IOException Thrown if writing to a stream failed.
     */
    private String saveBitmap(Bitmap bitmap,
                              String fileName,
                              Bitmap.CompressFormat compressFormat,
                              int quality) throws IOException {
        ByteArrayOutputStream bitmapOut = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, bitmapOut);
        OutputStream fileOut = appCtx.openFileOutput(fileName, Context.MODE_PRIVATE);
        bitmapOut.writeTo(fileOut);
        bitmapOut.close();
        fileOut.close();
        return new File(appCtx.getFilesDir(), fileName).getPath();
    }

    /**
     * Rotates the image to the right orientation if needed. Then saves it. This is needed for
     * images chosen via the gallery app on some devices.
     *
     * @param orientation The current orientation of the image.
     * @param source      The image to rotate.
     * @throws IOException Thrown if writing to a stream failed.
     */
    private Bitmap fixImageRotation(int orientation, Bitmap source) throws IOException {
        switch (orientation) {
            case 90:
                return rotateImage(source, 90);

            case 180:
                return rotateImage(source, 180);

            case 270:
                return rotateImage(source, 270);

            default:
                return source;
        }
    }

    /**
     * @return The orientation of the image with the passed uri.
     */
    public int getOrientation(Uri uri) {
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = appCtx.getContentResolver().query(uri, columns, null, null, null);
        int orientation = 0;
        if (cursor != null && cursor.moveToFirst()) {
            int colIndex = cursor.getColumnIndex(columns[0]);
            if (colIndex != -1) {
                orientation = cursor.getInt(colIndex);
            }
            cursor.close();
        }
        return orientation;
    }


    /**
     * Rotates the passed bitmap using the passed angle.
     *
     * @param source The image to rotate.
     * @param angle  The angle to use for the rotation.
     * @throws IOException Thrown if writing to a stream failed.
     */
    private Bitmap rotateImage(Bitmap source, float angle) throws IOException {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Returns a downscaled and compressed version of the bitmap at the passed file path.
     *
     * @param filePath The path to the bitmap which is to be optimized.
     * @return The downscaled bitmap in form of a byte array stream.
     */
    public ByteArrayOutputStream getOptimizedImageForUpload(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                Bitmap bitmap = getDownscaledBitmap(uri, false);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
                return os;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Deletes the temporary image files which were created when the user chose a picture from the
     * Storage Access Framework on the PictureSourceActivity.
     */
    public void deleteTempImages() {
        String[] imageFileNames = appCtx.getFilesDir().list();

        if (imageFileNames != null && imageFileNames.length > 0) {
            for (String fileName : imageFileNames) {
                // Check if image is temp. If it is older than 3 hours, delete it.
                if (fileName.startsWith(ImageUtil.TMP_IMG_PREFIX)) {
                    int start = fileName.indexOf("_") + 1;
                    int end = fileName.indexOf(".");
                    String timeStamp = fileName.substring(start, end);

                    if (isOlderThanThreeHours(timeStamp)) {
                        if (appCtx.deleteFile(fileName)) {
                            Log.d(LOGTAG, "Deleted temp image: " + fileName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Parses the passed time stamp and checks if it is more than three hours in the past.
     *
     * @param timeStamp The time stamp to check.
     * @return True if the time stamp is more then three hours in the past, false otherwise.
     */
    private boolean isOlderThanThreeHours(String timeStamp) {
        try {
            Date date = ImageUtil.TIMESTAMP_FORMAT.parse(timeStamp);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -3);
            Date threeHoursBefore = calendar.getTime();
            return date.before(threeHoursBefore);
        } catch (ParseException e) {
            return false;
        }
    }

    private void deleteCachedImageIfExists(String imageFolderPath, String pic) {
        if (pic != null && pic.startsWith("file://" + imageFolderPath)) {
            // image was taken only for this ad -> can be deleted
            File f = new File(pic.substring("file://".length()));
            if (f.exists() && f.isFile()) {
                //noinspection ResultOfMethodCallIgnored
                f.delete();
            }
        }
    }

    /**
     * Checks if external storage is available for read and write.
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return MEDIA_MOUNTED.equals(state);
    }

    /**
     * Downscales the image and stores the downscaled version in a new file.
     *
     * @param uri            The image uri.
     * @param compressFormat E.g. Bitmap.CompressFormat.JPEG.
     * @param quality        The compress percentage, e.g. 85.
     * @param fixRotation    True if the rotation of the image should be fixed.
     * @return The path to the downscaled image.
     * @throws IOException If the image could not be found.
     */
    public String createDownscaledImage(Uri uri, Bitmap.CompressFormat compressFormat, int quality,
                                        boolean fixRotation) throws IOException {
        String timeStamp = TIMESTAMP_FORMAT.format(new Date());
        String fileName = TMP_IMG_PREFIX + timeStamp + ".jpg";
        Bitmap bitmap = getDownscaledBitmap(uri, fixRotation);
        return saveBitmap(bitmap, fileName, compressFormat, quality);
    }

    /**
     * Downscales the image to fit in a box with width and height which were used to initialize this
     * ImageUtil.
     *
     * @param uri         The image uri.
     * @param fixRotation True if the rotation of the image should be fixed.
     * @return A downscaled bitmap.
     * @throws IOException If the image could not be found.
     */
    public Bitmap getDownscaledBitmap(Uri uri, boolean fixRotation) throws IOException {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = getImageDimensions(uri);
        if (opts.outHeight > targetImageHeight || opts.outWidth > targetImageWidth) {
            int scale = Math.max(opts.outHeight / targetImageHeight,
                    opts.outWidth / targetImageWidth);
            try {
                bitmap = Glide.with(appCtx)
                        .load(uri)
                        .asBitmap()
                        .into(opts.outWidth / scale, opts.outHeight / scale)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bitmap = Glide.with(appCtx)
                        .load(uri)
                        .asBitmap()
                        .into(opts.outWidth, opts.outHeight)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (fixRotation) {
            int orientation = getOrientation(uri);
            if (orientation > 0) {
                bitmap = fixImageRotation(orientation, bitmap);
            }
        }
        return bitmap;
    }


    /**
     * Downscales the image and stores the downscaled version in a new file.
     *
     * @param uri            The image uri.
     * @param compressFormat E.g. Bitmap.CompressFormat.JPEG.
     * @param quality        The compress percentage, e.g. 85.
     * @param fixRotation    True if the rotation of the image should be fixed.
     * @return The path to the downscaled image.
     * @throws IOException If the image could not be found.
     */
    public String createImageWithDimension(Uri uri,
                                           Bitmap.CompressFormat compressFormat,
                                           int quality,
                                           boolean fixRotation,
                                           int targetImageWidth,
                                           int targetImageHeight) throws IOException {
        String timeStamp = TIMESTAMP_FORMAT.format(new Date());
        String fileName = TMP_IMG_PREFIX + timeStamp + ".jpg";
        Bitmap bitmap = getBitmapWithDimension(uri, fixRotation, targetImageWidth,
                targetImageHeight);
        return saveBitmap(bitmap, fileName, compressFormat, quality);
    }

    /**
     * Downscales the image to fit in a box with width and height which were used to initialize this
     * ImageUtil.
     *
     * @param uri         The image uri.
     * @param fixRotation True if the rotation of the image should be fixed.
     * @return A downscaled bitmap.
     * @throws IOException If the image could not be found.
     */
    public Bitmap getBitmapWithDimension(Uri uri,
                                         boolean fixRotation,
                                         int targetImageWidth,
                                         int targetImageHeight) throws IOException {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = getImageDimensions(uri);
        if (opts.outHeight > targetImageHeight || opts.outWidth > targetImageWidth) {
            int scale = Math.max(opts.outHeight / targetImageHeight,
                    opts.outWidth / targetImageWidth);
            try {
                bitmap = Glide.with(appCtx)
                        .load(uri)
                        .asBitmap()
                        .into(opts.outWidth / scale, opts.outHeight / scale)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bitmap = Glide.with(appCtx)
                        .load(uri)
                        .asBitmap()
                        .into(opts.outWidth, opts.outHeight)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (fixRotation) {
            int orientation = getOrientation(uri);
            if (orientation > 0) {
                bitmap = fixImageRotation(orientation, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access Framework
     * Documents, as well as the _data field for the MediaStore and other file-based
     * ContentProviders.
     *
     * @param uri The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(appCtx, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(appCtx, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(appCtx, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(appCtx, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
     * file-based ContentProviders.
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
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
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
}
