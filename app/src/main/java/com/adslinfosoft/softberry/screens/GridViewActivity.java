//package com.adsl.datatracksystem.screens;
//
//import android.content.Intent;
//import android.content.res.Resources;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import GridViewAdapter;
//import DataTrackApplication;
//import GridItem;
//import Job;
//import AppConstants;
//import AppUtils;
//import com.adsl.orangeleaf.R;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//
//public class GridViewActivity extends ActionBarActivity implements
//        OnItemSelectedListener {
//    private static final String TAG = GridViewActivity.class.getSimpleName();
//
//    private GridView mGridView;
//    private ProgressBar mProgressBar;
//    public static String EXTRA_USER = "EXTRA_USER";
//    private GridViewAdapter mGridAdapter;
//    private ArrayList<GridItem> mGridData;
//    private String FEED_URL;
//    private ArrayList<Job> mJobs;
//    private Spinner mSpinner;
//    private ArrayList<String> list = new ArrayList<String>();
//    private Resources mResource;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gridview);
//        mResource = getResources();
//        mGridView = (GridView) findViewById(R.id.gridView);
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mSpinner = (Spinner) findViewById(R.id.spjobno);
//        mSpinner.setOnItemSelectedListener(this);
//        mJobs = (ArrayList<Job>) getIntent().getSerializableExtra(EXTRA_USER);
//
//
//        for(Job dao: mJobs)
//        {
//            String jobno = dao.getNo();
//            list.add(jobno);
//        }
//
//        //Initialize with empty data
//        mGridData = new ArrayList<>();
//        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
//        mGridView.setAdapter(mGridAdapter);
//
//        //Grid view click event
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                //Get item at position
//                GridItem item = (GridItem) parent.getItemAtPosition(position);
//
//                Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
//                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);
//
//                // Interesting data to pass across are the thumbnail size/location, the
//                // resourceId of the source bitmap, the picture description, and the
//                // orientation (to avoid returning back to an obsolete configuration if
//                // the device rotates again in the meantime)
//
//                int[] screenLocation = new int[2];
//                imageView.getLocationOnScreen(screenLocation);
//
//                //Pass the image title and url to DetailsActivity
//                intent.putExtra("title", item.getTitle()).
//                        putExtra("image", item.getImage());
//
//                //Start details activity
//                startActivity(intent);
//            }
//        });
//
//        //Start download
////        new AsyncHttpTask().execute(FEED_URL);
////        mProgressBar.setVisibility(View.VISIBLE);
//        loadSpinnerData();
//    }
//
//    private void loadSpinnerData() {
//        list.add(0, mResource.getString(R.string.business_prompt));
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        mSpinner.setAdapter(dataAdapter);
//    }
//
//
//    //Downloading data asynchronously
//    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
//
//        @Override
//        protected Integer doInBackground(String... params) {
//            Integer result = 0;
//            try {
//                // Create Apache HttpClient
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
//                int statusCode = httpResponse.getStatusLine().getStatusCode();
//
//                // 200 represents HTTP OK
//                if (statusCode == 200) {
//                    String response = streamToString(httpResponse.getEntity().getContent());
//                    parseResult(response);
//                    result = 1; // Successful
//                } else {
//                    result = 0; //"Failed
//                }
//            } catch (Exception e) {
//                Log.d(TAG, e.getLocalizedMessage());
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//            // Download complete. Lets update UI
//
//            if (result == 1) {
//                mGridAdapter.setGridData(mGridData);
//            } else {
//                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
//            }
//
//            //Hide progressbar
//            mProgressBar.setVisibility(View.GONE);
//        }
//    }
//
//
//    String streamToString(InputStream stream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
//        String line;
//        String result = "";
//        while ((line = bufferedReader.readLine()) != null) {
//            result += line;
//        }
//
//        // Close stream
//        if (null != stream) {
//            stream.close();
//        }
//        return result;
//    }
//
//    /**
//     * Parsing the feed results and get the list
//     *
//     * @param result
//     */
//    private void parseResult(String result) {
//        mGridData.clear();
//        try {
//            JSONObject response = new JSONObject(result);
//            JSONArray posts = response.optJSONArray("GetJobImagesResult");
//            if (posts.length() == 0) {
//                Toast.makeText(getApplicationContext(),
//                        "No Images Avilable for this job",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                GridItem item = null;
//                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
//                for (int i = 0; i < posts.length(); i++) {
//                    JSONObject post = posts.optJSONObject(i);
//                    String title = AppUtils.JsonDateToDate(format, post.optString("uploaddate"));
//                    item = new GridItem();
//                    item.setTitle(title);
//                    item.setImage(post.optString("imagepath"));
//                    mGridData.add(item);
//                    }
//                }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position,
//                               long id) {
//        // On selecting a spinner item
//        String jobno = parent.getItemAtPosition(position).toString();
//        String userName = DataTrackApplication.getPreference().getString(AppConstants.USER_NAME, "");
//        String pass = DataTrackApplication.getPreference().getString(AppConstants.PASSWORD, "");
//        if (mSpinner.getSelectedItemPosition() == 0) {
//            mGridData.clear();
//            mGridAdapter.notifyDataSetChanged();
////            mGridAdapter.setGridData(mGridData);
//        } else {
//            FEED_URL = "http://visioninfotechonline.com/customerol.svc/GetJobImages/" + userName + "/" + pass + "/" + jobno;
//            new AsyncHttpTask().execute(FEED_URL);
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//
//
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> arg0) {
//        // TODO Auto-generated method stub
//
//    }
//}