package com.digital.art.stuidoz.etsybot.models;

import java.util.List;

public class Listing {

    private String id;
    private List<String> tags;

    public String getId() { return id;}

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Listing(String id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }
}
