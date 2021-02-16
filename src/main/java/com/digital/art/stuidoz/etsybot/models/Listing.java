package com.digital.art.stuidoz.etsybot.models;


import com.digital.art.stuidoz.etsybot.services.tasks.TaskType;

import java.util.List;

public class Listing {

    private String id;
    private List<String> tags;
    private TaskType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Listing(String id, List<String> tags, TaskType type) {
        this.id = id;
        this.tags = tags;
        this.type = type;
    }
}
