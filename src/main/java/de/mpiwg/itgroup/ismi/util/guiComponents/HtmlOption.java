package de.mpiwg.itgroup.ismi.util.guiComponents;

import org.apache.commons.lang.StringUtils;

public class HtmlOption {

	private String value;
	private String label;
	private String style;
	
	public HtmlOption(String value, String label){
		this.value = value;
		this.label = label;
	}
	
	public HtmlOption(String value, String label, String style){
		this.value = value;
		this.label = label;
		this.style = style;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	
	public String getHtml(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<option value=\""+ value +"\" ");
		if(StringUtils.isNotEmpty(style)){
			sb.append("style=\"" + style + "\"");
		}
		sb.append(">");
		sb.append("</option>");
		
		return sb.toString();
	}	
}
