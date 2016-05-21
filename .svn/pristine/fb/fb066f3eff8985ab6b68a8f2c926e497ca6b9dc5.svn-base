package com.skl.ipc.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ResponseManager {
	private static JAXBContext jc = null;
	static {
		try {
			jc = JAXBContext.newInstance(Responses.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static void updateResponses(Responses responses) {
		File file = getResponseFile();
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			Collections.sort(responses.getResponses());
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.marshal(responses, new OutputStreamWriter(os, Charset.forName("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {}
			}
		}
	}

	public static Responses getResponses() {
		File responseFile = getResponseFile();
		Responses responses = null;
		InputStream is = null;
		try {
			is = new FileInputStream(responseFile);
			JAXBContext jc = JAXBContext.newInstance(Responses.class);
			Unmarshaller u = jc.createUnmarshaller();
			responses = (Responses)u.unmarshal(new InputStreamReader(is, Charset.forName("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {}
			}
		}
		if(responses != null) {
			for (Response response : responses.getResponses()) {
				response.initId();
			}
		}
		return responses;
	}
	
	private static File getResponseFile() {
		String workDir = System.getProperty("user.dir");
		File responseFile = new File(workDir, "responses.xml");
		if (!responseFile.exists()) {
			throw new IllegalStateException("responses.xml 文件不存在");
		}
		return responseFile;
	}

}
