package com.digital.art.stuidoz.etsybot.services.proxy.providers;

import com.digital.art.stuidoz.etsybot.models.ProxyHost;
import com.digital.art.stuidoz.etsybot.services.HTTPClient;

import java.util.ArrayList;
import java.util.List;


public abstract class ProxyProvider implements ProxyHostProvider{
	
	protected List<ProxyHost> hosts = new ArrayList<>();
	
	public abstract List<String> getUrls();
	public abstract void parseResponse(StringBuilder response);
	
	public List<ProxyHost> remoteHosts(){
		return hosts;
	};
	public void setHosts(List<ProxyHost>hosts) {
		this.hosts = hosts;
	}
	public void updateHosts() {
		HTTPClient httpClient = new HTTPClient();
		getUrls().forEach(url -> {
			try {
				httpClient.openConnection(url);
				parseResponse(httpClient.readHTTPBodyResponse());
			}catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
