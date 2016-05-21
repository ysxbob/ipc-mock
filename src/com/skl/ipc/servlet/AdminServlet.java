package com.skl.ipc.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skl.ipc.response.Response;
import com.skl.ipc.response.ResponseManager;
import com.skl.ipc.response.Responses;

public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = -4099598408545728430L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String uri = request.getParameter("uri");
		String method = request.getParameter("method");
		String content = request.getParameter("content");
		String opt = request.getParameter("opt");
		if("add".equals(opt)) {
			Responses responses = ResponseManager.getResponses();
			Response resp = responses.getResponse(uri, method);
			if(resp != null) {
				request.setAttribute("message", "已存在uri[" + uri + "]method[" + method + "]的配置");
			}
			else {
				resp = new Response();
				resp.setUri(uri);
				resp.setMethod(method);
				resp.setContent(content);
				responses.getResponses().add(resp);
				ResponseManager.updateResponses(responses);
			}
		} else if("update".equals(opt)) {
			Responses responses = ResponseManager.getResponses();
			Response resp = responses.getResponse(id);
			if(resp != null) {
				resp.setUri(uri);
				resp.setMethod(method);
				resp.setContent(content);
			}
			ResponseManager.updateResponses(responses);
		} else if("del".equals(opt)) {
			Responses responses = ResponseManager.getResponses();
			Response resp = responses.getResponse(id);
			responses.getResponses().remove(resp);
			ResponseManager.updateResponses(responses);
		}
		list(request, response);
	}

	
	private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Responses responses = ResponseManager.getResponses();
		List<Response> list = responses.getResponses();
		request.setAttribute("list", list);
		request.getRequestDispatcher("/list.jsp").forward(request, response);// 成功页面
	}

}
