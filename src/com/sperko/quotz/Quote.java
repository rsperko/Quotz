package com.sperko.quotz;

public class Quote {
	private int id;
	private String text;
	private String source;
	
	public Quote(int id, String text, String source) {
		this.id = id;
		this.text = text;
		this.source = source;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String toString() {
		return text;
	}
}
