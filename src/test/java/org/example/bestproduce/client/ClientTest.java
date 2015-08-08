package org.example.bestproduce.client;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.is;
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
	public void testClient() throws IOException, IllegalAccessException, InstantiationException {
		Client client = new Client();

		RateResponse rates = client.getRates(new RateRequest());

		assertThat(rates.getAckValue(), is(RateResponse.OK));
	}

	class TestableClient extends Client {
		@Override
		protected String getPricesApiEndpoint() throws IllegalAccessException, IOException, InstantiationException {
			return "prices-api-endpoint";
		}
	}
}
