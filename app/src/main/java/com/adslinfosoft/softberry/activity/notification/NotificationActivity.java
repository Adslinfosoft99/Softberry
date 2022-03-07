package com.adslinfosoft.softberry.activity.notification;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.activity.dashboard.TabActivity;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.db.FetchData;
import com.adslinfosoft.softberry.model.GridItem;
import com.adslinfosoft.softberry.model.Notification;
import com.adslinfosoft.softberry.screens.ShowImage;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.Utils.AppUtils;
import com.adslinfosoft.softberry.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    public static final String EXTRA_USER = "EXTRA_USER";
    private RecyclerView mList;
    private TextView tvNoNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notification);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.title);
        title.setText("Notification");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_indicator);


        FetchData fetchData = new FetchData();
        fetchData.deleteNotifications();

        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList.getRecycledViewPool().setMaxRecycledViews(0, 0);


        List<Notification> list = fetchData.getAllNotification();
        if (list.size() != 0) {
            mList.setAdapter(new SimpleItemRecyclerViewAdapter(list));
        } else {
            mList.setVisibility(View.GONE);
            tvNoNotifications = findViewById(R.id.txtNoJob);
            tvNoNotifications.setVisibility(View.VISIBLE);
            tvNoNotifications.setText("No Notification Found.");
        }

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

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.RowViewHolder> {
        String root;
        private final List<Notification> mValues;

        public SimpleItemRecyclerViewAdapter(List<Notification> items) {
            root = Environment.getExternalStorageDirectory() + AppConstants.FOLDER + "/";
            mValues = items;
        }

        @Override
        public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notification, parent, false);
            return new RowViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RowViewHolder holder, int position) {
            RowViewHolder rowViewHolder = holder;
            int rowPos = rowViewHolder.getAdapterPosition();

            final Notification mItem = mValues.get(rowPos);

            rowViewHolder.mContentView.setText(mItem.getMessage());
            rowViewHolder.mTvTitle.setText(mItem.getTitle());
            rowViewHolder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NotificationDetails.class);
                    intent.putExtra(AppConstants.USER_DATA, mItem);
                    startActivity(intent);
                }
            });
            if (mItem.getIsRead() == 1) {
                rowViewHolder.mRel.setBackgroundColor(R.drawable.read_bg);
            }
            try {
                String path = mItem.getPath();
                String hole = root + mItem.getPath();
                String imageurl = mItem.getImageURL();
//                if (imageurl.contains(".pdf"))
//                {
//                    holder.mImageView.setVisibility(View.VISIBLE);
//                    holder.mImageView.setImageResource(R.drawable.pdffile);
//                } else {
//                    if (!TextUtils.isEmpty(path)) {
//                        File imageFile = new File(hole);
//                        if (imageFile.exists()) {
//                            holder.mImageView.setVisibility(View.VISIBLE);
//                            holder.mImageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
//                        }
//                    } else {
//                        holder.mImageView.setVisibility(View.GONE);
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class RowViewHolder extends RecyclerView.ViewHolder {
            protected View mView;
            protected TextView mContentView;
//            protected TextView mTvDate;
            protected TextView mTvTitle;
            protected CardView mCard;
            //            public final ImageView mImageView;
            public Notification mItem;
            protected RelativeLayout mRel;

            public RowViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = view.findViewById(R.id.tv_msg);
//                mTvDate = (TextView) view.findViewById(R.id.tv_date);
                mTvTitle = view.findViewById(R.id.tv_title);
                mCard = view.findViewById(R.id.card_login);
                mRel = view.findViewById(R.id.relOne);
//                mImageView = (ImageView) view.findViewById(R.id.img_notification);

//                mImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mItem.getImageURL().contains(".pdf"))
//                        {
//                            Intent intent = new Intent(NotificationActivity.this, ActivityOpenPDF.class);
//
//                            intent.putExtra("NOTIFICATION_DATA", mItem.getImageURL());
//                            intent.putExtra("ACTIVITY_NUMBER", 1);
//
//                            //Start details activity
//                            startActivity(intent);
//                            Log.e("Notification Activity", "ActivityOpenPDF Called");
//                        } else {
//                            mJonNo = mItem.getJobNo();
//                            mCorrectionCount = mItem.getCorrectionCount();
//                            mEmail = mItem.getCoordinatorEmail();
//                            openImages(mJonNo, mCorrectionCount, mEmail, mItem.getPath());
//
//                        }
//                    }
//                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private void openImages(final String jobno, final String correctioncount, final String email, final String imagurl) {
        String userName = Softberry.getPreference().getString(AppConstants.USER_NAME, "");
        String pass = Softberry.getPreference().getString(AppConstants.PASSWORD, "");

        if (AppUtils.isNetworkAvailable(this)) {
            String url = "";

            System.out.print("url: " + url);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        JSONArray arr = response.getJSONArray("GetJobImagesByCountResult");
                        if (arr.length() == 0) {
                            Log.e("imagurl", imagurl);
                            Intent intent = new Intent(NotificationActivity.this, ShowImage.class);
                            intent.putExtra(AppConstants.IMAGE_PATH, imagurl);
                            intent.putExtra(AppConstants.IS_NOTIFICATION, 1);
                            startActivity(intent);
                        } else {
                            ArrayList<GridItem> mGridData = new ArrayList<>();
                            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                            int len = arr.length();
                            Log.e("Array Length", "" + arr.length());
                            for (int i = 0; i < len; i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String title = AppUtils.JsonDateToDate(format, obj.optString("uploaddate"));
                                GridItem item = new GridItem();
                                item.setTitle(title);
                                item.setImage(obj.optString("imagepath"));
                                String urltype = obj.optString("imagepath");
                                if (urltype.contains(".pdf")) {
                                    item.setPdf(true);
                                }
                                mGridData.add(item);
                            }

//                            Intent intent = new Intent(NotificationActivity.this, ShowImagesActivity.class);
//                            intent.putExtra(EXTRA_USER, mGridData);
//                            intent.putExtra(AppConstants.JOB_NO, mJonNo);
//                            intent.putExtra(AppConstants.CORDINATOR_EMIL, email);
//                            startActivity(intent);

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

            // Adding request to request queue
            Softberry.getInstance().addToRequestQueue(jsonObjReq);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error! Please check your net connection.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotificationActivity.this, TabActivity.class);
        startActivity(intent);
        finishAffinity();
    }

}
