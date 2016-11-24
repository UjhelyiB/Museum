package hu.bme.museum.fragments.map.marker_clustering;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.concurrent.ExecutionException;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.artwork.ArtworkDetailsFragment;
import hu.bme.museum.fragments.map.MapFragment;
import hu.bme.museum.model.Artwork;

public class MuseumClusterManager<ClusterItem extends ArtworkMarkerItem> extends ClusterManager
        implements
        ClusterManager.OnClusterItemClickListener<ArtworkMarkerItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<ArtworkMarkerItem>,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener,
        ClusterManager.OnClusterClickListener<ArtworkMarkerItem> {

    private static int IMAGE_WIDTH;
    private static int IMAGE_HEIGHT;
    private GoogleMap map;
    private MapFragment mapFragment;
    private List<Artwork> piecesOfArt;
    private ArtworkMarkerItem clickedArtworkMarkerItem;

    public MuseumClusterManager(Context context, GoogleMap map, MapFragment mapFragment) {
        super(context, map);

        DisplayMetrics metrics = new DisplayMetrics();
        mapFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        IMAGE_WIDTH = metrics.heightPixels/5;
        IMAGE_HEIGHT = metrics.heightPixels/5;


        this.map = map;
        this.mapFragment = mapFragment;

        setupListeners();

    }

    private void setupListeners() {
        map.setInfoWindowAdapter(this);
        map.setOnMarkerClickListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnInfoWindowClickListener(this);

        setOnClusterItemClickListener(this);
        setOnClusterItemInfoWindowClickListener(this);
        setOnClusterClickListener(this);

        piecesOfArt = FirebaseAdapter.getInstance().getAllArtworksForClusterManager(this);
    }

    public void populateMuseumClusterManager(){

        for(int i=0; i< piecesOfArt.size(); i++){
            Artwork currentArtwork = piecesOfArt.get(i);

            ArtworkMarkerItem newArtworkMarkerItem = new ArtworkMarkerItem(currentArtwork.name, currentArtwork.imageLink, currentArtwork.lat, currentArtwork.lng);

            addItem(newArtworkMarkerItem);
        }
        this.cluster();

    }


    @Override
    public boolean onClusterItemClick(ArtworkMarkerItem artworkMarkerItem) {
        clickedArtworkMarkerItem = artworkMarkerItem;
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ArtworkMarkerItem artworkMarkerItem) {
        for(int i=0; i< piecesOfArt.size(); i++) {
            if (piecesOfArt.get(i).name == artworkMarkerItem.getTitle()) {
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
    public View getInfoWindow(Marker marker) {
        View window = mapFragment.getLayoutInflater(null).inflate(R.layout.marker, null);

        ImageView im = (ImageView) window.findViewById(R.id.markerIcon);
        TextView tv = (TextView) window.findViewById(R.id.markerTitle);

        if (clickedArtworkMarkerItem != null) {
                try {
                        Bitmap image = Ion.with(mapFragment.getContext()).load(clickedArtworkMarkerItem.getImageLink()).asBitmap().get();
                        Bitmap resizedImage = Bitmap.createScaledBitmap(image, IMAGE_WIDTH, IMAGE_HEIGHT, false);
                        im.setImageBitmap(resizedImage);
                        tv.setText(clickedArtworkMarkerItem.getTitle());

                    clickedArtworkMarkerItem = null;
                    return window;

                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }catch(ExecutionException e){
                        e.printStackTrace();
                    }
                }

        return null;


    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onClusterClick(Cluster<ArtworkMarkerItem> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ArtworkMarkerItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

        return true;
    }
}
