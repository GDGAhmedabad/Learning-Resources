package com.demo.retrofit.utils.image;

/**
 * Utility class for handling thumbnail images.
 */
public class ThumbnailUtil {

    /**
     * @return The url pointing to the thumbnail image with the right size for this screen.
     */
    public static String getThumbUrlForScreenSize(String url, int targetWidth) {
        int tIndex = url.lastIndexOf("t");

        if (tIndex > -1) {
            char shorthand = getThumbnailShorthand(targetWidth);
            StringBuilder urlBuilder = new StringBuilder(url);
            urlBuilder.setCharAt(tIndex, shorthand);
            return urlBuilder.toString();
        }

        return url;
    }

    /**
     * @return The shorthand for the thumbnail image with the right width for this screen.
     */
    private static char getThumbnailShorthand(int targetWidth) {
        int[] thumbnailWidths = new int[]{90, 180, 270, 425};
        char[] thumbnailShorthands = new char[]{'t', 'r', 's', 'm'};

        for (int i = 0; i < thumbnailWidths.length; i++) {
            if (thumbnailWidths[i] >= targetWidth) {
                return thumbnailShorthands[i];
            }
        }

        // None of the thumbnails is large enough, so return the largest possible.
        return thumbnailShorthands[thumbnailShorthands.length - 1];
    }
}
