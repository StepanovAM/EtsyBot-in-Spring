package com.digital.art.stuidoz.etsybot.services;

import com.digital.art.stuidoz.etsybot.models.ProxyHost;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class HTTPClient {
    private HttpURLConnection connection;
    private Proxy webProxy;
    private Map<String, String> sessCookies = new HashMap<>();

    public HTTPClient() {}

    public HTTPClient(ProxyHost proxyHost, String type) {
        switch(type) {
            case "HTTP": webProxy  = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost.getIp(), proxyHost.getPort())); break;
            case "SOCKS": webProxy  = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost.getIp(), proxyHost.getPort())); break;
            default: webProxy  = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost.getIp(), proxyHost.getPort())); break;
        }
    }

    public void openConnectionProxy(String destUrl) throws MalformedURLException, IOException {
        connection = (HttpURLConnection) new URL(destUrl).openConnection(webProxy);
    }

    public void openSecuredConnectionProxy(String destUrl) throws MalformedURLException, IOException {
        connection = (HttpsURLConnection) new URL(destUrl).openConnection(webProxy);
    }

    public void openConnection(String destUrl) throws MalformedURLException, IOException {
        connection = (HttpURLConnection) new URL(destUrl).openConnection();
    }

    public void openSecuredConnection(String destUrl) throws MalformedURLException, IOException {
        connection = (HttpsURLConnection) new URL(destUrl).openConnection();
    }

    public void setHTTPMethod(String method) throws ProtocolException {
        connection.setRequestMethod(method);
    }

    public boolean usingProxy() {
        return connection.usingProxy();
    }

    public void setCookiesAutomatically() {
        StringBuilder sb = new StringBuilder();
        sessCookies.forEach((key, value) -> sb.append(key + "=" + value + "; "));
        if(sb.length() > 0) connection.setRequestProperty("Cookie", sb.toString());
    }

    public void setHeader(String key, String value) {
        connection.setRequestProperty(key, value);
    }

    public void connect() throws IOException {connection.connect(); }

    public int getResponseCode() throws IOException { return connection.getResponseCode(); }

    public List<String> separateResponseCookieFromMeta(){
        Map<String,List<String>> headers = connection.getHeaderFields();
        List<String> localCookies = headers.get("set-cookie") == null ? headers.get("Set-Cookie") : headers.get("set-cookie");

        return localCookies != null ? localCookies.parallelStream().map(str -> str.split(";")[0]).collect(Collectors.toList()) : Collections.emptyList();
    }

    public void setDefaultOptions(String method) throws Exception {
        setHeader("User-Agent", "Mozilla/5.0");
        setHeader("Connection", "Keep-Alive");
        setHTTPMethod(method);
    }

    public StringBuilder readHTTPBodyResponse() throws IOException {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line + System.lineSeparator());
            }
            return response;
        }
    }

    public boolean writeHTTPBodyRequest(String body)  {
        connection.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(body);
            wr.flush();
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        connection.disconnect();
    }

    public Map<String, String> getSessCokies() {
        return sessCookies;
    }


    public String getProxyInfo(){
        return webProxy.toString();
    }

}
