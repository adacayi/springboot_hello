package com.sanver.trials.springboot_hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix = "greeting")
public class GreeterRestController {
	private String saying;
	private String backendServiceHost;
	private int backendServicePort;

	@Autowired
	private GreeterService greeterService;

	public String getSaying() {
		return saying;
	}

	public String getBackendServiceHost() {
		return backendServiceHost;
	}

	public int getBackendServicePort() {
		return backendServicePort;
	}

	public void setSaying(String saying) {
		this.saying = saying;
	}

	public void setBackendServiceHost(String backendServiceHost) {
		this.backendServiceHost = backendServiceHost;
	}

	public void setBackendServicePort(int backendServicePort) {
		this.backendServicePort = backendServicePort;
	}

	@RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = "text/plain")
	public String greeting() {
		String response = greeterService.greeting(backendServiceHost, backendServicePort, saying);
		return response;
	}
}
