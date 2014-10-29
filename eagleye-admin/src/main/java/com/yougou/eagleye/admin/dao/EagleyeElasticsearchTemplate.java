package com.yougou.eagleye.admin.dao;

import org.elasticsearch.client.Client;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

public class EagleyeElasticsearchTemplate extends ElasticsearchTemplate{
	
	private Client client;
	
	public EagleyeElasticsearchTemplate(Client client) {
		super(client);
		// TODO Auto-generated constructor stub
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	
	
	

}
