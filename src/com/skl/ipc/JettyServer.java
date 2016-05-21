package com.skl.ipc;

import java.io.File;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 内 嵌 jetty http 服务
 *
 * @author chenlb(chenlb2008@gmail.com) 2012-5-17
 */
@Configurable
public class JettyServer {
	private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);

	// ===========set
	private int port = 8070;
	private String contextPath = "/";
	private Map<String, Filter> filters;
	private Map<String, Servlet> servlets;

	// ===========
	private Server server;

	private void init() throws Exception {
		server = new Server();

		// 连接池
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		connector.setMaxIdleTime(30000);
		connector.setRequestHeaderSize(8192);
		QueuedThreadPool threadPool = new QueuedThreadPool(5);
		threadPool.setName("embed-jetty-http");
		connector.setThreadPool(threadPool);
		server.setConnectors(new Connector[] { connector });

		// Context context = null;
		ServletContextHandler context = null;
		String workDir = System.getProperty("user.dir");
		File webRootDir = new File(workDir, "webapp");
		String webRoot = webRootDir.toURI().toURL().toExternalForm();
		context = new WebAppContext(webRoot, contextPath);
		server.setHandler(context);

		// add filter
		if (filters != null) {
			for (Map.Entry<String, Filter> eFilter : filters.entrySet()) {
				logger.info("add filter={}, path={}", eFilter.getValue().getClass(), eFilter.getKey());
				context.addFilter(new FilterHolder(eFilter.getValue()), eFilter.getKey(), FilterMapping.DEFAULT);
			}
		}

		// add servlet
		if (servlets != null) {
			for (Map.Entry<String, Servlet> eServlet : servlets.entrySet()) {
				logger.info("add servlet={}, path={}", eServlet.getValue().getClass(), eServlet.getKey());
				context.addServlet(new ServletHolder(eServlet.getValue()), eServlet.getKey());
			}
		}
	}

	public void start() throws Exception {
		init();
		server.start();
		logger.info("jetty embed server started, port={}", port);
	}

	public void stop() throws Exception {
		server.stop();
		server.destroy();
	}

	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring.xml");
		final JettyServer jettyServer = (JettyServer) context.getBean(JettyServer.class);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					jettyServer.stop();
				} catch (Exception e) {
					logger.error("run main stop error!", e);
				}
			}
		});
		try {
			jettyServer.start();
			logger.info("server started");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.warn("has exception!", e);
			System.exit(-1);
		}

	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void setFilters(Map<String, Filter> filters) {
		this.filters = filters;
	}

	public void setServlets(Map<String, Servlet> servlets) {
		this.servlets = servlets;
	}
}
