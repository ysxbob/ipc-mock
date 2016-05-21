package com.skl.ipc.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodeFilter implements Filter {
	protected String encoding = null;
	protected FilterConfig filterConfig = null;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String encoding = this.encoding;
		if (encoding != null) {
			request.setCharacterEncoding(encoding);
		} else {
			request.setCharacterEncoding("UTF-8");
		}
		chain.doFilter(request, response);// 传递过滤链

	}

	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		this.encoding = filterConfig.getInitParameter("encoding");// 获取传递过来的初使化编码

	}

}