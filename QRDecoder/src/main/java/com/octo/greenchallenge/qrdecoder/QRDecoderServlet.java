package com.octo.greenchallenge.qrdecoder;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.util.ContentConverter;

import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;

@SuppressWarnings("serial")
public class QRDecoderServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(QRDecoderServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		String decodedString = "";
		QuotaService qs = QuotaServiceFactory.getQuotaService();
		for (int j = 0; j < 10; j++) {
			long start = qs.getCpuTimeInMegaCycles();
			for (int i = 0; i < 100; i++) {

				QRCodeDecoder decoder = new QRCodeDecoder();
				decodedString = processDecode("WEB-INF/qrc/apple.qrc", decoder);

			}
			long end = qs.getCpuTimeInMegaCycles();
			double cpuSeconds = qs.convertMegacyclesToCpuSeconds(end - start);
			log.info("CPU en secondes : " + cpuSeconds
					+ " / CPU en Megacycles : " + (end - start));
		}

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
