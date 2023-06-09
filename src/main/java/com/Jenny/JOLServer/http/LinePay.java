package com.Jenny.JOLServer.http;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Collections;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LinePay {
	@Value("${trust.store}")
	private Resource trustStore;

	@Value("${trust.store.password}")
	private String trustStorePassword;
	private final Logger log = LoggerFactory.getLogger(LinePay.class);

	public URL getUrl() throws Exception {
		String u = "";
		try {
			return new URL(u);
		} catch (Exception e) {
			log.error("get URL ERROR:{}", e.getMessage());
		}
		return null;
	}

	public String call(String body) throws Exception {
//		HttpClient httpClient = HttpClient.newHttpClient();
//		String url = "https://sandbox.tappaysdk.com/tpc/payment/pay-by-prime";
//		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
//				.header("x-api-key", "partner_aqVYKm8K3d34f1uZhQDK0GZpXmsWaGlPtBhrnoGnpjiRXQGlvUQDeuWA")
//				.POST(BodyPublishers.ofString(body)).build();
//		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
//		String reponsebody = response.body();
//		log.info("reponsebody:{}", reponsebody);
//		
		
		
		
		
		
		
		
		 ResponseEntity<String> responData = 
			      restTemplate().getForEntity("https://sandbox.tappaysdk.com/tpc/payment/pay-by-prime", String.class, Collections.emptyMap());
		 log.info("reponse:{}",responData.toString());
		return "Success";

	}

	RestTemplate restTemplate() throws Exception {
		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return new RestTemplate(factory);
	}

	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").permitAll();
		return http.build();
	}
}
