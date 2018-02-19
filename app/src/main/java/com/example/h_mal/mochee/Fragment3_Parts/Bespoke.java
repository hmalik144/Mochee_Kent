package com.example.h_mal.mochee.Fragment3_Parts;

/**
 * Created by h_mal on 04/01/2018.
 */

public class Bespoke {

    private Integer logo;

    private Integer BackgroundColour;

    private String title;

    private Integer titleTextColour;

    private String description;

    private Integer descriptionTextColour;

    public Bespoke(Integer logo, Integer backgroundColour, String title, Integer titleTextColour,
                   String description, Integer descriptionTextColour) {
        this.logo = logo;
        this.BackgroundColour = backgroundColour;
        this.title = title;
        this.titleTextColour = titleTextColour;
        this.description = description;
        this.descriptionTextColour = descriptionTextColour;
    }

    public Integer getLogo() {
        return logo;
    }

    public Integer getBackgroundColour() {
        return BackgroundColour;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTitleTextColour() {
        return titleTextColour;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDescriptionTextColour() {
        return descriptionTextColour;
    }
}
