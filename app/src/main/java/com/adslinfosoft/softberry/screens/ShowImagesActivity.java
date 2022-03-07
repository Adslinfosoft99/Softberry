package com.adslinfosoft.softberry.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.adslinfosoft.softberry.adapter.NotificationImgAdapter;
import com.adslinfosoft.softberry.model.GridItem;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.R;

import java.util.ArrayList;

/**
 * Created by shreshtha on 19/09/16.
 */
public class ShowImagesActivity extends AppCompatActivity  {
    public static final String EXTRA_USER = "EXTRA_USER";
    public static String CLIENT_ID = "CLIENT_ID";
    private String mJobNo, mEmail;
    private ArrayList<GridItem> mGridData;
    private NotificationImgAdapter mAdapter;
    private GridView mGridView;
    private int mClientId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        mJobNo = getIntent().getStringExtra(AppConstants.JOB_NO);
        mEmail = getIntent().getStringExtra(AppConstants.CORDINATOR_EMIL);
        mClientId = getIntent().getIntExtra(CLIENT_ID, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getViews();
    }

    private void getViews() {
        mGridView = findViewById(R.id.gridView);
        mGridData = (ArrayList<GridItem>) getIntent().getSerializableExtra(EXTRA_USER);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (mGridData.get(position).isPdf())
                {
                    Intent intent = new Intent(ShowImagesActivity.this, ActivityOpenPDF.class);

                    intent.putExtra("GRID_DATA", mGridData);
                    intent.putExtra("IMAGE_POSTION", position);
                    intent.putExtra("ACTIVITY_NUMBER", 2);

                    //Start details activity
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ShowImagesActivity.this, ImageSlideShow.class);

                    intent.putExtra("GRID_DATA", mGridData);
                    intent.putExtra("IMAGE_POSTION", position);
                    intent.putExtra(AppConstants.JOB_NO, mJobNo);
                    intent.putExtra(AppConstants.CORDINATOR_EMIL, mEmail);
                    intent.putExtra("CLIENT_ID", mClientId);
                    //Start details activity
                    startActivity(intent);
                }
            }
        });
        mAdapter = new NotificationImgAdapter(this, false, R.layout.row_img_notification, mGridData);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
