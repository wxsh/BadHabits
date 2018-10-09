package model;
//import no.hiof.andrekar.badhabits.R;

import java.util.Date;

public class Habit {
    //We need a title and description for our main class.
    private String title;
    private String description;
    //Favourite boolean?
    private boolean isFavourite;

    //Start date?
    private Date startDate;


    //Constructor
    public Habit(String title, String description, Date startDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.isFavourite = false;
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }


    public boolean getIsFavourite() {
        return isFavourite;
    }


    //Setters

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavourite(boolean isFavourite) {
        //Might want to have this as a toggle instead.
        this.isFavourite = isFavourite;
    }
}
