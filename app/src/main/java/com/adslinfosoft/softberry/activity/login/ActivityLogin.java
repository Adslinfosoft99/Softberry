package com.adslinfosoft.softberry.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.activity.dashboard.TabActivity;
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
import androidx.core.app.ActivityCompat;

/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "ActivityLogin";

    private static final int REQUEST_READ_CONTACTS = 0;

    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView mtxtRegister;
    private CircularProgressView progressView;
    int PERMISSION_ALL = 1;
    private boolean Permission_is_granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        hideKeyPad();
        getViews();
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    Permission_is_granted = true;
                }
            }
        }
        return true;
    }

    private void getViews() {

        progressView = findViewById(R.id.progress_view);

        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView tvForgot = findViewById(R.id.txt_forgot);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void hideKeyPad() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        String phone = mEmailView.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            mEmailView.setError("Please Enter Mobile Number");
        } else {
            String password = mPasswordView.getText().toString();
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError("Please Enter Password");
            } else {
                makeJsonObjectRequest();
            }
        }

    }




    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeJsonObjectRequest() {

        if (AppUtils.isNetworkAvailable(this)) {

            progressView.startAnimation();
            progressView.setVisibility(View.VISIBLE);

            final String userName = mEmailView.getText().toString();
            final String pass = mPasswordView.getText().toString();

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
                        Intent intent = new Intent(ActivityLogin.this, TabActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

}

