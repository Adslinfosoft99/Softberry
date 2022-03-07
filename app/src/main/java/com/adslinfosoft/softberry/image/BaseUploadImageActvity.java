/**
 *
 */
package com.adslinfosoft.softberry.image;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.R;

import java.io.File;
import java.util.List;

/**
 * @author theBasics
 */
public class BaseUploadImageActvity extends Activity implements
        View.OnClickListener {
    protected Dialog dialogUploadPhoto;
    protected int IMAGE_URI_TYPE;
    protected Bitmap CURRENT_BITMAP;
    private static final int REQ_CODE_GALLERY = 156;
    private static final int CROP_FROM_CAMERA = 26;
    private static final int PICK_FROM_CAMERA = 1;
    private Uri mImageCaptureUri = null;
    private Activity mRegisteredActivity;
    /**
     * Constants for image type
     */
    private static final int SUFFICE_SPACE = 11;
    public static final int IMAGE_GALLERY = 153;
    private static final CharSequence ERROR_CROPPING_IMAGE = "Error cropping image";
    private static final int INSUFFICEINT_SPACE = 12;
    private static final int NO_SD_CARD = 13;
    private static final String TAG = "BaseAddUser";

    /**
     * alert message for take photo option from camera or galary
     */
    protected void alertBoxForUploadImageOp() {
        Log.e("upload image", "alert uplod image called");
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
        Log.e("upload image", "alert uplod image return");
    }

    /**
     * Opens the camera to capture a new Image
     */
    protected void openCamera(Activity context) {
        Log.e("upload image", "openCamera called");
        this.mRegisteredActivity = BaseUploadImageActvity.this;
        int storage = hasStorage();
        if (NO_SD_CARD == storage) {
            Toast.makeText(this, getResources().getString(R.string.no_sd_card),
                    Toast.LENGTH_LONG).show();
        } else if (INSUFFICEINT_SPACE == storage) {
            Toast.makeText(this,
                    getResources().getString(R.string.insuffice_space),
                    Toast.LENGTH_LONG).show();
        } else {
            launchCamera();
        }
        Log.e("upload image", "openCamera return");
    }

    private int hasStorage() {
        // TODO: After fix the bug, add "if (VERBOSE)" before logging errors.
        String state = Environment.getExternalStorageState();
        Log.e(TAG, "storage state is " + state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
                return NO_SD_CARD;
            else {
                StatFs stat = new StatFs(Environment
                        .getExternalStorageDirectory().getPath());
                double sdAvailSize = (double) stat.getAvailableBlocks()
                        * (double) stat.getBlockSize();
                // One binary gigabyte equals 1,073,741,824 bytes.
                return sdAvailSize > 1024 ? SUFFICE_SPACE : INSUFFICEINT_SPACE;
            }

        } else {
            return NO_SD_CARD;
        }
    }

    /**
     * Launches the camera to capture an image
     */
    private void launchCamera() {
        Log.e("upload image", "launchCamera called");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageCaptureUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "tmp_avatar_"
                + System.currentTimeMillis() + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens gallery to select an image
     */
    protected void openGallery(Activity context) {
        Log.e("upload image", "openGallery called");
        if (null != context) {
            Log.e("upload image", "openGallery contex is null");
            this.mRegisteredActivity = BaseUploadImageActvity.this;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                REQ_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult called");
        if (requestCode == REQ_CODE_GALLERY && resultCode == RESULT_OK
                && null != data) {
            Log.e(TAG, "REQ_CODE_GALLERY");
            mImageCaptureUri = data.getData();
            doCrop();

        } else if (requestCode == PICK_FROM_CAMERA) {
            doCrop();
        } else if (requestCode == CROP_FROM_CAMERA) {
            if (resultCode == RESULT_OK) {
                Bundle extras = null;
                if (null != data)
                    extras = data.getExtras();
                if (null != extras) {
                    Bitmap photo = extras.getParcelable("data");
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();
                    if (null != photo) {
                        CURRENT_BITMAP = photo;
                        IMAGE_URI_TYPE = IMAGE_GALLERY;
//                        if (mRegisteredActivity instanceof JobDetailActivity) {
//                         ((ActivityRegister) mRegisteredActivity)
//                                   .setImage(photo);
//                        }
                    }
                } else {
                    Toast.makeText(this, ERROR_CROPPING_IMAGE,
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doCrop() {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

            int size = list.size();

            if (size == 0) {
                Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
                return;
            } else {
                intent.setData(mImageCaptureUri);
                intent.putExtra("outputX", 650);
                intent.putExtra("outputY", 650);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }
}