package com.adslinfosoft.softberry.activity.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.R;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.Utils.AppUtils;
import com.adslinfosoft.softberry.activity.aboutus.AboutUsFragment;
import com.adslinfosoft.softberry.activity.complaint.ActivityComplaintList;
import com.adslinfosoft.softberry.activity.complaint.ActivityPostComplaint;
import com.adslinfosoft.softberry.activity.enquiry.ActivityEnquiryList;
import com.adslinfosoft.softberry.activity.enquiry.ActivityPostEnquiry;
import com.adslinfosoft.softberry.activity.login.ActivityLogin;
import com.adslinfosoft.softberry.activity.notification.NotificationActivity;
import com.adslinfosoft.softberry.activity.profile.ActivityChangePassword;
import com.adslinfosoft.softberry.activity.profile.frag.MyProfileFragment;
import com.adslinfosoft.softberry.adapter.ExpandableListAdapter;
import com.adslinfosoft.softberry.app.Config;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.frag.HomeFragment;
import com.adslinfosoft.softberry.model.ExpandedMenuModel;
import com.adslinfosoft.softberry.model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TabActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_READ_PHONE_STATE = 123;
    public static String EXTRA_USER = "EXTRA_USER";
    private String mToken;
    private String registered_email;

    private static final String TAG = "TabActivity";

    private User mUserDao;
    private static int mBackCount;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    List<ExpandedMenuModel> headerList = new ArrayList<>();
    HashMap<ExpandedMenuModel, List<ExpandedMenuModel>> childList = new HashMap<>();
    private ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissionsRejected = new ArrayList<>();
    private final ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mUserDao = (User) getIntent().getSerializableExtra(EXTRA_USER);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, null);
                toolbar.setNavigationIcon(d);
            }
        });
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        View mCustomView = mInflater.inflate(R.layout.action_bar, null);
        toolbar.addView(mCustomView);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.loading_color)));
        TextView title = mCustomView.findViewById(R.id.title);
        title.setText("Home");


        mBottomNav = findViewById(R.id.bottom_navigation);
        mBottomNav.setOnNavigationItemSelectedListener(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        expandableListView = findViewById(R.id.expandable_navigation);
        prepareMenuDataLogin();
        populateExpandableLoginList();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        if (savedInstanceState == null) {
            mBottomNav.setSelectedItemId(R.id.menu_home); // change to whichever id should be default
        }
        setListener();

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(CAMERA);
        permissions.add(READ_CONTACTS);
        permissions.add(WRITE_EXTERNAL_STORAGE);

        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        if (AppUtils.isNetworkAvailable(this)) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(TabActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    mToken = instanceIdResult.getToken();
                    Log.e("mToken", mToken);
                    if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                        Register task = new Register();
                        task.execute();
                    }

                }
            });

            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(TabActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void setListener() {
        NavigationView view = findViewById(R.id.nav_view);

        View navigationView = view.getHeaderView(0);
        if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
//            imageLoader = ImageLoader.getInstance();
            if (!ImageLoader.getInstance().isInited()) {
                ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3).threadPriority(1).defaultDisplayImageOptions(new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).build()).memoryCache(new LRULimitedMemoryCache(GravityCompat.RELATIVE_LAYOUT_DIRECTION)).diskCache(new LimitedAgeDiskCache(new File(StorageUtils.getCacheDirectory(getApplicationContext()), "icons"), null, 2592000)).build());
            }
            TextView tvName = navigationView.findViewById(R.id.tv_login_name);
            tvName.setText(Softberry.getPreference().getString(AppConstants.CLIENT_NAME, ""));
            TextView tvEmail = navigationView.findViewById(R.id.tv_emailId);
            tvEmail.setText(Softberry.getPreference().getString(AppConstants.CLIENT_EMAIL, ""));
            final ImageView mImg = navigationView.findViewById(R.id.imageView);
