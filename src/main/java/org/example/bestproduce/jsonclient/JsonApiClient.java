package org.example.bestproduce.jsonclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonApiClient {
	private static JsonApiClient instance = new JsonApiClient();

	public static JsonApiClient getInstance() {
		return instance;
	}

	public JsonResponse get(URL url, String apiToken) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Authentication", "BEARER " + apiToken);
		return new JsonResponse(connection);
	}
}
