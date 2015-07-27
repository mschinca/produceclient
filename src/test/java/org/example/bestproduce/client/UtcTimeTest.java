package org.example.bestproduce.client;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UtcTimeTest {

	@Test
	public void testUtcTime() throws MalformedURLException {
		String now = ISODateTimeFormat.dateTimeNoMillis().print(new DateTime(DateTimeZone.UTC));
		System.out.format("Current time: %s", now);
	}
}
