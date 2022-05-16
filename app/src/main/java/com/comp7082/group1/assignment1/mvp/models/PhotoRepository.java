package com.comp7082.group1.assignment1.mvp.models;

import android.content.Context;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoRepository implements IPhotoRepository {
    private Context context;
    public PhotoRepository(Context context) {  }

    @Override
    public ArrayList<Photo> findPhotos(Date startTimestamp, Date endTimestamp, String keywords, String searchLat, String searchLng) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.comp7082.group1.assignment1/files/Pictures");
        ArrayList<Photo> photos = new ArrayList<Photo>();
        File[] fList = file.listFiles();

        try {
            if (fList != null) {
                for (File f : fList) {
                    if (searchLat.length() != 0 && searchLng.length() != 0) {
//                        Parse the GPS string
                        ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                        Matcher matcherLat = Pattern.compile("\\d+").matcher(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                        Matcher matcherLng = Pattern.compile("\\d+").matcher(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
                        matcherLng.find();
                        matcherLat.find();
                        int imgLat = Integer.valueOf(matcherLat.group());
                        int imgLng = Integer.valueOf(matcherLng.group());
                        if (imgLat == Integer.valueOf(searchLat) && imgLng == Integer.valueOf(searchLng)) {
                            photos.add(new Photo(f)
                            );
                        }
                    } else {
                        if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                                && f.lastModified() <= endTimestamp.getTime())
                        ) && (keywords == "" || f.getPath().contains(keywords))) {
                            Photo p;
                            p = new Photo(f);
                            photos.add(p);
                        }
                    }
                }
            }

        } catch (Exception ex) {
//            Crashes after photo taken if no catch
        }
        return photos;
    }
    @Override
    public  Photo create()  {return null;}
}
