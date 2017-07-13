package com.demo.retrofit.network.response;

import com.demo.retrofit.network.response.submodel.ImageResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by deardhruv on 26/03/17.
 */
public class ImageListResponse extends AbstractApiResponse {

    @SerializedName("image_list")
    @Expose
    private List<ImageResult> imageResults = new ArrayList<>();

    public List<ImageResult> getImageResultList() {
        return imageResults;
    }

    public void setImageResultList(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
    }
}
