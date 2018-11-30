package com.example.yiy.parkingucr;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
