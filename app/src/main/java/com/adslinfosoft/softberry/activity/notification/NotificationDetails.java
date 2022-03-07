package com.adslinfosoft.softberry.activity.notification;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.adslinfosoft.softberry.db.FetchData;
import com.adslinfosoft.softberry.model.Notification;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationDetails extends AppCompatActivity {

    private static final String TAG = "NotificationDetails";
    private CircularProgressView progressView;
    private TextView tvTitle, tvMessage;
    private Notification mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reply);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Notification Details");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);
        hideKeyPad();
        getViews();
    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void getViews() {
        progressView = findViewById(R.id.progress_view);
        mDao = (Notification) getIntent().getSerializableExtra(AppConstants.USER_DATA);
        tvTitle = findViewById(R.id.txt_title);
        tvMessage = findViewById(R.id.tv_msg);
        setData();
    }

    private void setData() {
        tvTitle.setText("" + mDao.getTitle());
        tvMessage.setText("" + mDao.getMessage());
        FetchData mFetchData = new FetchData();
        mDao.setIsRead(1);
        int updateStatus = mFetchData.updateNotification(mDao);
        if (0 != updateStatus) {
            Log.e(TAG, "Updated Successfully");
        } else {
            Log.e(TAG, "Updated not Successfully");
        }
    }

}
