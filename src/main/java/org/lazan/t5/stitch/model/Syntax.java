package org.lazan.t5.stitch.model;

public enum Syntax {
	JAVA("lang-java"),
	JAVASCRIPT("lang-js"),
	XML("lang-xml");

	private String cssClass;
	
	private Syntax(String cssClass) {
		this.cssClass = cssClass;
	}
	
	public String getCssClass() {
		return cssClass;
	}
}
