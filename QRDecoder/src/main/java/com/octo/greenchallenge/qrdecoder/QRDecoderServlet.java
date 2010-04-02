package com.octo.greenchallenge.qrdecoder;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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
	
	int decodeCycle= 100;
	private static final String rootPath="WEB-INF/qrc/";
	private static final String imageType=".qrc";
	private static final String[] filenames = new String[]{"amazon","apache","apple","att","cisco","dell","ebay","facebook","google","hp","ibm","intel","linkedin","microsoft","mozilla","oracle","oreilly","redhat","techcrunch","wired"};
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {		
		resp.setContentType("text/plain");

		String decodedString = "";
		String decodeTemp = "";
		QuotaService qs = QuotaServiceFactory.getQuotaService();
		
		// plusieurs executions afin de voir si les résultats sont reproductibles, (et pour faire 1 moyenne)
		
		// 1 décodage à vide
		for(int k=0;k<filenames.length ;k++){
			QRCodeDecoder decoder = new QRCodeDecoder();
			decodeTemp = processDecode(rootPath+filenames[k]+imageType, decoder);
		}
		// démarrage mesure
		long loopStart = qs.getCpuTimeInMegaCycles();
		
		for (int j = 0; j < decodeCycle; j++) {
			long start = qs.getCpuTimeInMegaCycles();
			//for (int i = 0; i < 100; i++) {
			//log.info("Start : "+start);
				QRCodeDecoder decoder = new QRCodeDecoder();
				QRCodeDecoder.setCanvas(
						new jp.sourceforge.qrcode.util.DebugCanvasAdapter(){
							public  void println(String message){
								//QRDecoderServlet.log.info("DebugCanvas: " +  message);
								// System.out.println(message);
							};
						}
				);
				//affichage des valeurs décodées à la dernière execution
				for(int k=0;k<filenames.length ;k++){
					decodeTemp = processDecode(rootPath+filenames[k]+imageType, decoder);
					if (j==(decodeCycle-1)) {
						decodedString += decodeTemp;
						decodedString += "\n";
					}
				}
			//}
			long end = qs.getCpuTimeInMegaCycles();
			//log.info("End : "+end);
			double cpuSeconds = qs.convertMegacyclesToCpuSeconds(end - start);
			log.info("CPU en secondes : " + cpuSeconds
					+ " / CPU en Megacycles : " + (end - start));							
		}
		
		long cpuMegacyclesValue = qs.getCpuTimeInMegaCycles() - loopStart;
		cpuMegacyclesValue = cpuMegacyclesValue/100;
		String challengerID = getChallengerID(req);
		//TODO: gestion de la constante externe SERVER_APP ?
		String source = "SERVER_APP";
		
		postResults(challengerID, cpuMegacyclesValue, source);
				
		resp.getWriter().println(decodedString);
	}
	
	/**
	 * Extract the challenger ID from the requested URL.
	 * For instance, "fakeID" will be extracted form the following URL:
	 *     "http://fakeID.qr-decode.appspot.com/"
	 */
	public String getChallengerID(HttpServletRequest req){
		int urlProtocolHeaderSize = 7; // length of "http://"
		int dot = '.'; 
		String url = req.getRequestURL().toString().substring(urlProtocolHeaderSize);
		//TODO: refactorer la gestion du contexte localhost 
		if(url.equals("localhost:8888/qrdecoder")){
			url = "fake.qr-decode.appspot.com/";
		}				
		return url.substring(0, url.indexOf(dot));
	}
	
	/**
	 * 
	 * @param filename
	 * @param decoder
	 * @return
	 * @throws IOException
	 */
	public String processDecode(String filename, QRCodeDecoder decoder)
			throws IOException {
		RleImage image = new RleImage(filename);
		String decodedString = new String(decoder.decode(image));
		decodedString = ContentConverter.convert(decodedString);
		return decodedString;
	}

	/**
	 * 
	 * @param challengerID
	 * @param cpuMegacyclesValue
	 * @param source
	 */
	private void postResults(String challengerID, long cpuMegacyclesValue, String source){
        try {
        	String encoding = "UTF-8";
        	String cpuMegacycles = URLEncoder.encode( Long.toString(cpuMegacyclesValue ), encoding);			
    		challengerID = URLEncoder.encode(challengerID, encoding);
    		source = URLEncoder.encode(source, encoding);
        	
        	log.info("TRYING POST: challengerID=" + challengerID +" ; CPUCycles=" + cpuMegacycles+" ; source=" + source);
        	
            URL url = new URL("http://score-challenge.appspot.com/api/collect/cpu");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write("challengerID=" + challengerID);
            writer.write("&CPUCycles=" + cpuMegacycles);
            writer.write("&source=" + source);
            writer.close();
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
            	// OK!
            	log.info("POST: challengerID=" + challengerID +" ; CPUCycles=" + cpuMegacycles+" ; source=" + source);                
            } else {
            	// Server returned HTTP error code.            	
            	log.info("POST: Server returned HTTP error code " + responseCode);
            }
        } catch (MalformedURLException e) {
        	log.info("POST: MalformedURLException");
        } catch (UnsupportedEncodingException e1) {
        	log.info("POST: UnsupportedEncodingException");
		} catch (IOException e) {
        	log.info("POST: IOException");
		}
	}
}
