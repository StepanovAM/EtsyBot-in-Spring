package com.digital.art.stuidoz.etsybot.services.tasks;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.HTTPClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public class FoundBySearchTask extends Task {

    private final String ETSY_HOME = "https://www.etsy.com/";
    private final String ETSY_SESS_UAID = "uaid";

    public boolean perform(Listing listing, HTTPClient client, String next) {
        try {
            viewPage(client, ETSY_HOME);

            client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));
            client.disconnect();

            String id = listing.getId();
            List<String> tags = listing.getTags();

            for(String tag : tags){
                try {
                    String correctTag = tag.replace(" ", "%20");
                    String href = ETSY_HOME + "search?q=" + correctTag;
                    String uri = "/listing/" + id;
                    String html;

                    client.getSessCokies().put("search_options", "{\"prev_search_term\":\"" + correctTag + "\",\"item_language\":null,\"language_carousel\":null}");
                    do {
                        html = performEtsySearch(client, href);
                        href = parseListingOnSearchResult(html, id);

                        client.disconnect();
                        client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));
                    }while(href.length() != 0 && !href.contains(uri));

                    if(href == null || href.length() == 0)
                        throw new IllegalArgumentException("Не удалось найти листинг по заданному тэгу");

                    next = href;
                    viewPage(client, href);
                }catch(Exception e) {
                    // TODO process it
                }
            }
        }catch(Exception e) {
            // TODO process it
//            System.out.println("PROXY IS NOT AVAILABLE: " + client.getProxyInfo() + " " + Thread.currentThread().getName());
            return false;
        }

        return next(listing, client, next);
    }

    private String performEtsySearch(HTTPClient client, String url) throws Exception {
        client.openSecuredConnectionProxy(url);
        client.setDefaultOptions("GET");
        client.setCookiesAutomatically();
        return client.readHTTPBodyResponse().toString();
    }

    private String parseListingOnSearchResult(String html, String id) {
        Document doc = Jsoup.parse(html);

        Element div = doc.getElementsByAttributeValue("data-listing-id", id).first();
        if(div!=null)
            return div.select("a").first().attr("href");

        Elements elements = doc.select("[href*=" + "/listing/" + id + "]");
        if(elements!=null && elements.size() > 0)
            return elements.attr("href");

        Element li = doc.getElementsByAttributeValue("aria-label", "Review Page Results").last().select("li").last();
        return  li.select("a").first().attr("href");
    }

    private void viewPage(HTTPClient client, String... pages) throws Exception {
        for(String destUrl : pages) {
            client.openSecuredConnectionProxy(destUrl);
            client.setDefaultOptions("GET");
            client.setCookiesAutomatically();
            client.readHTTPBodyResponse();
        }
    }

}
