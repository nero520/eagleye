package com.yougou.eagleye.admin.domain;

import com.yougou.eagleye.trace.domain.Span;

public class EagleyeSpan{

	private String uuid;
	
	private Span span;

	public Span getSpan() {
		return span;
	}

	public void setSpan(Span span) {
		this.span = span;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
