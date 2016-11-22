package hu.bme.museum.fragments.map.marker_clustering;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.concurrent.ExecutionException;

import hu.bme.museum.R;
import hu.bme.museum.fragments.artwork.ArtworkDetailsFragment;
import hu.bme.museum.fragments.map.MapFragment;
import hu.bme.museum.model.Artwork;

import static java.security.AccessController.getContext;


public class MuseumClusterManager extends ClusterManager
        implements
        ClusterManager.OnClusterItemClickListener<ArtworkMarkerItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<ArtworkMarkerItem>,
        GoogleMap.InfoWindowAdapter {

    private static final int IMAGE_WIDTH = 200;
    private static final int IMAGE_HEIGHT = 200;
    private GoogleMap map;
    private MapFragment mapFragment;
    private List<Artwork> piecesOfArt;

    public MuseumClusterManager(Context context, GoogleMap map, MapFragment mapFragment) {
        super(context, map);
        this.map = map;
        this.mapFragment = mapFragment;
        this.piecesOfArt = mapFragment.getPiecesOfArt();

        setupListeners();

    }

    private void setupListeners() {
        map.setInfoWindowAdapter(this);
    }

    public MuseumClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
    }



    @Override
    public boolean onClusterItemClick(ArtworkMarkerItem artworkMarkerItem) {
        for(int i=0; i< piecesOfArt.size(); i++){
            if(piecesOfArt.get(i).name.equals(artworkMarkerItem.getTitle())){
                ArtworkDetailsFragment artworkDetailsFragment =
                        new ArtworkDetailsFragment();
                artworkDetailsFragment.setArtwork(piecesOfArt.get(i));

                mapFragment.getFragmentManager().beginTransaction()
                        .replace(R.id.map, artworkDetailsFragment)
                        .addToBackStack(null).commit();
            }
        }
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
            if(piecesOfArt.get(i).name.equals(marker.getTitle())){
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
    public View getInfoContents(Marker marker) {
        return null;
    }
}
