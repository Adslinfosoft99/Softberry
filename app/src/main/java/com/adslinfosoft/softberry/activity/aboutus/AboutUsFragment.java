package com.adslinfosoft.softberry.activity.aboutus;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adslinfosoft.softberry.API.API;
import com.adslinfosoft.softberry.Utils.AppConstants;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;

public class AboutUsFragment extends Fragment implements OnMapReadyCallback {
    private TextView tvWebsite, tvPortal, tvCompanyEmail, tvAddress, tvCompanyMobile;
    private CircularProgressView progressView;
    private static final String TAG = "AboutUsFragment";
    private double latitude, longitute;
    private GoogleMap mMap;

    public AboutUsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_aboutus, container, false);
        progressView = view.findViewById(R.id.progress_view);
        tvAddress = view.findViewById(R.id.txt_addressVal);
        tvCompanyMobile = view.findViewById(R.id.txt_companyPhone);
        tvWebsite = view.findViewById(R.id.txt_websiteVal);
        tvPortal = view.findViewById(R.id.txt_portalVal);
        tvCompanyEmail = view.findViewById(R.id.txt_companyEmailVal);
        Register task = new Register();
        task.execute();
        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private class Register extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressView.startAnimation();
            progressView.setVisibility(View.VISIBLE);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... args) {
            String status = null;
            String url = API.GET_ABOUT_US;
            JsonObjectRequest req = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "response: " + response);
                            progressView.stopAnimation();
                            progressView.setVisibility(View.GONE);
                            parseData(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getActivity(),
                            "Something went wrong. please try again later.", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);

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

    private void parseData(JSONObject response) {
        String address, addressLine1, addressLine2, city, state, pincode, name, geolocation;
        try {
            name = response.getString("companyname");
            addressLine1 = response.getString("addressline1");
            addressLine2 = response.getString("addressline2");
            city = response.getString("city");
            state = response.getString("state");
            pincode = response.getString("pincode");
            address = name + "\n" + addressLine1 + "," + addressLine2 + ", " + city + "," + state + " - " + pincode;
            tvAddress.setText("" + address);
            tvCompanyEmail.setText("" + response.getString("email"));
            tvWebsite.setText("" + response.getString("website"));
            tvPortal.setText("" + response.getString("website"));
            tvCompanyMobile.setText("" + response.getString("contactno"));
            geolocation = response.getString("geolocation");
            String[] separated = geolocation.split(",");
            latitude = Double.parseDouble(separated[0]);
            longitute = Double.parseDouble(separated[1]);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("latitude", "" + latitude);
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitute);
        mMap.addMarker(new MarkerOptions().position(sydney).title("SOFTBERRY TECHNOLOGY PVT. LTD."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
    }


}
