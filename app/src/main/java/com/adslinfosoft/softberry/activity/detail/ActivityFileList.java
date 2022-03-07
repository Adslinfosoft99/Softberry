package com.adslinfosoft.softberry.activity.detail;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.activity.detail.adapter.FilesAdapter;
import com.adslinfosoft.softberry.model.FileVO;
import com.adslinfosoft.softberry.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityFileList extends AppCompatActivity implements FilesAdapter.PaymentItemClickListener{
    private TextView tvJobNo, tvJobDate;
    private CircularProgressView progressView;
    private RecyclerView mList;
    private ArrayList<FileVO> fileList;
    private String titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_list);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        titleTxt = getIntent().getStringExtra(AppConstants.NAME);
        title.setText(titleTxt);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loading_color)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);
        hideKeyPad();
        getViews();
    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void getViews() {
        progressView = findViewById(R.id.progress_view);
        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        tvJobNo = findViewById(R.id.txt_jobNoVal);
        tvJobDate = findViewById(R.id.txt_jobDate);
        setData();
    }

    private void setData() {
        tvJobNo.setText("" + getIntent().getStringExtra(AppConstants.JOB_NO));
        tvJobDate.setText("" + getIntent().getStringExtra(AppConstants.JOB_DATE));

        fileList = (ArrayList<FileVO>) getIntent().getSerializableExtra(AppConstants.USER_DATA);
        FilesAdapter mAdapter = new FilesAdapter(ActivityFileList.this, fileList, ActivityFileList.this);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void clickOnPaymentItem(String path, String name, String type) {
        String url = path.replaceAll(" ", "%20");
        Intent intent = new Intent(ActivityFileList.this, ActivityShowFile.class);
        intent.putExtra(AppConstants.USER_DATA, url);
        intent.putExtra(AppConstants.NAME, name);
        intent.putExtra(AppConstants.FILE_TYPE, type);
        startActivity(intent);
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
