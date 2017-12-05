package com.example.amitava.moviestalker.Utilities;

/**
 * Created by Amitava on 01-Dec-17.
 */

public class GridItem {
    String title, image, rating, releaseDate, overview;

    public void setTitle(String title){
        this.title = title;
    }

    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return image;
    }

    public String getTitle(){
        return title;
    }

    public void setRating(String rating){ this.rating = rating; }
    public String getRating(){ return rating; }

    public void  setReleaseDate(String releaseDate){ this.releaseDate = releaseDate; }
    public String getReleaseDate(){ return releaseDate; }

    public void setOverview(String overview){ this.overview = overview; }
    public String getOverview(){ return overview; }
}
