package com.example.h_mal.mochee.Fragment4_Parts;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by h_mal on 04/01/2018.
 */

@IgnoreExtraProperties
public class InspirationItems {

    private String description;
    private String imageURL;
    private String name;
    private String subname;

    public InspirationItems() {
    }

    public InspirationItems(String description, String imageURL, String name, String subname) {
        this.description = description;
        this.imageURL = imageURL;
        this.name = name;
        this.subname = subname;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }
}
