package com.comp7082.group1.assignment1.mvp.presenters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.comp7082.group1.assignment1.mvp.models.Photo;
import com.comp7082.group1.assignment1.mvp.models.PhotoRepository;

import java.util.ArrayList;

public class GalleryPresenter {
    private Activity context;
    private PhotoRepository repository;
    private ArrayList<Photo> photos = null;
    //..
    //..
    public GalleryPresenter(Activity context) { }
    public void takePhoto() {   }
    public void search() {  }
    public void onReturn(int requestCode, int resultCode, Intent data) { }
    public void handleNavigationInput(String navigationAction, String caption)  { }
    public interface View {
        public void displayPhoto(Bitmap photo, String caption, String timestamp, boolean isFirst, boolean isLast);
    }
}

