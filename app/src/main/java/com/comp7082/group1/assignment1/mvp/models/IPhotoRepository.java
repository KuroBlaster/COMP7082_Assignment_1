package com.comp7082.group1.assignment1.mvp.models;

import java.util.ArrayList;
import java.util.Date;

public interface IPhotoRepository {
    public ArrayList<Photo> findPhotos(Date startTimestamp, Date endTimestamp, String keywords, String searchLat, String searchLng);
    public  Photo create();
}
