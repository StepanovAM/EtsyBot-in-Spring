package com.digital.art.stuidoz.etsybot.services.tasks;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.HTTPClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class AddingToCartTask extends Task {

    private final String ETSY_CART = "https://www.etsy.com/cart/listing.php";

    public boolean perform(Listing listing, HTTPClient client, String input) {
        try{
            client.openSecuredConnectionProxy(input);
            client.setDefaultOptions("GET");
            client.setCookiesAutomatically();
            client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));

            String html = client.readHTTPBodyResponse().toString();
            String params = parseAddingToCartPOSTForm(html);

            client.disconnect();

            client.openSecuredConnectionProxy(ETSY_CART);
            client.setDefaultOptions("POST");
            client.setCookiesAutomatically();
            client.writeHTTPBodyRequest(params);

            System.out.println("OK: " + client.getResponseCode() + ", using proxy? " + client.usingProxy());

            client.disconnect();
        }catch (Exception e){
            // TODO process it
            return false;
        }
        return true;
    }

    private String parseAddingToCartPOSTForm(String html) {
        Element form = Jsoup.parse(html).getElementsByClass("add-to-cart-form").first();
        return form.getElementsByTag("input").parallelStream().map(input -> input.attr("name") + "=" + input.val()).collect(Collectors.joining("&"));
    }



}
