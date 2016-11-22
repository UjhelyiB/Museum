package hu.bme.museum.fragments.map.marker_clustering;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.artwork.ArtworkDetailsFragment;
import hu.bme.museum.fragments.map.MapFragment;
import hu.bme.museum.model.Artwork;

import static java.security.AccessController.getContext;


public class MuseumClusterManager extends ClusterManager
        implements
        ClusterManager.OnClusterItemClickListener<ArtworkMarkerItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<ArtworkMarkerItem>,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private static final int IMAGE_WIDTH = 200;
    private static final int IMAGE_HEIGHT = 200;
    private GoogleMap map;
    private MapFragment mapFragment;
    private List<Artwork> piecesOfArt;

    public MuseumClusterManager(Context context, GoogleMap map, MapFragment mapFragment) {
        super(context, map);
        this.map = map;
        this.mapFragment = mapFragment;

        setupListeners();

    }

    private void setupListeners() {
        map.setInfoWindowAdapter(this);

//        this.getClusterMarkerCollection().setOnInfoWindowAdapter(this);
//        this.getMarkerCollection().setOnInfoWindowAdapter(this);

        map.setOnMarkerClickListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnInfoWindowClickListener(this);


        piecesOfArt = FirebaseAdapter.getInstance().getAllArtworksForClusterManager(this);
    }

    public void populateMuseumClusterManager(){

        for(int i=0; i< piecesOfArt.size(); i++){
            Artwork currentArtwork = piecesOfArt.get(i);

            ArtworkMarkerItem newArtworkMarkerItem = new ArtworkMarkerItem(currentArtwork.name, currentArtwork.lat, currentArtwork.lng);

            addItem(newArtworkMarkerItem);
        }
        this.cluster();

    }


    @Override
    public boolean onClusterItemClick(ArtworkMarkerItem artworkMarkerItem) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ArtworkMarkerItem artworkMarkerItem) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        View window = mapFragment.getLayoutInflater(null).inflate(R.layout.marker, null);

        ImageView im = (ImageView) window.findViewById(R.id.markerIcon);
        TextView tv = (TextView) window.findViewById(R.id.markerTitle);

        for(int i=0; i< piecesOfArt.size(); i++){
            if(piecesOfArt.get(i).lat == marker.getPosition().latitude && piecesOfArt.get(i).lng == marker.getPosition().longitude){
                try {

                    Bitmap image = Ion.with(mapFragment.getContext()).load(piecesOfArt.get(i).imageLink).asBitmap().get();
                    Bitmap resizedImage = Bitmap.createScaledBitmap(image, IMAGE_WIDTH, IMAGE_HEIGHT, false);
                    im.setImageBitmap(resizedImage);
                    tv.setText(piecesOfArt.get(i).name);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        return window;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        super.onInfoWindowClick(marker);
        for(int i=0; i< piecesOfArt.size(); i++){
            if(piecesOfArt.get(i).lat == marker.getPosition().latitude && piecesOfArt.get(i).lng == marker.getPosition().longitude){
                ArtworkDetailsFragment artworkDetailsFragment =
                        new ArtworkDetailsFragment();
                artworkDetailsFragment.setArtwork(piecesOfArt.get(i));

                mapFragment.getFragmentManager().beginTransaction()
                        .replace(R.id.map, artworkDetailsFragment)
                        .addToBackStack(null).commit();
            }
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
