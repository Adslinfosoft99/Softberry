package com.adslinfosoft.softberry.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.model.GridItem;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alokgupta on 23/09/16.
 */
public class ImageSlideShow extends AppCompatActivity {

    private ArrayList<GridItem> mGridData;
    private ImagePagerAdapter adapter;
    private String image;
    private ImageLoader imageLoader;
    private ViewPager viewPager;
    private String mJobNo, mEmail;
    private final Context context = this;
    private int mClientID;
    public static String CLIENT_ID = "CLIENT_ID";
    private static final String TAG = "ImageSlideShow";
    private String imgString;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        mJobNo = getIntent().getStringExtra(AppConstants.JOB_NO);
        mEmail = getIntent().getStringExtra(AppConstants.CORDINATOR_EMIL);
        mClientID = getIntent().getIntExtra(CLIENT_ID, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mGridData = (ArrayList<GridItem>) getIntent().getSerializableExtra("GRID_DATA");
        int pos = getIntent().getIntExtra("IMAGE_POSTION",0);
         viewPager = findViewById(R.id.view_pager);
        adapter = new ImagePagerAdapter(mGridData);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3).threadPriority(1).defaultDisplayImageOptions(new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).build()).memoryCache(new LRULimitedMemoryCache(GravityCompat.RELATIVE_LAYOUT_DIRECTION)).diskCache(new LimitedAgeDiskCache(new File(StorageUtils.getCacheDirectory(getApplicationContext()), "icons"), null, 2592000)).build());
        }
        imageLoader = ImageLoader.getInstance();
        image = mGridData.get(pos).getImage();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }

    private class ImagePagerAdapter extends PagerAdapter {
        private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();
        private final ImageLoader imageLoader;
        private static final int ANIM_DURATION = 600;
        private TextView titleTextView;
        private SubsamplingScaleImageView imageView;

        public ImagePagerAdapter(ArrayList<GridItem> mGridData) {
            this.mGridData = mGridData;
            if (!ImageLoader.getInstance().isInited()) {
                ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3).threadPriority(1).defaultDisplayImageOptions(new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).build()).memoryCache(new LRULimitedMemoryCache(GravityCompat.RELATIVE_LAYOUT_DIRECTION)).diskCache(new LimitedAgeDiskCache(new File(StorageUtils.getCacheDirectory(getApplicationContext()), "icons"), null, 2592000)).build());
            }
            imageLoader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            return mGridData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Context context = ImageSlideShow.this;
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.activity_details_view, container, false);


        //Set the background color to black

        titleTextView = root.findViewById(R.id.title);
        titleTextView.setText(Html.fromHtml(mGridData.get(position).getTitle()));

        imageView = root.findViewById(R.id.grid_item_image);
            if (mGridData.get(position).isPdf())
            {
                imageView.setImage(ImageSource.resource(R.drawable.pdffile));
            } else {
                imageLoader.loadImage(mGridData.get(position).getImage(), new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        imageView.setImage(ImageSource.bitmap(loadedImage));
                    }
                });
            }
        container.addView(root, 0);
        return root;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.action_share:
            int position = viewPager.getCurrentItem();
            String image_path = mGridData.get(position).getImage().replace(" ","%20");
            shareItem(image_path);
            return(true);
        case R.id.action_comment:
            showDialog();
            return(true);
        case android.R.id.home:
            finish();
            return (true);

    }
        return(super.onOptionsItemSelected(item));
    }

    public void shareItem(String url) {
        imageLoader.loadImage(url, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(loadedImage));
                startActivity(Intent.createChooser(i, "Share Image"));
            }
        });

    }

    public void shareEmail(String url, final String comment) {
        imageLoader.loadImage(url, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                String[] emailAddresses = { "support@softberry.in", mEmail};
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("image/*");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                        best = info;
                        break;
                    }
                }
                if (best != null) {
                    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                }
                intent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        emailAddresses);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Job No. " + mJobNo);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, comment);
                intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(loadedImage));

                startActivity(intent);
            }
        });

    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void showDialog()
    {
        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);
        inputAlert.setTitle("Softberry");
        inputAlert.setMessage("Enter Your Comment");
        final EditText userInput = new EditText(context);
        inputAlert.setView(userInput);
        inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInputValue = userInput.getText().toString();
                if(userInputValue != null && !userInputValue .isEmpty())
                {
                    Register task = new Register(mJobNo, userInputValue);
                    task.execute();
                } else
                {
                    showToast("Please Enter Comment.");
                }

            }
        });
        inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private class Register extends AsyncTask<String, String, String> {
        private final String job_no;
        private final String cmt;
        private int clientid;


        Register(String Jobno, String comment) {
            this.job_no = Jobno;
            this.cmt = comment;
            if (mClientID != 0)
            {
                this.clientid = mClientID;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String status = null;
            JSONObject obj = new JSONObject();
            int position = viewPager.getCurrentItem();
            String image_path = mGridData.get(position).getImage().replace(" ","%20");
            try {
                Bitmap bmp = BitmapFactory.decodeStream(new java.net.URL(image_path).openStream());
                imgString = Base64.encodeToString(getBytesFromBitmap(bmp),
                        Base64.NO_WRAP);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject params = new JSONObject();
                params.put("jobno", job_no);
                params.put("comment", cmt);
                params.put("cid", clientid);
                params.put("imgbyte", imgString);
                obj.put("qcmt", params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = "http://softberry.co.in/CustomerOL.svc/QuickComment";
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST ,url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            VolleyLog.v("Response:%n %s", response.toString());
                            try {
                                String status = response.getString("QuickCommentResult");
                                if (status.toLowerCase().contains("success")) {
                                    showToast("Comment Post Successfully.");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                }
            });
            req.setRetryPolicy(new DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Softberry.getInstance().addToRequestQueue(req);
            return status;
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

}

