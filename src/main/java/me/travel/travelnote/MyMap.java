package me.travel.travelnote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MyMap extends FragmentActivity implements LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private Location location;

    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MyMap.this, "Location permission is not available for google map api", Toast.LENGTH_LONG).show();
            return;
        }

        // Getting reference to the SupportMapFragment of activity_main.xml
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        mMap = fm.getMap();

        // Enabling MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        focusCurrentLoc();

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottom_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                callEvent(menuItemId);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                callEvent(menuItemId);
                //Toast.makeText(getApplicationContext(), getMessage(menuItemId, true), Toast.LENGTH_SHORT).show();
            }
        });
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        double left = vr.latLngBounds.southwest.longitude;
        double top = vr.latLngBounds.northeast.latitude;
        double right = vr.latLngBounds.northeast.longitude;
        double bottom = vr.latLngBounds.southwest.latitude;

        Toast.makeText(MyMap.this, "dis : " + top, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private String getMessage(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.bb_menu_my_loc:
                message += "my location";
                break;
            case R.id.bb_menu_calendar:
                message += "notes";
                break;
            case R.id.bb_menu_search:
                message += "search";
                break;
            case R.id.bb_menu_add:
                message += "add note";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }

    public void focusCurrentLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MyMap.this, "Location permission is not available for google map api", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }
    }
    public void callEvent(int menuItemId){
        switch (menuItemId) {
            case R.id.bb_menu_my_loc:
                focusCurrentLoc();
                break;
        }
    }
}
