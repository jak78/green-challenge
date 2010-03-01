package com.octo.greenchallenge.qrdecoder;

import jp.sourceforge.qrcode.QRCodeDecoder;
import junit.framework.TestCase;

import org.junit.Test;

public class QRDecoderServletTest extends TestCase {

	@Test
	public void testProcessDecode() throws Exception {

		QRDecoderServlet servlet = new QRDecoderServlet();
		QRCodeDecoder decoder = new QRCodeDecoder();

		String actual = servlet.processDecode("apple.qrc", decoder);
		String expectedBegin = "BEGIN:VCARD";
		String expectedEnd = "END:VCARD";

		assertEquals(expectedBegin, actual.substring(0, 11));
		assertEquals(expectedEnd, actual.substring(actual.length() - 9, actual.length()));
	}

}
