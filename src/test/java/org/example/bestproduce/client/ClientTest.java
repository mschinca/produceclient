package org.example.bestproduce.client;

import org.example.bestproduce.jsonclient.JsonApiClient;
import org.example.bestproduce.jsonclient.JsonResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {
	private ConfigurationService configurationService;
	private JsonApiClient jsonApiClient;

	@Test
	@Ignore
	public void testClientEndToEnd() throws IOException, IllegalAccessException, InstantiationException {
		Client client = new Client();

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.OK));
	}

	@Before
	public void setUpMocks() throws IllegalAccessException, IOException, InstantiationException {
		configurationService = mock(ConfigurationService.class);
		jsonApiClient = mock(JsonApiClient.class);
		when(configurationService.apiToken()).thenReturn("api-token");
		when(configurationService.pricesApiEndPoint()).thenReturn("api-token");
	}

	@Test
	public void testWhenApiServerReturnsGenericErrorClientReturnsNullResponse() throws IOException, IllegalAccessException, InstantiationException {
		mockJsonApiClientGetToReturnStatus(500, mock(JsonResponse.class));
		Client client = new Client(configurationService, jsonApiClient);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates, is(nullValue()));
	}

	@Test
	public void testWhenApiServerReturnsUnauthorizedErrorClientReturnsWrongCredentialsResponse() throws IOException, IllegalAccessException, InstantiationException {
		mockJsonApiClientGetToReturnStatus(403, mock(JsonResponse.class));
		Client client = new Client(configurationService, jsonApiClient);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.FAILURE));
		assertThat(rates.getErrorMessage(), is("Wrong credentials"));
	}

	@Test
	public void testWhenApiServerReturnsSuccessfulResponseClientReturnsRateResponse() throws IOException, IllegalAccessException, InstantiationException {
		JsonResponse jsonResponse = mock(JsonResponse.class);
		RateResponse rateResponse = successfulResponse();
		when(jsonResponse.getBody(RateResponse.class)).thenReturn(rateResponse);
		mockJsonApiClientGetToReturnStatus(200, jsonResponse);

		Client client = new Client(configurationService, jsonApiClient);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.OK));
	}

	@Test
	public void testWhenAnExceptionOccursRateResponseIsNull() throws IOException, IllegalAccessException, InstantiationException {
		when(jsonApiClient.get(
				(URL) anyObject(),
				anyString())).thenThrow(new IOException());

		Client client = new Client(configurationService, jsonApiClient);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates, is(nullValue()));
	}

	private void mockJsonApiClientGetToReturnStatus(int status, JsonResponse jsonResponse) throws IOException {
		when(jsonResponse.getStatus()).thenReturn(status);
		when(jsonApiClient.get(
				(URL) anyObject(),
				anyString())).thenReturn(jsonResponse);
	}

	private RateResponse successfulResponse() {
		RateResponse rateResponse = new RateResponse();
		rateResponse.setAckValue(RateResponse.OK);
		return rateResponse;
	}
}
