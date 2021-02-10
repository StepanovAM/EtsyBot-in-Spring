package com.digital.art.stuidoz.etsybot.services.tasks;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.HTTPClientService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddingToCartTask implements Task{

    private Listing listing;
    private Map<String, Integer> hosts;

    private String ETSY_HOME = "https://www.etsy.com/";
    private String ETSY_CART = "https://www.etsy.com/cart/listing.php";
    private String ETSY_SESS_UAID = "uaid";

    public AddingToCartTask(Listing listing, Map<String, Integer> hosts) {
        this.listing = listing;
        this.hosts = hosts;
    }

    @Override
    public void run() {
        System.out.println("IN");
        hosts.forEach((ip, port) -> {
            try {
                HTTPClientService client = new HTTPClientService(ip, port, "HTTP");
                viewPage(client, ETSY_HOME);

                client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));
                client.disconnect();

                String id = listing.getId();
                List<String> tags = listing.getTags();
                tags.forEach(tag -> {
                    try {
                        String correctTag = tag.replace(" ", "%20");
                        String href = ETSY_HOME + "search?q=" + correctTag;
                        String html;

                        client.getSessCokies().put("search_options", "{\"prev_search_term\":\"" + correctTag + "\",\"item_language\":null,\"language_carousel\":null}");
                        do {
                            html = performEtsySearch(client, href);
                            href = parseListingOnSearchResult(html, id);

                            client.disconnect();
                            client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));
                            System.out.println(href + " " + ip + ":" + port);
                        }while(href.length() != 0 && !href.contains("/listing/" + id));

                        if(href == null || href.length() == 0)
                            throw new IllegalArgumentException("Не удалось найти листинг по заданному тэгу");

                        viewPage(client, href);
                        System.out.println("DONE: " + client.getSessCokies().get(ETSY_SESS_UAID) + " " + ip + ":" + port);
                    }catch(Exception e) {
                        System.out.println("NOPE: " + tag + " " + e.getMessage() + " " + ip + ":" + port);
                    }
                });
            }catch(Exception e) {
                System.out.println("PROXY IS NOT AVAILABLE: " + ip + ":" + port);
//                  removeHost(ip, port);
            }
        });
    }

    public String performEtsySearch(HTTPClientService client, String url) throws Exception {
        client.openSecuredConnectionProxy(url);
        client.setDefaultOptions("GET");
        client.setCookiesAutomatically();
        return client.readHTTPBodyResponse().toString();
    }

    public String parseListingOnSearchResult(String html, String id) {
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

    public void addToCart(HTTPClientService client, String... listing) throws Exception{
        for(String destUrl : listing) {
            client.openSecuredConnectionProxy(destUrl);
            client.setDefaultOptions("GET");
            client.setCookiesAutomatically();
            client.separateResponseCookieFromMeta().forEach(cookie -> client.getSessCokies().put(cookie.split("=")[0], cookie.split("=")[1]));

            String html = client.readHTTPBodyResponse().toString();
            String params = parseAddigToCartPOSTForm(html);

            client.disconnect();

            client.openSecuredConnectionProxy(ETSY_CART);
            client.setDefaultOptions("POST");
            client.setCookiesAutomatically();
            client.writeHTTPBodyRequest(params);

            System.out.println("OK: " + client.getResponseCode() + ", using proxy? " + client.usingProxy());

            client.disconnect();
        }
    }

    public String parseAddigToCartPOSTForm(String html) {
        Element form = Jsoup.parse(html).getElementsByClass("add-to-cart-form").first();
        return form.getElementsByTag("input").parallelStream().map(input -> input.attr("name") + "=" + input.val()).collect(Collectors.joining("&"));
    }

    public void viewPage(HTTPClientService client, String... pages) throws Exception {
        for(String destUrl : pages) {
            client.openSecuredConnectionProxy(destUrl);
            client.setDefaultOptions("GET");
            client.setCookiesAutomatically();
            client.readHTTPBodyResponse();
        }
    }
}
