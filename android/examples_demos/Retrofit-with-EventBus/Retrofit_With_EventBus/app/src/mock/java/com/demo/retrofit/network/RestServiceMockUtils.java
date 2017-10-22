package com.demo.retrofit.network;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RestServiceMockUtils {
    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(Context context, String filePath) throws IOException, Resources.NotFoundException {

        final InputStream stream = context.getResources()
                .openRawResource(context.getResources().getIdentifier(filePath,//"FILENAME_WITHOUT_EXTENSION",
                        "raw",
                        context.getPackageName()));

        String ret = convertStreamToString(stream);
        //Make sure you close all streams.
        stream.close();
        return ret;
    }

}
