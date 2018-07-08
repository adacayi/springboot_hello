package com.sanver.trials.springboot_hello;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix = "greeting")
public class GreeterRestController {
	private RestTemplate template = new RestTemplate();
	private String saying;
	private String backendServiceHost;
	private int backendServicePort;

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
		String backendServiceUrl;
		if (backendServicePort == 0) {
			backendServiceUrl = String.format("http://%s/backend/greeting?name=%s", backendServiceHost, saying);
		} else
			backendServiceUrl = String.format("http://%s:%d/backend/greeting?name=%s", backendServiceHost,
					backendServicePort, saying);
		BackendDTO response = template.getForObject(backendServiceUrl, BackendDTO.class);
		return response.getGreeting() + ". The address is " + response.getIp() + ".\nTime is "
				+ LocalDateTime.ofInstant(Instant.ofEpochMilli(response.getTime()), ZoneId.systemDefault());
	}
}
