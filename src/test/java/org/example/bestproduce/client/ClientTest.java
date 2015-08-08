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
		mockJsonApiClientGetToReturnStatus(500);
		TestableClient client = new TestableClient(configurationService, jsonApiClient);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates, is(nullValue()));
	}

	@Test
	public void testWhenApiServerReturnsUnauthorizedErrorClientReturnsWrongCredentialsResponse() throws IOException, IllegalAccessException, InstantiationException {
		mockJsonApiClientGetToReturnStatus(403);
		TestableClient client = new TestableClient(configurationService, jsonApiClient);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.FAILURE));
		assertThat(rates.getErrorMessage(), is("Wrong credentials"));
	}

	private void mockJsonApiClientGetToReturnStatus(int t) throws IOException {
		JsonResponse jsonResponse = mock(JsonResponse.class);
		when(jsonResponse.getStatus()).thenReturn(t);
		when(jsonApiClient.get(
				(URL) anyObject(),
				anyString())).thenReturn(jsonResponse);
	}

	@Test
	public void testWhenApiServerReturnsSuccessfulResponseClientReturnsRateResponse() throws IOException, IllegalAccessException, InstantiationException {
		mockJsonApiClientGetToReturnStatus(200);

		TestableClient client = new TestableClient(configurationService, jsonApiClient);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.OK));
	}

	@Test
	public void testWhenAnExceptionOccursRateResponseIsNull() throws IOException, IllegalAccessException, InstantiationException {
		TestableClient client = new TestableClient(configurationService, JsonApiClient.getInstance());
		client.setRaiseException(true);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates, is(nullValue()));
	}

	class TestableClient extends Client {

		private int status;
		private boolean raiseException = false;

		TestableClient() {
		}

		TestableClient(ConfigurationService configurationService, JsonApiClient jsonApiClient) {
			super(configurationService, jsonApiClient);
		}

		@Override
		protected RateResponse getBody(JsonResponse jsonResponse) throws IllegalAccessException, InstantiationException, IOException {
			RateResponse rateResponse = new RateResponse();
			rateResponse.setAckValue(RateResponse.OK);
			return rateResponse;
		}

		public void setRaiseException(boolean raiseException) {
			this.raiseException = raiseException;
		}
	}
}
