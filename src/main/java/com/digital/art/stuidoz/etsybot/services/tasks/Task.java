package com.digital.art.stuidoz.etsybot.services.tasks;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.HTTPClient;

public abstract class Task {
    protected Task next;

    public Task connectWith(Task next){
        return (this.next = next);
    }

    public boolean next(Listing listing, HTTPClient client, String input){
        return next != null ? next.perform(listing, client, input) : true;
    }

    public abstract boolean perform(Listing listing, HTTPClient client, String input);
}
