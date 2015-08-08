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

	public Client() {
		configurationService = new ConfigurationService();
	}

	public Client(ConfigurationService configurationService) {
		this.configurationService = configurationService;
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

			JsonApiClient jsonApiClient = JsonApiClient.getInstance();
			JsonResponse jsonResponse = jsonApiClient.get(url, configurationService.apiToken());

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

	protected int getStatus(JsonResponse jsonResponse) throws Exception {
		return jsonResponse.getStatus();
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
