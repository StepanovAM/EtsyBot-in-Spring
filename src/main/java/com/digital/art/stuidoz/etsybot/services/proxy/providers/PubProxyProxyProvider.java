package com.digital.art.stuidoz.etsybot.services.proxy.providers;

import com.digital.art.stuidoz.etsybot.models.ProxyHost;
import com.digital.art.stuidoz.etsybot.services.proxy.providers.json.pubproxy.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

public class PubProxyProxyProvider extends ProxyProvider{
	
	@Override
	public List<String> getUrls() {
		return List.of("http://pubproxy.com/api/proxy?limit=20",
				"http://pubproxy.com/api/proxy?limit=20");
	}

	@Override
	public void parseResponse(StringBuilder json) {
		try {
			Response response = new ObjectMapper().readValue(json.toString(), Response.class);
			response.getData().forEach(data -> hosts.add(new ProxyHost(data.getIp(), data.getPort())));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
