package com.comp7082.group1.assignment1.mvp.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

public class PhotoRepository implements IPhotoRepository {
    private Context context;
    public PhotoRepository(Context context) {  }
    @Override
    public ArrayList<Photo> findPhotos(Date startTimestamp, Date endTimestamp, String keywords) { return null;}
    @Override
    public  Photo create()  {return null;}
}
