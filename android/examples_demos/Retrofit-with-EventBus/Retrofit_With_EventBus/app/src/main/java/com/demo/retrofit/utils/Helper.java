package com.demo.retrofit.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.demo.retrofit.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Helper methods that are used all over the app.
 */
public final class Helper {
    private static final String LOGTAG = "Helper";

    public static final long LOCATION_TIMEOUT = 15 * 60 * 1000;

    static final String DATE_TEMPLATE = "EEE, d MMM yyyy HH:mm:ss Z";

    private static final long ONE_DAY = 86400L;
    public static final long FOUR_WEEKS = 28L * ONE_DAY * 1000L;

    private static final SimpleDateFormat FORMATTER1 = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public static final SimpleDateFormat CHAT_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat validationDateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat validationTimeFormat = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat validationDateTimeFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat FORMATTER2 =
            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    public static final SimpleDateFormat FORMATTER3 =
            new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static final SimpleDateFormat ZULU_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());

    public Helper() {
        // should not be instantiated
    }

    /**
     * Check for active internet connection
     *
     * @param ctx
     * @return
     */
    public static boolean isInternetActive(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();

        return info != null && info.isConnected() && info.isAvailable();
    }


    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }


    public static void closeCursor(Cursor c) {
        if (c != null && !c.isClosed()) {
            c.close();
        }
    }

    public static String changeDateFormat(DateFormat sourceFormat, SimpleDateFormat targetFormat,
                                          String dateStr) {
        Date date;
        try {
            date = sourceFormat.parse(dateStr);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getFormattedDate(long milliSeconds, SimpleDateFormat dateFormat) {
        // Create a calendar object that will convert the date and time value in milliseconds to
        // date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return dateFormat.format(calendar.getTime());
    }


    public static String getValidationDate(final String adDate) throws ParseException {
        return "" + getDaysBetweenDates(FORMATTER1.parse(adDate), new Date());
    }

    /**
     * @param date1 -past date
     * @param date2 -future date than date1
     * @return total days between Dates
     */
    private static int getDaysBetweenDates(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24L));
    }


    public static String getCurrentDate() {
        return FORMATTER1.format(new Date());
    }

    /**
     * Returns a semicolon separated list built from the passed list of String.
     *
     * @param list A list of strings.
     * @return A semicolon separated list in one String object or an empty string if the passed list
     * is null.
     */
    private static String createSemicolonSeparatedList(List<String> list) {
        if (list == null)
            return "";

        String semicolonSeparatedList = "";
        int i = 0;

        for (String item : list) {
            semicolonSeparatedList = semicolonSeparatedList.concat(item);
            if (list.size() > i + 1) {
                semicolonSeparatedList += ";";
            }
            i++;
        }

        return semicolonSeparatedList;
    }

    public static String getFilenameFromPath(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        int lastIndexOf = path.lastIndexOf("/");
        if (lastIndexOf == -1) {
            return path;
        }
        return path.substring(lastIndexOf + 1, path.length());
    }

    public static void showKeyBoard(Context context, View view) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(Context context, View view) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        InputMethodManager mgr = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isClickedOutsideOf(MotionEvent me, View v) {
        int[] location = {
                0, 0
        };
        //noinspection ResourceType
        v.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        if (me.getX() < x) {
            return true;
        } else if (me.getY() < y) {
            return true;
        } else if (me.getX() > x + v.getMeasuredWidth()) {
            return true;
        } else if (me.getY() > y + v.getMeasuredHeight()) {
            return true;
        }
        return false;
    }

    public static String getLocationTextForDetail(String country, String cityzip, String city) {
        return country + "-" + cityzip + " " + city;
    }

    private static boolean isOnline(Context ctx) {
        final ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * Check if roaming connection active.
     *
     * @return
     */
    private static boolean isConnectionRoaming(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isRoaming();

    }

    /**
     * Is Roaming enabled.
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    private static boolean isRoamingEnabled(Context ctx) {
        ContentResolver cr = ctx.getContentResolver();
        int result = 0; // 0 is false
        boolean check = false;

        try {
            result = Settings.Secure.getInt(cr, Settings.Secure.DATA_ROAMING);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (result == 1) {
            check = true;
        }

        return check;
    }

    /**
     * Check if network connection is allowed.
     *
     * @return
     */
    public static boolean isNetworkConnectionAllowed(Context ctx) {
        boolean result;
        result = isOnline(ctx) && (!isConnectionRoaming(ctx) || isRoamingEnabled(ctx));

        if (!result) {
            Log.w(LOGTAG, "no network connection allowed");
        }

        return result;
    }

    public static boolean isAppInstalled(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;

        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;
    }

    /**
     * Writes long strings to the logcat.
     *
     * @param str The string to write to the logcat.
     */
    public static void logLongStrings(String logTag, String str) {
        if (str.length() > 4000) {
            Log.d(logTag, str.substring(0, 4000));
            logLongStrings(logTag, str.substring(4000));
        } else {
            Log.d(logTag, str);
        }
    }

    public static StringBuffer getMd5Checksum(String fileContentBaseEncoded) {
        byte[] bytesOfMessage = fileContentBaseEncoded.getBytes();
        return getMd5Checksum(bytesOfMessage);
    }

    public static StringBuffer getMd5Checksum(byte[] bytesOfMessage) {
        byte[] md5sum;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md5sum = md.digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage());
        }

        StringBuffer md5checksum = new StringBuffer();

        for (byte aMd5sum : md5sum) {
            md5checksum.append(Integer.toString((aMd5sum & 0xff) + 0x100, 16).substring(1));
        }

        return md5checksum;
    }

    /**
     * Returns the current app version code.
     *
     * @param ctx The Android application context.
     * @return Application's version code from the {@link PackageManager}.
     */
    public static int getAppVersion(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    0);

            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @param ctx The Android application context.
     * @return Application name
     */
    public static String getAppName(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager
                    .getApplicationInfo(ctx.getApplicationInfo().packageName, 0);
        } catch (final NameNotFoundException ignored) {
        }
        return (String) (applicationInfo != null
                ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
    }

    /**
     * Tries to open the app store by using the passed storeAppUri. If this fails, opens the store
     * website.
     *
     * @param ctx             The Android context.
     * @param storeAppUri     The app store uri.
     * @param storeWebsiteUri The store website.
     */
    public static void openAppStore(Context ctx, Uri storeAppUri, Uri storeWebsiteUri) {
        Intent marketIntent;

        try {
            marketIntent = new Intent(Intent.ACTION_VIEW, storeAppUri);
            marketIntent.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            ctx.startActivity(marketIntent);

        } catch (android.content.ActivityNotFoundException anfe) {
            marketIntent = new Intent(Intent.ACTION_VIEW, storeWebsiteUri);
            marketIntent.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            ctx.startActivity(marketIntent);
        }
    }

    /**
     * Show the activity over the lockscreen and wake up the device. If you launched the app
     * manually both of these conditions are already true. If you deployed from the IDE, however,
     * this will save you from hundreds of power button presses and pattern swiping per day!
     */
    public static void riseAndShine(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        PowerManager power = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock lock = power.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wakeup!");

        lock.acquire(100000);
        lock.release();
    }


    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }


    public static SpannableStringBuilder trimSpannable(SpannableStringBuilder spannable) {
        checkNotNull(spannable);
        int trimStart = 0;
        int trimEnd = 0;

        String text = spannable.toString();

        while (text.length() > 0 && text.startsWith("\n")) {
            text = text.substring(1);
            trimStart += 1;
        }

        while (text.length() > 0 && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
            trimEnd += 1;
        }

        return spannable.delete(0, trimStart)
                .delete(spannable.length() - trimEnd, spannable.length());
    }


    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    /**
     * This method assumes endDate >= startDate
     **/
    public static long getDaysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }


    public static String getJSONData(Object object) {
        if (object != null) {
            Gson gson = new Gson();
            return gson.toJson(object);
        } else {
            return null;
        }
    }

    public static boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target.trim()).matches();
    }

    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
