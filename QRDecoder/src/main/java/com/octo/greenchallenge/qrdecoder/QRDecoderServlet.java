package com.octo.greenchallenge.qrdecoder;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.util.ContentConverter;

@SuppressWarnings("serial")
public class QRDecoderServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		// long start = System.currentTimeMillis();

		QRCodeDecoder decoder = new QRCodeDecoder();
		String decodedString = processDecode("WEB-INF/qrc/apple.qrc", decoder);

		// long processTime = System.currentTimeMillis() - start;
		
		resp.getWriter().println(decodedString);
	}

	public String processDecode(String filename, QRCodeDecoder decoder)
			throws IOException {
		RleImage image = new RleImage(filename);
		String decodedString = new String(decoder.decode(image));
		decodedString = ContentConverter.convert(decodedString);
		return decodedString;
	}

}
