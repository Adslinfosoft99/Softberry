package com.adslinfosoft.softberry.image;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.adslinfosoft.softberry.Utils.Permissions;
import com.adslinfosoft.softberry.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * Created by shreshtha on 29/08/16.
 */
public abstract class PhotoPicker extends AppCompatActivity implements
        View.OnClickListener {
    private int type;
    protected Dialog dialogUploadPhoto;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private boolean isCameraTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Permissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isCameraTask) {
                        cameraIntent();
                    } else {
                        galleryIntent();
                    }
                } else {
                    //code for deny
                }
                break;
        }
    }

    /**
     * alert message for take photo option from camera or galary
     */
    protected void alertBoxForUploadImageOp(int type) {
        this.type = type;
        Log.d("upload image", "alert upload image called");
        dialogUploadPhoto = new Dialog(this, R.style.WindowTitleBackground);
        dialogUploadPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUploadPhoto.setContentView(R.layout.dialog_upload_image);
        dialogUploadPhoto.setCancelable(false);
        TextView mTvCamera = dialogUploadPhoto
                .findViewById(R.id.txt_openCamera);
        TextView mTvGallary = dialogUploadPhoto
                .findViewById(R.id.txt_openGallery);
        Button mButtonCancel = dialogUploadPhoto
                .findViewById(R.id.btn_CancelDialogUpload);

        mTvCamera.setOnClickListener(this);
        mTvGallary.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
        dialogUploadPhoto.show();
        Log.d("upload image", "alert uplod image return");
    }

    @Override
    public void onClick(View v) {
        boolean result;
        switch (v.getId()) {
            case R.id.txt_openCamera:
                isCameraTask = true;
                result = Permissions.checkPermission(this);
                dialogUploadPhoto.dismiss();
                if (result) {
                    cameraIntent();
                }
                break;
            case R.id.txt_openGallery:
                isCameraTask = false;
                result = Permissions.checkPermission(this);
                dialogUploadPhoto.dismiss();
                if (result) {
                    galleryIntent();
                }
                break;
            case R.id.btn_CancelDialogUpload:
                dialogUploadPhoto.dismiss();
                break;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setImage(thumbnail, type);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setImage(bm, type);
    }

    protected abstract void setImage(Bitmap thumbnail, int type);

    protected abstract void removeImage(int type);

    protected abstract void zoomImage(int type);

}