//            if (TextUtils.isEmpty(DataTrackApplication.getPreference().getString(AppConstants.SHOP_LOGO, ""))) {
//                mImg.setImageResource(R.mipmap.maleicon);
//            } else {
//                imageLoader.loadImage(DataTrackApplication.getPreference().getString(AppConstants.SHOP_LOGO, ""), new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        mImg.setImageBitmap(loadedImage);
//                    }
//                });
//            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_profile:
                fragment = new MyProfileFragment();
                break;
            case R.id.menu_contact:
                fragment = new AboutUsFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void prepareMenuDataLogin() {

        ExpandedMenuModel menuModel = new ExpandedMenuModel("My Account", true, true); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        List<ExpandedMenuModel> childModelsList = new ArrayList<>();

        ExpandedMenuModel childModel = new ExpandedMenuModel("Change Password", false, false);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new ExpandedMenuModel("Post Enquiry", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new ExpandedMenuModel("Enquiry List", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);


        menuModel = new ExpandedMenuModel("Notifications", true, false);
        headerList.add(menuModel);

        menuModel = new ExpandedMenuModel("Post Complaint", true, false);
        headerList.add(menuModel);

        menuModel = new ExpandedMenuModel("Complaint List", true, false);
        headerList.add(menuModel);

        menuModel = new ExpandedMenuModel("Log out", true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
    }


    private void populateExpandableLoginList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList, true);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        Intent intent;
                        switch (groupPosition) {
                            case 1:
                                if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                                    intent = new Intent(getApplicationContext(), ActivityPostEnquiry.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                    startActivity(intent);
                                }
                                break;
                            case 2:
                                if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                                    intent = new Intent(getApplicationContext(), ActivityEnquiryList.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                    startActivity(intent);
                                }
                                break;
                            case 3:
                                if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                                    intent = new Intent(getApplicationContext(), NotificationActivity.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                    startActivity(intent);
                                }
                                break;
                            case 4:
                                if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                                    intent = new Intent(getApplicationContext(), ActivityPostComplaint.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                    startActivity(intent);
                                }
                                break;
                            case 5:
                                if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                                    intent = new Intent(getApplicationContext(), ActivityComplaintList.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                    startActivity(intent);
                                }
                                break;
                            case 6:
                                if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                                    SharedPreferences.Editor edit = Softberry.getPreference().edit();
                                    edit.putString(AppConstants.USER_NAME, "");
                                    edit.putString(AppConstants.PASSWORD, "");
                                    edit.putBoolean(AppConstants.IS_LOGIN, false);
                                    edit.commit();
                                    finishAffinity();
                                    startHomeScreen();

                                }
                                break;
                            default:
                                break;
                        }

                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    ExpandedMenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    switch (groupPosition) {
                        case 0:
                            Intent intent;
                            switch (childPosition) {
                                case 0:
                                    if (Softberry.getPreference().getBoolean(AppConstants.IS_LOGIN, false)) {
                                        intent = new Intent(getApplicationContext(), ActivityChangePassword.class);
                                        startActivity(intent);
                                    } else {
                                        intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                        startActivity(intent);
                                    }
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }

                return false;
            }
        });
    }

    private void startHomeScreen() {
        Intent mainIntent = new Intent(this, ActivityLogin.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mainIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mBackCount > 1) {
            mBackCount = 0;
            super.onBackPressed();
        } else {
            ++mBackCount;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
    }


    private class Register extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String status = null;
            JSONObject params = new JSONObject();
            try {
                params.put("gcmid", mToken);
                params.put("devicetype", "Android");
                params.put("udid", Settings.Secure.getString(TabActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                Log.e("params", "" + params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = API.UPDATE_MEMBER_LOGIN;
            JsonObjectRequest req = new JsonObjectRequest(url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response:%n %s", response.toString());
                            try {
                                String msg = response.getString("message");
                                if (msg.equals("success")) {
                                    Log.e(TAG, "Update Login Details");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "bearer " + Softberry.getPreference().getString(AppConstants.ACCESS_TOKEN, ""));
                    return params;
                }

            };

            Softberry.getInstance().addToRequestQueue(req);
            return status;
        }
    }
}
