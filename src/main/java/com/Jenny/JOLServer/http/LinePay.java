package com.Jenny.JOLServer.http;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;






@Component
public class LinePay {
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
		HttpClient httpClient = HttpClient.newHttpClient();
		String url = "https://sandbox.tappaysdk.com:443/tpc/payment/pay-by-prime";
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
				.header("x-api-key","partner_aqVYKm8K3d34f1uZhQDK0GZpXmsWaGlPtBhrnoGnpjiRXQGlvUQDeuWA")
				.POST(BodyPublishers.ofString(body)).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		String reponsebody = response.body();
				return reponsebody;
		
	}


}
