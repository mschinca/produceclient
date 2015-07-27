package org.example.bestproduce.client;

public class RateResponse {
	public static String OK = "OK";
	public static String FAILURE = "FAILURE";

	private String ackValue;
	private String errorMessage;
	private String quote;

	public String getAckValue() {
		return ackValue;
	}

	public void setAckValue(String ackValue) {
		this.ackValue = ackValue;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}
}
