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
	
	int decodeCycle= 10;
	private static final String rootPath="WEB-INF/qrc/";
	private static final String imageType=".qrc";
	private static final String[] filenames = new String[]{"amazon","apache","apple","att","cisco","dell","ebay","facebook","google","hp","ibm","intel","linkedin","microsoft","mozilla","oracle","oreilly","redhat","techcrunch","wired"};
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		String decodedString = "";
		QuotaService qs = QuotaServiceFactory.getQuotaService();
		// plusieurs executions afin de voir si les résultats sont reproductibles, (et pour faire 1 moyenne)
		for (int j = 0; j < decodeCycle; j++) {
			long start = qs.getCpuTimeInMegaCycles();
			//for (int i = 0; i < 100; i++) {
			log.info("Start : "+start);
				QRCodeDecoder decoder = new QRCodeDecoder();
				//affichage des valeurs décodées à la dernière exécution
				for(int k=0;k<filenames.length ;k++){
					if (j==(decodeCycle-1)) {
						decodedString += processDecode(rootPath+filenames[k]+imageType, decoder);
						decodedString += "\\n";
					}
				}
				

			//}
			long end = qs.getCpuTimeInMegaCycles();
			log.info("End : "+end);
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
