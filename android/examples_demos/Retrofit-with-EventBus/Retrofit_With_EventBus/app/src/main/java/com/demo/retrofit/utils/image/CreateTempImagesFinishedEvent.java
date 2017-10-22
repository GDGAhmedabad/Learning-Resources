package com.demo.retrofit.utils.image;

import java.util.List;

/**
 * Created by deardhruv on 26/03/17.
 */
public class CreateTempImagesFinishedEvent {
    public List<String> bitmapPaths;
    public List<String> originalFilePaths;
    public Exception exception;
}
