package org.example.bestproduce.client;

import org.example.bestproduce.jsonclient.JsonApiClient;
import org.example.bestproduce.jsonclient.JsonResponse;

import java.io.IOException;
import java.net.URL;

public class ConfigurationService {
	private static final String PRICES_ENDPOINT = "/apiendpoint";
	private static final String TOKEN_ENDPOINT = "/apitoken";
	private static final String CONFIGURATION_SERVICE_PATH = "http://localhost:3000";

	public static String getPricesApiEndpoint() throws IllegalAccessException, IOException, InstantiationException {
		JsonApiClient jsonClient = JsonApiClient.getInstance();
		JsonResponse jsonResponse = jsonClient.get(new URL(CONFIGURATION_SERVICE_PATH + PRICES_ENDPOINT), "configuration-token");
		return jsonResponse.getBody(ApiEndpoint.class).getValue();
	}

	public static String getApiToken() throws IOException, InstantiationException, IllegalAccessException {
		JsonApiClient jsonClient = JsonApiClient.getInstance();
		JsonResponse jsonResponse = jsonClient.get(new URL(CONFIGURATION_SERVICE_PATH + TOKEN_ENDPOINT), "configuration-token");
		return jsonResponse.getBody(Token.class).getValue();
	}

	public String apiToken() throws IllegalAccessException, IOException, InstantiationException {
		return ConfigurationService.getApiToken();
	}
}
