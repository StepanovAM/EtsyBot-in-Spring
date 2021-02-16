package com.digital.art.stuidoz.etsybot.services.proxy.providers;

import java.util.List;

import com.digital.art.stuidoz.etsybot.models.ProxyHost;
import com.digital.art.stuidoz.etsybot.services.proxy.providers.json.foxtools.ResponseJSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class FoxToolsProxyProvider extends ProxyProvider {
	
	private ResponseJSON response;

	@Override
	public void parseResponse(StringBuilder json) {
		try {
			System.out.println(json);
			response = new ObjectMapper().readValue(json.toString(), FoxToolsProxyProvider.class).getResponse();
			response.getItems().forEach(result -> hosts.add(new ProxyHost(result.getIp(), result.getPort())));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getUrls() {
		return List.of("http://api.foxtools.ru/v2/Proxy",
				"http://api.foxtools.ru/v2/Proxy?page=2");
	}

	public ResponseJSON getResponse() {
		return response;
	}

	public void setResponse(ResponseJSON response) {
		this.response = response;
	}
}
