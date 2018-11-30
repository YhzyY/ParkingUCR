package com.example.yiy.parkingucr;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    LocationManager locationManager;
    public static String Current_Long;
    public static String Current_Lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        CheckPermission();
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Getting reference to TextView tv_longitude

        System.out.println("这里是onLocationChanged : " + location);
        // Setting Current Longitude
        Current_Long = String.valueOf(location.getLongitude());
        // Setting Current Latitude
        Current_Lat = String.valueOf(location.getLatitude());

        System.out.println("这里是具体结果：longtitude : " + Current_Long + "latitude : " + Current_Lat);

        double CurrLat = Double.valueOf(Current_Lat);
        double CurrLon = Double.valueOf(Current_Long);
        LatLng Loc_Current = new LatLng(CurrLat, CurrLon);
        mMap.addMarker(new MarkerOptions().position(Loc_Current).title("This is you current address").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Loc_Current));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
       }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Loc_Lot32 = new LatLng(33.970106, -117.33114);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot32).title("Lot32"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Loc_Lot32));

        LatLng Loc_Lot30 = new LatLng(33.969962, -117.33186);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot30).title("Lot30"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Loc_Lot30));

        LatLng Loc_Lot26 = new LatLng(33.981603, -117.334914);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot26).title("Lot26"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Loc_Lot26));

        LatLng Loc_Lot24 = new LatLng(33.978089, -117.330567);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot24).title("Lot24"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Loc_Lot24));

        LatLng Loc_Lot6 = new LatLng(33.969771, -117.327454);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot6).title("Lot6"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Loc_Lot6));

        LatLng Loc_BigSprings = new LatLng(33.975179, -117.320979);
        mMap.addMarker(new MarkerOptions().position(Loc_BigSprings).title("Big Springs Road Parking Lot"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Loc_BigSprings));

    }

}
