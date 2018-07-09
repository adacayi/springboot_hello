package com.sanver.trials.springboot_hello;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class GreeterService {
	private RestTemplate rest;

	public GreeterService(RestTemplate rest) {
		this.rest = rest;
	}

	@HystrixCommand(fallbackMethod = "greetingReliable")
	public String greeting(String backendServiceHost, int backendServicePort, String saying) {
		String backendServiceUrl;
		if (backendServicePort == 0) {
			backendServiceUrl = String.format("http://%s/backend/greeting?name=%s", backendServiceHost, saying);
		} else
			backendServiceUrl = String.format("http://%s:%d/backend/greeting?name=%s", backendServiceHost,
					backendServicePort, saying);
		BackendDTO response = rest.getForObject(backendServiceUrl, BackendDTO.class);
		return response.getGreeting() + ". The address is " + response.getIp() + ".\nTime is "
				+ LocalDateTime.ofInstant(Instant.ofEpochMilli(response.getTime()), ZoneId.systemDefault());
	}

	// Fallback method must have the same parameters with the actual method. Else it
	// will give a runtime error.
	public String greetingReliable(String backendServiceHost, int backendServicePort, String saying) {
		String hostAddress;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex) {
			hostAddress = "Unknown";
		}
		return "Reliable greeting from " + hostAddress;
	}
}
