package com.digital.art.stuidoz.etsybot.services.proxy;

import com.digital.art.stuidoz.etsybot.models.ProxyHost;
import com.digital.art.stuidoz.etsybot.services.proxy.providers.ProxyHostProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProxyHostsWorker {

    private List<ProxyHostProvider> proxyProviders;
    private List<ProxyHost> remoteHosts;

    public ProxyHostsWorker(List<ProxyHostProvider> proxyProviders) {
        this.proxyProviders = proxyProviders;
    }

    @PostConstruct
    public void init(){
        retrieveProxyHosts();
    }

    @Async
    public void retrieveProxyHosts(){
        remoteHosts = proxyProviders.stream()
                .peek(proxyProvider -> proxyProvider.updateHosts())
                .flatMap(proxyProvider -> proxyProvider.remoteHosts().stream())
                .collect(Collectors.toList());
    }

    public List<ProxyHost> getRemoteHosts() {
        return remoteHosts;
    }
}