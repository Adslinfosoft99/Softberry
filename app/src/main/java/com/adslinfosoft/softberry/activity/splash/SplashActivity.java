package com.adslinfosoft.softberry.activity.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.activity.dashboard.TabActivity;
import com.adslinfosoft.softberry.activity.login.ActivityLogin;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.Utils.AppUtils;
import com.adslinfosoft.softberry.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private boolean isRunning = false;

    private CircularProgressView progressView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        progressView = findViewById(R.id.progress_view);
        if (AppUtils.isNetworkAvailable(this)) {
            String userName = Softberry.getPreference().getString(AppConstants.USER_NAME, "");
            String pass = Softberry.getPreference().getString(AppConstants.PASSWORD, "");

            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pass)) {
                isRunning = true;
                makeJsonObjectRequest(userName, pass);
            }
        }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                if (!isRunning) {
                    startLoginScreen();
                }
            }

        }, SPLASH_DISPLAY_LENGTH);
    }

    private void startLoginScreen() {
        Intent mainIntent = new Intent(SplashActivity.this, ActivityLogin.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }


    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeJsonObjectRequest(final String userName, final String pass) {

        if (AppUtils.isNetworkAvailable(this)) {

            progressView.startAnimation();
            progressView.setVisibility(View.VISIBLE);


            String url = API.LOGIN_URL;

            System.out.print("url: " + url);

            StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progressView.stopAnimation();
                        response = response.replaceAll("[\\\\]{1}[\"]{1}", "\"");
                        response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        JSONObject obj = new JSONObject(response);
                        Log.e("response", response);
                        SharedPreferences.Editor edit = Softberry.getPreference().edit();
                        edit.putString(AppConstants.USER_NAME, userName);
                        edit.putString(AppConstants.PASSWORD, pass);
                        edit.putString(AppConstants.ROLE, obj.getString("Roles"));
                        edit.putString(AppConstants.COMPANY_NAME, obj.getString("companyname"));
                        edit.putString(AppConstants.ACCESS_TOKEN, obj.getString("access_token"));
                        edit.putInt(AppConstants.CLIENT_ID, obj.getInt("clientid"));
                        edit.putString(AppConstants.CLIENT_NAME, obj.getString("clientname"));
                        edit.putString(AppConstants.CLIENT_MOBILE, obj.getString("clientmobile"));
                        edit.putString(AppConstants.COMPANY_MOBILE, obj.getString("companymobile"));
                        edit.putString(AppConstants.COMPANY_EMAIL, obj.getString("companyemail"));
                        edit.putString(AppConstants.CLIENT_EMAIL, obj.getString("clientemail"));
                        edit.putString(AppConstants.ADDRESSLINE1, obj.getString("companyaddress"));
                        edit.putString(AppConstants.ADDRESSLINE2, obj.getString("companyaddress2"));
                        edit.putString(AppConstants.CITY, obj.getString("companycity"));
                        edit.putString(AppConstants.STATE, obj.getString("companystate"));
                        edit.putString(AppConstants.PINCODE, obj.getString("companypincode"));
                        edit.putString(AppConstants.COMPANY_SITE, obj.getString("companywebsite"));
                        edit.putString(AppConstants.COMPANY_GSTIN, obj.getString("companygstin"));
                        edit.putString(AppConstants.MARKETING_MANAGER, obj.getString("marketingmanager"));
                        edit.putString(AppConstants.COMPANY_BRANCH, obj.getString("companybranch"));
                        edit.putString(AppConstants.COMPANY_PHONE, obj.getString("companyphone"));
                        edit.putString(AppConstants.STATE_CODE, obj.getString("companystatecode"));
                        edit.putBoolean(AppConstants.IS_LOGIN, true);
                        edit.commit();
                        Intent intent = new Intent(SplashActivity.this, TabActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "The user name or password is incorrect", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    return params;
                }

                //Pass Your Parameters here
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", userName);
                    params.put("password", pass);
                    params.put("grant_type", "password");
                    return params;
                }
            };
            Softberry.getInstance().addToRequestQueue(jsonObjRequest);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error! Please check your net connection.",
                    Toast.LENGTH_LONG).show();
        }
    }


