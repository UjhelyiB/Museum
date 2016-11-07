package hu.bme.museum;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hu.bme.museum.model.PieceOfArt;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends TabFragment implements OnMapReadyCallback {

    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION_PERM = 267;
    private static final long LOCATION_REFRESH_TIME = 5000;
    private static final float LOCATION_REFRESH_DISTANCE = 10;

    private GoogleMap map;

    Marker userMarker;
    private ArrayList<PieceOfArt> piecesOfArt = new ArrayList<>();
    LocationManager locationManager;
    private static View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!requestNeededPermission()) {
                return rootView;
            }

        }

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

        return rootView;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            updateMapWithLocation(location);

            if(userMarker != null){
                userMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

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

        updateMapWithLocation(getCurrentLocation());

        loadPieces();

        addMarkers();
    }

    private void addMarkers() {
        Location userLocation = getCurrentLocation();
        if (userLocation != null) {
            userMarker = map.addMarker(new MarkerOptions().position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.user_marker_icon)))
                    .title("You"));
        }


        for (int i = 0; i < piecesOfArt.size(); i++) {
            map.addMarker(new MarkerOptions()
                    .position(piecesOfArt.get(i).getPosition())
                    //.icon(BitmapDescriptorFactory.fromResource(piecesOfArt.get(i).getPicture()))
                    .title(piecesOfArt.get(i).getName()));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    //TODO
    //load from DB
    public void loadPieces() {
        piecesOfArt.add(new PieceOfArt("House", null, new LatLng(getCurrentLocation().getLatitude()+0.003, getCurrentLocation().getLongitude()+0.001), "This is a house.", "Somebody"));
        piecesOfArt.add(new PieceOfArt("Tractor", null, new LatLng(getCurrentLocation().getLatitude()-0.003, getCurrentLocation().getLongitude()+0.001), "This a a tractor.", "Somebody Else"));
    }


    public Location getCurrentLocation(){
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!requestNeededPermission()) {
                return null;
            }
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    public void updateMapWithLocation(Location location) {
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                    location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to north
                    .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public boolean requestNeededPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_ACCESS_FINE_LOCATION_PERM);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public String getTabTitle() {
        return "Map";
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
