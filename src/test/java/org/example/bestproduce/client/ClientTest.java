package org.example.bestproduce.client;

import org.example.bestproduce.jsonclient.JsonResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ClientTest {

	@Test
	@Ignore
	public void testClientEndToEnd() throws IOException, IllegalAccessException, InstantiationException {
		Client client = new Client();

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.OK));
	}

	@Test
	public void testWhenApiServerReturnsGenericErrorClientReturnsNullResponse() throws IOException, IllegalAccessException, InstantiationException {
		TestableClient client = new TestableClient();
		client.setStatus(500);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates, is(nullValue()));
	}

	@Test
	public void testWhenApiServerReturnsUnauthorizedErrorClientReturnsWrongCredentialsResponse() throws IOException, IllegalAccessException, InstantiationException {
		TestableClient client = new TestableClient();
		client.setStatus(403);

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.FAILURE));
		assertThat(rates.getErrorMessage(), is("Wrong credentials"));
	}

	class TestableClient extends Client {

		private int status;

		@Override
		protected String getPricesApiEndpoint() throws IllegalAccessException, IOException, InstantiationException {
			return "prices-api-endpoint";
		}

		@Override
		protected String getApiToken() throws IOException, InstantiationException, IllegalAccessException {
			return "api-token";
		}

		@Override
		protected int getStatus(JsonResponse jsonResponse) throws IOException {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}
}
