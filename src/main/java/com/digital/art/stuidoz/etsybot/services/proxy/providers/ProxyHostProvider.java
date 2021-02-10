package com.digital.art.stuidoz.etsybot.services.proxy.providers;

import java.util.Map;

public interface ProxyHostProvider {

    Map<String, Integer> remoteHosts();
    void updateHosts();
}
