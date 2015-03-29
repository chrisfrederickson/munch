package com.felkertech.n.munch.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.felkertech.n.munch.Photography.CameraPreview;
import com.felkertech.n.munch.Photography.FroyoAlbumDirFactory;
import com.felkertech.n.munch.R;
import com.felkertech.n.munch.Photography.AlbumStorageDirFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraPro extends Activity {

    private Camera mCamera;
    private SurfaceView mPreview;
    private MediaRecorder mMediaRecorder;
    public final static String TAG = "munch::CameraPro";
    public Boolean FacingFront = false;
    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "PHOTO_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int READ_REQUEST_CODE = 42;
    private static final int PICKFILE_RESULT_CODE = 271828;

    private static AlbumStorageDirFactory mAlbumStorageDirFactory = new FroyoAlbumDirFactory();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_pro);

        // Create an instance of Camera
        //@TODO Push to back auto
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        try {
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        } catch(Exception e) {
            Cheers(e.getMessage());
        }

        // Add a listener to the Gallery button
        ImageButton captureButton = (ImageButton) findViewById(R.id.pro_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: " +
                        "");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
            //Go on with your life
            releaseCamera();

            Log.d(TAG, "Picture file seems to be written and closed");
            //@TODO Upload Intent & ANALYZE
        }
    };

    /** Beginning File Save
     */
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            try {
                storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            } catch(Exception e) {
                Log.d(TAG, e.getMessage()+" getAlbumDir failed");
                return null;
            }
            //Cheers(storageDir.getAbsolutePath());
            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("munch::camerapro", "failed to create directory");
                        //Cheers("Cannot create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v("munch::camerapro", "External storage is not mounted READ/WRITE.");
            //Cheers("Cannot READ/WRITE to external storage.");
        }

        return storageDir;
    }
    private static String getAlbumName() {
        return "Munch";
    }
    /** **/
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//        File mediaStorageDir = new File(getAlbumDir(), "Munch");
        File mediaStorageDir = getAlbumDir();
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    /** End of Function **/
    private void Cheers(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}