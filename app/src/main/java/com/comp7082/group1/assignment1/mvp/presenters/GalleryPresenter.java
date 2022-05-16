package com.comp7082.group1.assignment1.mvp.presenters;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.comp7082.group1.assignment1.mvp.models.Photo;
import com.comp7082.group1.assignment1.mvp.models.PhotoRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GalleryPresenter {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    private Activity context;
    private PhotoRepository repository;
    private ArrayList<Photo> photos = null;
    private int index = 0;

    private FusedLocationProviderClient fusedLocationClient;
    //..
    //..
    public GalleryPresenter(Activity context) {
        repository = new PhotoRepository(context);
        this.context = context;
        index = 0;
        photos = repository.findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }
    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(context);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            Uri photoURI = FileProvider.getUriForFile(context, "com.comp7082.group1.assignment1", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            context.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void search() {  }

    public void onReturn(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == context.RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
                Date startTimestamp, endTimestamp;
                String searchLat = null, searchLng = null;
                try {
                    String from = (String) data.getStringExtra("STARTTIMESTAMP");
                    String to = (String) data.getStringExtra("ENDTIMESTAMP");
                    startTimestamp = format.parse(from);
                    endTimestamp = format.parse(to);
                    searchLat = (String) data.getStringExtra("LAT");
                    searchLng = (String) data.getStringExtra("LNG");
                } catch (Exception ex) {
                    startTimestamp = null;
                    endTimestamp = null;
                    searchLat = "";
                    searchLng = "";
                }
                String keywords = (String) data.getStringExtra("KEYWORDS");

                index = 0;
                photos = repository.findPhotos(startTimestamp, endTimestamp, keywords, searchLat, searchLng);
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == context.RESULT_OK) {
            index = photos.size();
            photos = repository.findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");
        }

        // Deletes auto-generated image file if photo was not taken
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == context.RESULT_CANCELED) {
            //todo: check later as I am not sure if this is needed in the new MVP design
            File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), "/Android/data/com.comp7082.group1.assignment1/files/Pictures");
            File[] fList = file.listFiles();
            fList[fList.length - 1].delete();
        }
    }

    private File createImageFile(Context context) throws IOException {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_Description" + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void handleNavigationInput(String navigationAction, String caption)  {
        photos.get(index).updateCaption(caption);
        photos = repository.findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");
        if (navigationAction.equalsIgnoreCase("next") && !isLast()) {
            index++;
        } else if (navigationAction.equalsIgnoreCase("prev") && !isFirst()) {
            index--;
        }
    }

    public Bitmap getPhotoBitmap() {
        return photos.get(index).getBitmap();
    }

    public String getCaption() {
        return photos.get(index).getCaption();
    }

    public String getTimestamp() {
        return photos.get(index).getTimestamp();
    }

    public String getDate() {
        return photos.get(index).getDate();
    }

    public String getTime() {
        return photos.get(index).getTime();
    }
    public boolean isFirst() {
        return (index <= 0) ? true : false;
    }

    public boolean isLast() {
        return (index == photos.size() - 1) ? true : false;
    }

    public boolean isEmpty() {
        return photos.isEmpty();
    }
    /* todo implement the location here in presenter
    public void getImageLocation(View view) {
        try {
            ExifInterface exif = new ExifInterface(photos.get(index));
            Matcher matcherLat = Pattern.compile("\\d+").matcher(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            Matcher matcherLng = Pattern.compile("\\d+").matcher(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            matcherLat.find();
            matcherLng.find();
            int lat = Integer.valueOf(matcherLat.group());
            int lng = Integer.valueOf(matcherLng.group());
            Toast.makeText(MainActivity.this, "LAT: " + lat + "\nLNG: " + lng, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setLatLng(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Need to enable LOCATION ACCESS first in settings", Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        try {
//                            Parse latitude to GPS format (Degree / min / sec / N or S)
                            ExifInterface exif = new ExifInterface(photos.get(index));
                            double lat = location.getLatitude();
                            double alat = Math.abs(lat);
                            String dms = Location.convert(alat, Location.FORMAT_SECONDS);
                            String[] splits = dms.split(":");
                            String[] secnds = (splits[2]).split("\\.");
                            String seconds;
                            if (secnds.length == 0) {
                                seconds = splits[2];
                            } else {
                                seconds = secnds[0];
                            }
                            String latitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat > 0 ? "N" : "S");
//                            Parse longtitude to GPS format (Degree / min / sec / E or W)
                            double lon = location.getLongitude();
                            double alon = Math.abs(lon);
                            String dms2 = Location.convert(alon, Location.FORMAT_SECONDS);
                            splits = dms2.split(":");
                            secnds = (splits[2]).split("\\.");
                            if (secnds.length == 0) {
                                seconds = splits[2];
                            } else {
                                seconds = secnds[0];
                            }
                            String longitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr);
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon > 0 ? "E" : "W");

                            exif.saveAttributes();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

*/

    public interface View {
        public void displayPhoto(Bitmap photo, String caption, String timestamp, boolean isFirst, boolean isLast);
    }
}

