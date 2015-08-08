package org.example.bestproduce.client;

import org.apache.http.client.utils.URIBuilder;
import org.example.bestproduce.jsonclient.JsonApiClient;
import org.example.bestproduce.jsonclient.JsonResponse;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.net.URL;

public class Client {
	public RateResponse getRates(RateRequest request) throws IllegalAccessException, IOException, InstantiationException {
		RateResponse response = null;

		URIBuilder builder = new URIBuilder().
				setScheme("http").
				setHost(getPricesApiEndpoint()).
				setPath("/prices").
				setParameter("productType", request.getProductType()).setParameter("quantity", request.getQuantity()).
				setParameter("dateTime", getCurrentUtcTimeISO8601());

		try {
			URL url = builder.build().toURL();

			JsonApiClient jsonApiClient = JsonApiClient.getInstance();
			JsonResponse jsonResponse = jsonApiClient.get(url, getApiToken());

			int status = getStatus(jsonResponse);

			if (status == 200) {
				response = getBody(jsonResponse);
			} else if (status == 403) {
				return wrongCredentialsResponse();
			} else {
				System.out.println("Generic error from server");
			}

		} catch (Exception e) {
			System.out.println("There was an exception:");
			e.printStackTrace();
		}

		return response;
	}

	protected RateResponse getBody(JsonResponse jsonResponse) throws IllegalAccessException, InstantiationException, IOException {
		return jsonResponse.getBody(RateResponse.class);
	}

	protected int getStatus(JsonResponse jsonResponse) throws IOException {
		return jsonResponse.getStatus();
	}

	protected String getApiToken() throws IOException, InstantiationException, IllegalAccessException {
		return ConfigurationService.getApiToken();
	}

	protected String getPricesApiEndpoint() throws IllegalAccessException, IOException, InstantiationException {
		return ConfigurationService.getPricesApiEndpoint();
	}

	private RateResponse wrongCredentialsResponse() {
		RateResponse rateResponse = new RateResponse();
		rateResponse.setAckValue(RateResponse.FAILURE);
		rateResponse.setErrorMessage("Wrong credentials");
		return rateResponse;
	}

	private String getCurrentUtcTimeISO8601() {
		return ISODateTimeFormat.dateTimeNoMillis().print(new DateTime(DateTimeZone.UTC));
	}
}
