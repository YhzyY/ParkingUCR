package com.example.yiy.parkingucr;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
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

    //some code from Atik Hasan: https://www.youtube.com/watch?v=n2zuIcJblUo
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

        mMap.clear();
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
        showLots();

        double Lat[] = {33.970106, 33.969962, 33.981603, 33.978089, 33.969771, 33.975179};
        double Lon[] = {-117.33114, -117.33186, -117.334914, -117.330567, -117.327454, -117.320979};

        double toLot32 = getDistance(CurrLat,CurrLon,33.970106, -117.33114);
        double toLot30 = getDistance(CurrLat,CurrLon,33.969962, -117.33186);
        double toLot26 = getDistance(CurrLat,CurrLon,33.981603, -117.334914);
        double toLot24 = getDistance(CurrLat,CurrLon,33.978089, -117.330567);
        double toLot6 = getDistance(CurrLat,CurrLon,33.969771, -117.327454);
        double toLotBigS = getDistance(CurrLat,CurrLon,33.975179, -117.320979);

        double toLot[] = {toLot32,toLot30,toLot26,toLot24,toLot6,toLotBigS };
        String Lot[] = {"Lot32","Lot30","Lot26","Lot24","Lot6","Big Spring Structure" };
        int i;
        int minindex = 0;
        double mindistance = toLot32;
        for (i = 0; i<6; i++ ){
            if (mindistance < toLot[i]){
            }else{
                minindex = i;
                mindistance = toLot[i];
                System.out.println("更小的值出现 " + i + "  " + Lot[minindex]);
            }
        }
        String ClosestLot = Lot[minindex];
        Toast.makeText(this, "The closest parking lot is " + ClosestLot, Toast.LENGTH_SHORT).show();

        //launch Google Map
        String navigation = "https://www.google.com/maps/dir/?api=1&destination="+Lat[minindex] + "," + Lon[minindex];
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(navigation));
        //Suppressing selection dialog
        if (isAppInstalled("com.google.android.apps.maps")) {
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        }
        startActivity(intent);
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
        showLots();
    }

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s*1000;
        return s;
    }

    public void showLots(){
        float zoomLevel = 14.0f;

        // Add a marker and move the camera
        LatLng Loc_Lot32 = new LatLng(33.970106, -117.33114);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot32).title("Lot32"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc_Lot32, zoomLevel));

        LatLng Loc_Lot30 = new LatLng(33.969962, -117.33186);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot30).title("Lot30"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc_Lot30, zoomLevel));

        LatLng Loc_Lot26 = new LatLng(33.981603, -117.334914);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot26).title("Lot26"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc_Lot26, zoomLevel));

        LatLng Loc_Lot6 = new LatLng(33.969771, -117.327454);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot6).title("Lot6"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc_Lot6, zoomLevel));

        LatLng Loc_BigSprings = new LatLng(33.975179, -117.320979);
        mMap.addMarker(new MarkerOptions().position(Loc_BigSprings).title("Big Springs Road Parking Lot"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc_BigSprings, zoomLevel));

        LatLng Loc_Lot24 = new LatLng(33.978089, -117.330567);
        mMap.addMarker(new MarkerOptions().position(Loc_Lot24).title("Lot24"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc_Lot24, zoomLevel));
    }

    // helper function to check if Maps is installed
    //code from https://stackoverflow.com/questions/6560345/suppressing-google-maps-intent-selection-dialog
    private boolean isAppInstalled(String uri) {
        PackageManager pm = getApplicationContext().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
