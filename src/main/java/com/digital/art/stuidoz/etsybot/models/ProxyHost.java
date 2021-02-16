package com.digital.art.stuidoz.etsybot.models;

public class ProxyHost {

    private String ip;
    private Integer port;

    public ProxyHost(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
