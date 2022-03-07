package com.adslinfosoft.softberry.screens;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adslinfosoft.softberry.model.GridItem;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

/**
 * Created by alokgupta on 24/10/16.
 */
public class ActivityOpenPDF extends AppCompatActivity {
    private ArrayList<GridItem> mGridData;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openpdf);
        mWebView = findViewById(R.id.webview);

        int activity_number = getIntent().getIntExtra("ACTIVITY_NUMBER", 0);

        if (activity_number == 1)
        {
            String url = getIntent().getExtras().getString("NOTIFICATION_DATA");
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url.replace(" ","%20"));
        }
        else if (activity_number == 2) {
            mGridData = (ArrayList<GridItem>) getIntent().getSerializableExtra("GRID_DATA");
            int pos = getIntent().getIntExtra("IMAGE_POSTION", 0);
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mGridData.get(pos).getImage().replace(" ", "%20"));
        }
        final Activity MyActivity = this;
        // Makes Progress bar Visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Make the bar disappear after URL is loaded, and changes
                // string to Loading...
                MyActivity.setTitle("Loading...");
                MyActivity.setProgress(progress * 100);
                if (progress > 90){
                    MyActivity.setTitle(R.string.app_name);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
}
