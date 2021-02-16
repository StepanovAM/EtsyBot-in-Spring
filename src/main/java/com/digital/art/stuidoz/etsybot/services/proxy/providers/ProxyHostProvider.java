package com.digital.art.stuidoz.etsybot.services.proxy.providers;

import com.digital.art.stuidoz.etsybot.models.ProxyHost;

import java.util.List;
import java.util.Map;

public interface ProxyHostProvider {

    List<ProxyHost> remoteHosts();
    void updateHosts();
}
