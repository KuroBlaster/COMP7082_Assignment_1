package com.comp7082.group1.assignment1.mvp.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Photo {
    private File photoFile;
    private String photoPath;

    public Photo(File photoFile) {
        this.photoFile = photoFile;
        photoPath = photoFile.getAbsolutePath();
    }
    public File getPhotoFile() { return photoFile;}

    public String getTimestamp() {
        /* Below are just for reference:
        "Date: " = attr[1], " Time: "= attr[2], "Description:" = attr[3]);
         */
        String[] attr = photoPath.split("_");
        return attr[1] + attr[2];
    }
    public String getDate() {
        String[] attr = photoPath.split("_");
        return attr[1];
    }
    public String getTime() {
        String[] attr = photoPath.split("_");
        return attr[2];
    }
    public String getCaption() {
        photoPath = photoFile.getAbsolutePath();
        String[] attr = photoPath.split("_");
        return attr[3];
    }
    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(photoPath);
    }

    public void updateCaption (String caption) {
        photoPath = photoFile.getAbsolutePath();
        String[] attr = photoPath.split("_");
        if (attr.length >= 4) {
            File to = new File(attr[0] + "_" + attr[1] + "_" + attr[2] + "_" + caption + "_" + attr[4]);
            File from = new File(photoPath);
            from.renameTo(to);
        }
    }
}

