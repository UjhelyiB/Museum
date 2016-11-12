package hu.bme.museum.model;

import com.google.android.gms.maps.model.LatLng;

public class PieceOfArt {

    public String name;
    public String author;
    public String description;
    public String imageLink;
    public LatLng position;
    public String date;
    public String exhibition_id;


    public PieceOfArt(){

    }

    public PieceOfArt(String name, String imageLink, LatLng position, String description, String author) {
        this.name = name;
        this.imageLink = imageLink;
        this.position = position;
        this.description = description;
        this.author = author;
    }
}
