package hu.bme.museum.fragments.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import hu.bme.museum.R;
import hu.bme.museum.fragments.TabFragment;
import hu.bme.museum.fragments.game.GameFragment;
import hu.bme.museum.fragments.map.marker_clustering.MuseumClusterManager;
import hu.bme.museum.model.game.VisitLocation;

public class    MapFragment extends TabFragment
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        LocationListener, GoogleApiClient.OnConnectionFailedListener{

    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION_PERM = 267;
    private static final long LOCATION_REFRESH_TIME = 2000;
    private static final float LOCATION_REFRESH_DISTANCE = 2;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private MuseumClusterManager clusterManager;
    LocationRequest locationRequest;

    private Marker userMarker;
    private static View rootView;

    public static Location userLocation;
    private VisitLocation visitLocationChallenge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REFRESH_TIME);
        locationRequest.setSmallestDisplacement(LOCATION_REFRESH_DISTANCE);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return rootView as it is */
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        googleApiClient.disconnect();
    }

    @SuppressWarnings("MissingPermission")
    protected void startLocationUpdates() {
        if(googleApiClient.isConnected()){
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
        }

    }

    protected void stopLocationUpdates() {
        if(googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        userLocation = location;

        if(userMarker != null){
            userMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }else{
            addUserMarker();
        }

        updateMapWithUserLocation();
        if(visitLocationChallenge != null && visitLocationChallenge.getGoalLocation().distanceTo(userLocation) < 6){
            visitLocationChallenge.sendAnswer(true, null);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        setUpClusterer();
    }

    private void setUpClusterer() {
        clusterManager = new MuseumClusterManager(getContext(), map, this);
    }

    private void addUserMarker(){
        if (userLocation != null) {
            userMarker = map.addMarker(new MarkerOptions().position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.user_marker_icon)))
                    .title(getString(R.string.you)));
        }
    }

    public void updateMapWithUserLocation() {
        if (userLocation != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
                    // Sets the center of the map to location user
                    .zoom(18)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to north
                    .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void requestNeededPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION_PERM);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case REQUEST_CODE_ACCESS_FINE_LOCATION_PERM:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();

                    addUserMarker();
                    updateMapWithUserLocation();
                }
        }
    }

    @Override
    public String getTabTitle() {
        return "Map";
    }

    private Bitmap getBitmapFromDrawable(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestNeededPermission();

        }else{
            userLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            addUserMarker();
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Map", "Connection Failed!");
        Toast.makeText(getActivity(), "Connection Failed!", Toast.LENGTH_SHORT).show();
    }

    public void setvisitLocationChallenge(VisitLocation visitLocationChallenge){
        this.visitLocationChallenge = visitLocationChallenge;
    }
}
