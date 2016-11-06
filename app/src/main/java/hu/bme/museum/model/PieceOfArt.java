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

    public LatLng getPosition(){
        return position;
    }

    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return author;
    }

    public String getName(){
        return name;
    }

    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }

    public String getPicture() {
        return imageLink;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription() { return description; }

    public void setDate(String date){this.date = date;}

    public String getDate(){return date;}
}
