package hu.bme.museum.fragments.map.marker_clustering;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ArtworkMarkerItem implements ClusterItem {
    private final LatLng mPosition;
    private String title;

    public ArtworkMarkerItem(String title, double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        this.title = title;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return title;
    }
}
