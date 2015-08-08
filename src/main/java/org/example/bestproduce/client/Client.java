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
	private ConfigurationService configurationService;
	private JsonApiClient jsonApiClient;

	public Client() {
		configurationService = new ConfigurationService();
		jsonApiClient = JsonApiClient.getInstance();
	}

	public Client(ConfigurationService configurationService, JsonApiClient jsonApiClient) {
		this.configurationService = configurationService;
		this.jsonApiClient = jsonApiClient;
	}

	public RateResponse getRates(RateRequest request) throws IllegalAccessException, IOException, InstantiationException {
		RateResponse response = null;

		URIBuilder builder = new URIBuilder().
				setScheme("http").
				setHost(configurationService.pricesApiEndPoint()).
				setPath("/prices").
				setParameter("productType", request.getProductType()).setParameter("quantity", request.getQuantity()).
				setParameter("dateTime", getCurrentUtcTimeISO8601());

		try {
			URL url = builder.build().toURL();

			JsonResponse jsonResponse = jsonApiClient.get(url, configurationService.apiToken());

			int status = jsonResponse.getStatus();

			if (status == 200) {
				response = jsonResponse.getBody(RateResponse.class);
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
