package com.digital.art.stuidoz.etsybot.services.proxy.providers;

import com.digital.art.stuidoz.etsybot.services.HTTPClientService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class ProxyProvider implements ProxyHostProvider{
	
	protected Map<String, Integer> hosts = new HashMap<>();
	
	public abstract List<String> getUrls();
	public abstract void parseResponse(StringBuilder response);
	
	public Map<String, Integer> remoteHosts(){
		return hosts;
	};
	public void setHosts(Map<String, Integer> hosts) {
		this.hosts = hosts;
	}
	public void updateHosts() {
		HTTPClientService httpClient = new HTTPClientService();
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
