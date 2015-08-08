package org.example.bestproduce.client;

import org.example.bestproduce.jsonclient.JsonResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {
	private ConfigurationService configurationService;

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
		when(configurationService.apiToken()).thenReturn("api-token");
		when(configurationService.pricesApiEndPoint()).thenReturn("api-token");
	}

	@Test
	public void testWhenApiServerReturnsGenericErrorClientReturnsNullResponse() throws IOException, IllegalAccessException, InstantiationException {
		TestableClient client = new TestableClient(configurationService);
		client.setStatus(500);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates, is(nullValue()));
	}

	@Test
	public void testWhenApiServerReturnsUnauthorizedErrorClientReturnsWrongCredentialsResponse() throws IOException, IllegalAccessException, InstantiationException {
		TestableClient client = new TestableClient(configurationService);
		client.setStatus(403);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.FAILURE));
		assertThat(rates.getErrorMessage(), is("Wrong credentials"));
	}

	@Test
	public void testWhenApiServerReturnsSuccessfulResponseClientReturnsRateResponse() throws IOException, IllegalAccessException, InstantiationException {
		TestableClient client = new TestableClient(configurationService);
		client.setStatus(200);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.OK));
	}

	@Test
	public void testWhenAnExceptionOccursRateResponseIsNull() throws IOException, IllegalAccessException, InstantiationException {
		TestableClient client = new TestableClient(configurationService);
		client.setRaiseException(true);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates, is(nullValue()));
	}

	class TestableClient extends Client {

		private int status;
		private boolean raiseException = false;

		TestableClient() {
		}

		TestableClient(ConfigurationService configurationService) {
			super(configurationService);
		}

		@Override
		protected int getStatus(JsonResponse jsonResponse) throws Exception {
			if (raiseException) {
				throw new Exception("Hardcoded exception");
			}
			else {
				return status;
			}
		}

		public void setStatus(int status) {
			this.status = status;
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
