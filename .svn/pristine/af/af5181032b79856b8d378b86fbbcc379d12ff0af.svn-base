package com.skl.ipc.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.codec.binary.StringUtils;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response implements Comparable<Response> {
	@XmlTransient
	private String id;
	@XmlAttribute(name="uri")
	private String uri;
	@XmlAttribute(name="method")
	private String method;
	@XmlValue
	private String content;
	
	
	public void initId() {
		id = uri + "-" + method;
	}
	
	
	@Override
	public int compareTo(Response o) {
		if(!StringUtils.equals(uri, o.uri)) {
			return uri.compareTo(o.uri);
		}
		else {
			return method.compareTo(o.method);
		}
	}




	public String getId() {
		return id;
	}
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		if(uri != null) {
			uri = uri.trim();
			uri = uri.endsWith("/") ? uri.substring(0, uri.length()-1) : uri;
		}
		this.uri = uri;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
