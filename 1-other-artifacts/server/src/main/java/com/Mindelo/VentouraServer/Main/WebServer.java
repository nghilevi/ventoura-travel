package com.Mindelo.VentouraServer.Main;



import java.io.*;
import java.net.*;
import java.security.ProtectionDomain;
import java.util.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.server.nio.*;
import org.eclipse.jetty.util.thread.*;
import org.eclipse.jetty.webapp.*;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;

/**
 * Embedded Jetty Server
 */
public class WebServer {
	// TODO: You should configure this appropriately for your environment


	public static interface WebContext {
		public File getWarPath();

		public String getContextPath();
	}

	private Server server;
	private int port;
	private String bindInterface;

	public WebServer(int aPort) {
		this(aPort, null);
	}

	public WebServer(int aPort, String aBindInterface) {
		port = aPort;
		bindInterface = aBindInterface;
	}

	public void start() throws Exception {
		server = new Server();
		server.setThreadPool(createThreadPool());
		server.addConnector(createConnector());
		server.setHandler(createHandlers());
		server.setStopAtShutdown(true);
		server.start();
	}

	public void join() throws InterruptedException {
		server.join();
	}

	public void stop() throws Exception {
		server.stop();
	}

	private ThreadPool createThreadPool() {
		// TODO: You should configure these appropriately
		// for your environment - this is an example only
		QueuedThreadPool _threadPool = new QueuedThreadPool();
		_threadPool.setMinThreads(10);
		_threadPool.setMaxThreads(100);
		return _threadPool;
	}

	private SelectChannelConnector createConnector() {
		SelectChannelConnector _connector = new SelectChannelConnector();
		_connector.setPort(port);
		_connector.setHost(bindInterface);
		return _connector;
	}

	private HandlerCollection createHandlers() {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");

		// if (!isRunningInShadedJar()) {
		// // TODO No idea, what is a shaded JAR.
		// // Maybe we need this on test mode ?
		// //
		// http://steveliles.github.com/setting_up_embedded_jetty_8_and_spring_mvc_with_maven.html
		// _ctx.setWar(getShadedWarUrl());
		// } else {
		// // load web application content from the file system,
		// _ctx.setWar(PROJECT_RELATIVE_PATH_TO_WEBAPP);
		// }

		// -------------------------------------------------------
		// Tell Jetty to find the war to execute
		// http://eclipsesource.com/blogs/2009/10/02/executable-wars-with-jetty/
		// -------------------------------------------------------
		ProtectionDomain protectionDomain = WebServer.class
				.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		_ctx.setWar(location.toExternalForm());
		_ctx.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");

		List<Handler> _handlers = new ArrayList<Handler>();

		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[0]));

		RequestLogHandler _log = new RequestLogHandler();
		// The log functions has been teporatly disabled. ! TODO fix
		//_log.setRequestLog(createRequestLog());

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts, _log });

		return _result;
	}

	private RequestLog createRequestLog() {
		NCSARequestLog _log = new NCSARequestLog();

		File _logPath = new File(ConfigurationConstant.LOG_FILE_PATH);
		_logPath.getParentFile().mkdirs();

		_log.setFilename(_logPath.getPath());
		_log.setRetainDays(90);
		_log.setExtended(false);
		_log.setAppend(true);
		_log.setLogTimeZone("GMT");
		_log.setLogLatency(true);
		return _log;
	}
}