//    /**
//     * Method to make json object request where json response starts wtih {
//     */
//    private void makeJsonObjectRequest(final String userName, final String pass) {
//        progressView.startAnimation();
//
//        String url = API.GET_PROFILE + userName + "/" + pass;
//
//        System.out.print("url: " + url);
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                url, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//                progressView.stopAnimation();
//                progressView.setVisibility(View.GONE);
//                try {
//                    JSONArray arr = response.getJSONArray("GetCustomerResult");
//                    if (arr.length() == 0) {
//                        Toast.makeText(getApplicationContext(),
//                                "Error! Invalid username or password",
//                                Toast.LENGTH_LONG).show();
//                    } else {
//                        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
//                        int len = arr.length();
//                        User user = new User();
//                        ArrayList<Job> jobs = new ArrayList<>();
//                        ArrayList<Job> complete_jobs = new ArrayList<>();
//                        for (int i = 0; i < len; i++) {
//                            JSONObject obj = arr.getJSONObject(i);
//                            if (i == 0) {
//                                user.setId(obj.getInt("cid"));
//                                user.setName(obj.getString("cname"));
//                                user.setCompany(obj.getString("company"));
//                                user.setAdd(obj.getString("address"));
//                                user.setEmail(obj.getString("email"));
//                                user.setMobile(obj.getString("mobile1"));
//                                user.setPhone(obj.getString("phone"));
//                                if (TextUtils.isEmpty(obj.getString("jno"))) {
//                                    continue;
//                                }
//                            }
//                            Job job = new Job();
//                            job.setStatus(obj.getString("jobstatus"));
//                            job.setNo(obj.getString("jno"));
//                            job.setId(obj.getString("jid"));
//                            job.setDate(AppUtils.JsonDateToDate(format, obj.getString("jdate")));
//                            job.setJobDescription(obj.getString("JobDescription"));
//                            job.setDesigner(obj.getString("Designer"));
//                            job.setInvoiceno(obj.getString("InvoiceNo"));
//                            job.setAmount(obj.getString("Amount"));
//                            job.setCoordinator(obj.getString("Coordinator"));
//                            job.setCoordinatorNumber(obj.getString("CoordinatorMobile"));
//                            job.setCoordinatorEmail(obj.getString("CoordinatorEmail"));
//                            job.setDilveryDate(AppUtils.JsonDateToDate(format, obj.getString("FinalDeliveryDate")));
//                            job.setLastImgPath(obj.getString("lastimgpath"));
//                            if (obj.getString("jobstatus").equalsIgnoreCase("Delivered")) {
//                                complete_jobs.add(job);
//                            } else {
//                                jobs.add(job);
//                            }
//                        }
//                        user.setJobs(jobs);
//                        user.setCompleteJobs(complete_jobs);
//
//                        SharedPreferences.Editor edit = DataTrackApplication.getPreference().edit();
//                        edit.putInt(AppConstants.CLIENT_ID, user.getId());
//                        edit.putString(AppConstants.USER_MOBILE, user.getMobile());
//                        edit.putBoolean(AppConstants.IS_LOGIN, true);
//                        edit.putString(AppConstants.NAME, user.getName());
//                        edit.commit();
//
//                        Intent intent = new Intent(SplashActivity.this, TabActivity.class);
//                        intent.putExtra(TabActivity.EXTRA_USER, user);
//                        startActivity(intent);
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                    progressView.stopAnimation();
//                    progressView.setVisibility(View.GONE);
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        "Something went wrong. please try again later.", Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                progressView.stopAnimation();
//                progressView.setVisibility(View.GONE);
//                startLoginScreen();
//            }
//        });
//
//        // Adding request to request queue
//        DataTrackApplication.getInstance().addToRequestQueue(jsonObjReq);
//    }

}
