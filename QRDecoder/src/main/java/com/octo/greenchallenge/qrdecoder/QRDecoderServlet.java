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
	
	int decodeCycle= 1;
	private static final String rootPath="WEB-INF/qrc/";
	//private static final String imageType=".qrc";
	
	private static final String[] filenames = new String[]{"amazon.qrc","apache.qrc","apple.qrc","att.qrc","cisco.qrc","dell.qrc","ebay.qrc","facebook.qrc","google.qrc","hp.qrc","ibm.qrc","intel.qrc","linkedin.qrc","microsoft.qrc","mozilla.qrc","oracle.qrc","oreilly.qrc","redhat.qrc","techcrunch.qrc","wired.qrc"};
	
	//private static final String[] filenames = new String[]{"Aaron_A_Bush.vcf.qrc", "Aaron_A_Watson.vcf.qrc", "Aaron_Bush.vcf.qrc", "Aaron_D_Bush.vcf.qrc", "Aaron_E_Watson.vcf.qrc", "Aaron_T_Watson.vcf.qrc", "Aaron_T_Watson_1.vcf.qrc", "Aaron_Watson.vcf.qrc", "Aaron_Watson_1.vcf.qrc", "Aa_Jr_Bush.vcf.qrc", "Abby_Watson.vcf.qrc", "Adam_Clinton.vcf.qrc", "Adam_R_Bush.vcf.qrc", "Addie_G_Bush.vcf.qrc", "Adeline_S_Bush.vcf.qrc", "Adler_H_Sr_Bush.vcf.qrc", "Adora_M_Clinton.vcf.qrc", "Adrian_Roger.vcf.qrc", "Alana_Bush.vcf.qrc", "Alana_E_Bush.vcf.qrc", "Alana_M_Bush.vcf.qrc", "Albert_P_IV_Bush.vcf.qrc", "Alan_M_Bush.vcf.qrc", "Alisha_N_Clinton.vcf.qrc", "Allise_Jones.vcf.qrc", "Allison_L_Clinton.vcf.qrc", "Amelia_M_Clinton.vcf.qrc", "Andrea_L_Clinton.vcf.qrc", "Andrew_Watson.vcf.qrc", "Anna_Jones.vcf.qrc", "Anna_K_Roger.vcf.qrc", "Anna_Roger.vcf.qrc", "Anna_Roger_1.vcf.qrc", "Ann_Clinton.vcf.qrc", "Ann_Roger.vcf.qrc", "Arthur_A_Clinton.vcf.qrc", "Ashley_Clinton.vcf.qrc", "Atley_L_Clinton.vcf.qrc", "Ayotte_Roger.vcf.qrc", "A_Bush.vcf.qrc", "A_Bush_1.vcf.qrc", "A_Bush_2.vcf.qrc", "A_Bush_3.vcf.qrc", "A_B_Roger.vcf.qrc", "A_L_Bush.vcf.qrc", "A_M_Clinton.vcf.qrc", "A_S_Watson.vcf.qrc", "A_Watson.vcf.qrc", "A_Watson_1.vcf.qrc", "A_Watson_2.vcf.qrc", "A_Watson_3.vcf.qrc", "A_Watson_4.vcf.qrc", "A_Watson_6.vcf.qrc", "A_Watson_5.vcf.qrc", "A_Watson_7.vcf.qrc", "Barbara_A_Clinton.vcf.qrc", "Barbara_M_Clinton.vcf.qrc", "Barry_S_Clinton.vcf.qrc", "Baughman_Roger.vcf.qrc", "Becki_Sanchez.vcf.qrc", "Beth_Roger.vcf.qrc", "Beverly_A_Clinton.vcf.qrc", "Bill_Clinton (1).vcf.qrc", "Bill_Clinton.vcf.qrc", "Bilstad_Roger.vcf.qrc", "Blake_Clinton.vcf.qrc", "Bob_J_Clinton.vcf.qrc", "Brandy_L_Clinton.vcf.qrc", "Brenda_A_Clinton.vcf.qrc", "Brenda_M_Clinton.vcf.qrc", "Brenda_R_Clinton.vcf.qrc", "Brenda_R_Clinton_1.vcf.qrc", "Brett_C_Clinton.vcf.qrc", "Brian_Clinton.vcf.qrc", "Brian_E_Clinton.vcf.qrc", "Brian_J_Clinton.vcf.qrc", "Brian_M_Clinton.vcf.qrc", "Chad_Smith.vcf.qrc", "C_Murray.vcf.qrc", "Daniella_Watson.vcf.qrc", "Darryl_Jones.vcf.qrc", "Emily_Jones.vcf.qrc", "Gary_Bush.vcf.qrc", "Goldberg_Roger.vcf.qrc", "Graf_Roger.vcf.qrc", "Hazel_Smith.vcf.qrc", "Heidi_Jones.vcf.qrc", "Holly_Jo_Jones.vcf.qrc", "Jessica_Bush.vcf.qrc", "Jordan_Jones.vcf.qrc", "Lawrence_Jones.vcf.qrc", "Linda_Jones.vcf.qrc", "Marrissa_Jones.vcf.qrc", "Lloyd_Jones.vcf.qrc", "Mary_Jones.vcf.qrc", "Rachel_Watson.vcf.qrc", "Renee_Watson.vcf.qrc", "Richard_Watson.vcf.qrc", "V_Watson.vcf.qrc", "Sandra_Bush.vcf.qrc"};
	
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
			decodeTemp = processDecode(rootPath+filenames[k], decoder);
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
					decodeTemp = processDecode(rootPath+filenames[k], decoder);
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
		cpuMegacyclesValue = cpuMegacyclesValue/decodeCycle;
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
