package com.skl.ipc.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;

import com.skl.ipc.encode.IPCHeader;
import com.skl.ipc.encode.IPCHttpInfo;
import com.skl.ipc.encode.IPCRequest;
import com.skl.ipc.encode.IPCResponse;
import com.skl.ipc.response.Response;
import com.skl.ipc.response.ResponseManager;
import com.skl.ipc.response.Responses;

@SuppressWarnings("serial")
public class IPCServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/xml;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        IPCRequest req = null;
        try {
			req = parseRequest(request);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        String uri = request.getRequestURI().substring("/list".length()-1);
        String method = req.getHttpInfo().getMethod();
        Responses responses = ResponseManager.getResponses();
        Response resp = responses.getResponse(uri, method);
        String xml = "";
        if(resp == null) {
        	xml = "<ResponseStatus><statusCode>-200</statusCode><statusString>no content</statusString></ResponseStatus>";
        }
        else {
        	xml = resp.getContent();
        }
        try {
        	xml = encodeText(xml, request);
        } catch (JAXBException ex) {
        	ex.printStackTrace();
        }
        response.getWriter().write(xml);
	}

	
    private String encodeText(String text, HttpServletRequest request) throws PropertyException, JAXBException {
        IPCResponse response = new IPCResponse();
        
        IPCHttpInfo httpInfo = new IPCHttpInfo();
        httpInfo.setHttpVersion(request.getProtocol());
        httpInfo.setMethod(request.getMethod());
        httpInfo.setUri(request.getRequestURI());
        response.setHttpInfo(httpInfo);
        
        response.getHeaders().add(new IPCHeader("Content-Type", "application/xml;charset=utf-8"));
        response.setBody(Base64.encodeBase64String(text.getBytes(Charset.forName("UTF-8"))));
        
        JAXBContext context = JAXBContext.newInstance(response.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        StringWriter writer = new StringWriter();
        marshaller.marshal(response, writer);
        return writer.toString();
    }
	
    
    private IPCRequest parseRequest(HttpServletRequest request) throws JAXBException, IOException {
    	InputStream is = request.getInputStream();
        JAXBContext context = JAXBContext.newInstance(IPCRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        IPCRequest req = (IPCRequest)unmarshaller.unmarshal(is);
        return req;
    }
}
