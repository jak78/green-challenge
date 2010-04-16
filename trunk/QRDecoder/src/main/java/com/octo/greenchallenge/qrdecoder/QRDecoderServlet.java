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
	private static final String[] filenames = new String[]{"amazon.qrc","apache.qrc","apple.qrc","att.qrc","cisco.qrc","dell.qrc","ebay.qrc","facebook.qrc","google.qrc","hp.qrc","ibm.qrc","intel.qrc","linkedin.qrc","microsoft.qrc","mozilla.qrc","oracle.qrc","oreilly.qrc","redhat.qrc","techcrunch.qrc","wired.qrc","ABL.vcf.qrc", "ACL.vcf.qrc", "ACO.vcf.qrc", "AFC.vcf.qrc", "AFE.vcf.qrc", "AFF.vcf.qrc", "AHU.vcf.qrc", "AMZ.vcf.qrc", "AND.vcf.qrc", "AVE.vcf.qrc", "AZO.vcf.qrc", "BCH.vcf.qrc", "BEG.vcf.qrc", "BHA.vcf.qrc", "BLA.vcf.qrc", "BMA.vcf.qrc", "BPA.vcf.qrc", "CBE.vcf.qrc", "CBL.vcf.qrc", "CDC.vcf.qrc", "CGH.vcf.qrc", "CLR.vcf.qrc", "CGU.vcf.qrc", "COT.vcf.qrc", "CPA.vcf.qrc", "CPI.vcf.qrc", "CPO.vcf.qrc", "CTH.vcf.qrc", "CTR.vcf.qrc", "DAL.vcf.qrc", "DAT.vcf.qrc", "DFR.vcf.qrc", "DHA.vcf.qrc", "DJO.vcf.qrc", "DOM.vcf.qrc", "DRO.vcf.qrc", "DZO.vcf.qrc", "EBE.vcf.qrc", "EBI.vcf.qrc", "ECH.vcf.qrc", "EDO.vcf.qrc", "EDR.vcf.qrc", "EDU.vcf.qrc", "EFA.vcf.qrc", "EGE.vcf.qrc", "EGN.vcf.qrc", "EPA.vcf.qrc", "ETH.vcf.qrc", "FCH.vcf.qrc", "FDA.vcf.qrc", "FFR.vcf.qrc", "FGA.vcf.qrc", "FJG.vcf.qrc", "FHI.vcf.qrc", "FPE.vcf.qrc", "FRO.vcf.qrc", "FSA.vcf.qrc", "FWA.vcf.qrc", "GDU.vcf.qrc", "GGU.vcf.qrc", "GIL.vcf.qrc", "GLZ.vcf.qrc", "GPL.vcf.qrc", "HLO.vcf.qrc", "HTR.vcf.qrc", "HVA.vcf.qrc", "IAN.vcf.qrc", "IGN.vcf.qrc", "IHE.vcf.qrc", "ISC.vcf.qrc", "JAS.vcf.qrc", "JBE.vcf.qrc", "JCA.vcf.qrc", "JFG.vcf.qrc", "JFH.vcf.qrc", "JGL.vcf.qrc", "JJA.vcf.qrc", "JLY.vcf.qrc", "JME.vcf.qrc", "JSC.vcf.qrc", "JVA.vcf.qrc", "JYR.vcf.qrc", "KBO.vcf.qrc", "KMA.vcf.qrc", "LAV.vcf.qrc", "LBA.vcf.qrc", "LBR.vcf.qrc", "LCI.vcf.qrc", "LRI.vcf.qrc", "MAC.vcf.qrc", "MAG.vcf.qrc", "MBA.vcf.qrc", "MBO.vcf.qrc", "MBE.vcf.qrc", "MDE.vcf.qrc", "MEL.vcf.qrc", "MGA.vcf.qrc", "MHI.vcf.qrc", "MLO.vcf.qrc", "MMD.vcf.qrc", "MLA.vcf.qrc", "MMO.vcf.qrc", "MPH.vcf.qrc", "MRO.vcf.qrc", "MWA.vcf.qrc", "NBA.vcf.qrc", "NFA.vcf.qrc", "NGR.vcf.qrc", "NHO.vcf.qrc", "NLE.vcf.qrc", "NRA.vcf.qrc", "NSL.vcf.qrc", "NSA.vcf.qrc", "OJA.vcf.qrc", "OLD.vcf.qrc", "OLM.vcf.qrc", "OLR.vcf.qrc", "OMA.vcf.qrc", "PKE.vcf.qrc", "PGU.vcf.qrc", "PPE.vcf.qrc", "PSO.vcf.qrc", "RCS.vcf.qrc", "REM.vcf.qrc", "RGA.vcf.qrc", "RGH.vcf.qrc", "RKO.vcf.qrc", "RPA.vcf.qrc", "RSA.vcf.qrc", "SDJ.vcf.qrc", "SGA.vcf.qrc", "SGR.vcf.qrc", "SFA.vcf.qrc", "SGU.vcf.qrc", "SMA.vcf.qrc", "SPR.vcf.qrc", "THL.vcf.qrc", "TLI.vcf.qrc", "TOM.vcf.qrc", "TVI.vcf.qrc", "VCO.vcf.qrc", "VDA.vcf.qrc", "VGU.vcf.qrc", "VVI.vcf.qrc", "WBO.vcf.qrc", "WMO.vcf.qrc", "YBE.vcf.qrc", "YMA.vcf.qrc", "YRA.vcf.qrc"};
	
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
