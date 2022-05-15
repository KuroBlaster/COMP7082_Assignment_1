package com.comp7082.group1.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    String mCurrentPhotoPath;
    private ArrayList<String> photos = null;
    private int index = 0;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");

//        Initialize location service
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (photos.size() == 0) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }
    }

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
        } catch (IOException e) {
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

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void takePhoto(View v) throws IOException {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            Uri photoURI = FileProvider.getUriForFile(this, "com.comp7082.group1.assignment1", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    private ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords, String searchLat, String searchLng) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.comp7082.group1.assignment1/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
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
                            photos.add(f.getPath());
                        }
                    } else {
                        if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                                && f.lastModified() <= endTimestamp.getTime())
                        ) && (keywords == "" || f.getPath().contains(keywords)))
                            photos.add(f.getPath());
                    }
                }
            }

        } catch (Exception ex) {
//            Crashes after photo taken if no catch
        }
        return photos;
    }

    public void scrollPhotos(View v) {
        switch (v.getId()) {
            case R.id.btnPrev:
                if (index > 0) {
                    index--;
                    updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
                }
                break;
            case R.id.btnNext:
                if (index < (photos.size() - 1)) {
                    index++;
                    updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
                }
                break;
            default:
                break;
        }
        if (photos.size() == 0) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }
    }

    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        TextView tv = (TextView) findViewById(R.id.tvTimestamp);
        EditText et = (EditText) findViewById(R.id.etCaption);
        TextView latlong = (TextView) findViewById(R.id.latlong);

        if (path == null || path == "") {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("Description");
            tv.setText("Date and time");
            latlong.setText("LAT: LNG:");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            String[] attr = path.split("_");
            tv.setText("Date: " + attr[1] + " Time: " + attr[2]);
            et.setText(attr[3]);

           // try {
           //     ExifInterface exif = new ExifInterface(photos.get(index));
           //     Matcher matcherLat = Pattern.compile("\\d+").matcher(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
           //     Matcher matcherLng = Pattern.compile("\\d+").matcher(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
           //     matcherLat.find();
           //     matcherLng.find();//    int lat = Integer.valueOf(matcherLat.group());
           //     int lng = Integer.valueOf(matcherLng.group());
                latlong.setText("LAT: LNG: ");
                //Toast.makeText(MainActivity.this, "LAT: " + lat + "\nLNG: " + lng, Toast.LENGTH_LONG).show();
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        }
    }

    private File createImageFile() throws IOException {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_Description" + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void updatePhoto(String path, String caption) {
        String[] attr = path.split("_");
        if (attr.length >= 4) {
            File to = new File(attr[0] + "_" + attr[1] + "_" + attr[2] + "_" + caption + "_" + attr[4]);
            File from = new File(path);
            from.renameTo(to);
            photos.set(index, attr[0] + "_" + attr[1] + "_" + attr[2] + "_" + caption + "_" + attr[4]);
        }
    }

    public void launchSearchActivity(View view) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
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
                photos = findPhotos(startTimestamp, endTimestamp, keywords, searchLat, searchLng);
                if (photos.size() == 0) {
                    displayPhoto(null);
                } else {
                    displayPhoto(photos.get(index));
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            index = photos.size();
            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");

            Toast.makeText(MainActivity.this, "size: " + photos.size() + "\n index: " + index, Toast.LENGTH_LONG).show();
        }

        // Deletes auto-generated image file if photo was not taken
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), "/Android/data/com.comp7082.group1.assignment1/files/Pictures");
            File[] fList = file.listFiles();
            fList[fList.length - 1].delete();
        }
    }

    public void shareFile(View v) {
        if (photos.size() == 0) {
            Context context = getApplicationContext();
            CharSequence text = "There is no photo to share. Please take a photo before sharing.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        Uri photoUri = FileProvider.getUriForFile(this, "com.comp7082.group1.assignment1", new File(photos.get(index)));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(shareIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            intentList.add(new LabeledIntent(shareIntent, packageName, ri.loadLabel(pm), ri.icon));
        }
        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
        shareIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent, null));
    }
}