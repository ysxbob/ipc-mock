package com.skl.ipc.response;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.StringUtils;

@XmlRootElement(name="responses")
@XmlAccessorType(XmlAccessType.FIELD)
public class Responses {
	
	@XmlElement(name="response")
	private List<Response> responses = new ArrayList<Response>();
	
	/**
	 * @return the responses
	 */
	public List<Response> getResponses() {
		return responses;
	}

	/**
	 * @param responses the responses to set
	 */
	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}
	
	public Response getResponse(String id) {
		for (Response response : responses) {
			if(StringUtils.endsWithIgnoreCase(id, response.getId())) {
				return response;
			}
		}
		return null;
	}
	
	public Response getResponse(String uri, String method) {
		for (Response response : responses) {
			if(response.getMethod().equals(method)) {
				if(response.getUri().contains("{")) {
					// 用正则表达式去配对
					String pattern = response.getUri().replaceAll("\\{.*?\\}", "(.*?)");
					Matcher m = Pattern.compile(pattern).matcher(uri);
					if(m.matches() && response.getMethod().equals(method)) {
						return response;
					}
				}
				else if(response.getUri().equals(uri)){
					return response;
				}
			}
			else {
				continue;
			}
		}
		return null;
	}
	
}
