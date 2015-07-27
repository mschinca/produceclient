package org.example.bestproduce.jsonclient;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class JsonResponse {
	private HttpURLConnection connection;

	protected JsonResponse(HttpURLConnection connection) {
		this.connection = connection;
	}

	public int getStatus() throws IOException {
		return connection.getResponseCode();
	}

	public <ResponseType> ResponseType getBody(Class<ResponseType> responseKlass) throws IllegalAccessException, InstantiationException, IOException {
		String body = getBody(connection);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
		return mapper.readValue(body, responseKlass);
	}

	private String getBody(HttpURLConnection conn) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
		StringBuilder body = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			body.append(line);
		}
		conn.disconnect();
		return body.toString();
	}
}
