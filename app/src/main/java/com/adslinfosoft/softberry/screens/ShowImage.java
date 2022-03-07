package com.adslinfosoft.softberry.screens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by alokgupta on 01/09/16.
 */
public class ShowImage extends AppCompatActivity {

    private SubsamplingScaleImageView imageView;
    private ImageLoader imageLoader;
    private String path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        imageView = findViewById(R.id.imageView);
        String root = Environment.getExternalStorageDirectory() + AppConstants.FOLDER+"/";
        path = getIntent().getStringExtra(AppConstants.IMAGE_PATH);
        int index = getIntent().getIntExtra(AppConstants.IS_NOTIFICATION,0);
        if (index == 1)
        {
            File imgFile = new  File(root+path);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                imageView.setImage(ImageSource.bitmap(myBitmap));

            }
        }
        else if (index == 2)
        {
            if (!ImageLoader.getInstance().isInited()) {
                ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3).threadPriority(1).defaultDisplayImageOptions(new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).build()).memoryCache(new LRULimitedMemoryCache(GravityCompat.RELATIVE_LAYOUT_DIRECTION)).diskCache(new LimitedAgeDiskCache(new File(StorageUtils.getCacheDirectory(getApplicationContext()), "icons"), null, 2592000)).build());
            }
            imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(path, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImage(ImageSource.bitmap(loadedImage));
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
