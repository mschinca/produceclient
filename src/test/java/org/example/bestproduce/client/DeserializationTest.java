package org.example.bestproduce.client;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.example.bestproduce.jsonclient.JsonApiClient;
import org.example.bestproduce.jsonclient.JsonResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DeserializationTest {

	private static final String CONFIGURATION_SERVER_URL = "http://localhost:3000";
	private static final String PRICE_SERVER_URL = "http://localhost:4000";

	@Test
	public void testDeserializeToken() throws IOException {
		String body = "{\"value\": \"config-service-token\"}";

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
		Token token = mapper.readValue(body, Token.class);
		assertThat(token.getValue(), is("config-service-token"));
	}

	@Test
	public void testDeserializeRates() throws IOException {
		String body = "{\"ackValue\": \"FAILURE\",\"errorMessage\": \"unknown product\"}";

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
		RateResponse response = mapper.readValue(body, RateResponse.class);
		assertThat(response.getErrorMessage(), is("unknown product"));
	}

	@Test
	@Ignore
	// needs a dummy configuration server in execution
	public void testDeserializeApiEndpointEndToEnd() throws IOException, InstantiationException, IllegalAccessException {
		String value = JsonApiClient.getInstance().get(new URL(CONFIGURATION_SERVER_URL + "/apiendpoint"), "configuration-token").getBody(ApiEndpoint.class).getValue();
		assertThat(value, is("localhost:4000"));
	}

	@Test
	@Ignore
	// needs a dummy configuration server in execution
	public void testDeserializePricesEndToEnd() throws IOException, InstantiationException, IllegalAccessException {
		String quote = JsonApiClient.getInstance().get(new URL(PRICE_SERVER_URL + "/prices"), "configuration-token").getBody(RateResponse.class).getQuote();
		assertThat(quote, is("134"));
	}
}
