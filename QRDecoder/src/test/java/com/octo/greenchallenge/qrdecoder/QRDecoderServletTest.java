package com.octo.greenchallenge.qrdecoder;

import jp.sourceforge.qrcode.QRCodeDecoder;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class QRDecoderServletTest extends TestCase {
    private QRDecoderServlet servlet;
    private QRCodeDecoder decoder;

    @Before
    public void setUp() {
        servlet = new QRDecoderServlet();
        decoder = new QRCodeDecoder();
    }

    @Test
    public void testProcessDecode() throws Exception {

        String actual = servlet.processDecode("apple.qrc", decoder);
        String expectedBegin = "BEGIN:VCARD";
        String expectedEnd = "END:VCARD";

        assertEquals(expectedBegin, actual.substring(0, 11));
        assertEquals(expectedEnd, actual.substring(actual.length() - 9, actual.length()));
    }

    @Test
    public void testGetChallengerID() throws Exception {
        assertEquals("mikaelrob@gmail.com",servlet.getChallengerID("http://qr-decode-mikaelrob.appspot.com"));
        assertEquals("test-toto@gmail.com",servlet.getChallengerID("http://qr-decode-test-toto.appspot.com"));
        assertEquals("fake@gmail.com",servlet.getChallengerID("http://localhost:8888/qrdecoder"));
    }

}
