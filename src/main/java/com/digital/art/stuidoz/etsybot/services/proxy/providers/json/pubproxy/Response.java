package com.digital.art.stuidoz.etsybot.services.proxy.providers.json.pubproxy;

import java.util.List;

public class Response {
	
	private List<Data> data;
	private int count;

	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
