package hu.bme.museum.model;

import com.google.android.gms.maps.model.LatLng;

public class PieceOfArt {
    private String title;
    private int picture;
    private LatLng position;

    public PieceOfArt(String title, int picture, LatLng position) {
        this.title = title;
        this.picture = picture;
        this.position = position;
    }

    public LatLng getPosition(){
        return position;
    }
    public String getTitle(){
        return title;
    }

    public int getPicture() {
        return picture;
    }
}
