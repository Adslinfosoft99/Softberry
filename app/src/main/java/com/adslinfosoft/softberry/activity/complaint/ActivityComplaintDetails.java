package com.adslinfosoft.softberry.activity.complaint;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.model.Complaint;
import com.adslinfosoft.softberry.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityComplaintDetails extends AppCompatActivity {
    private TextView tvJobNo, tvIssue, tvDate, tvDescr;
    private Complaint mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_details);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Complaint List");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loading_color)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);
        hideKeyPad();
        getViews();

    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void getViews() {
        mDao = (Complaint) getIntent().getSerializableExtra(AppConstants.USER_DATA);
        tvJobNo = findViewById(R.id.txt_jobNoVal);
        tvIssue = findViewById(R.id.et_email1);
        tvDate = findViewById(R.id.et_email2);
        tvDescr = findViewById(R.id.tv_title);
        setData(mDao);
    }

    private void setData(Complaint mDao) {
        tvJobNo.setText("" + mDao.getJobNo());
        tvIssue.setText("" + mDao.getIssue());
        tvDate.setText("" + mDao.getDate());
        tvDescr.setText("" + mDao.getIssueDescr());
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
