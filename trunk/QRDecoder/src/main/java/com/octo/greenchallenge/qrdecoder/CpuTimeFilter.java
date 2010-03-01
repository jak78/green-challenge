package com.octo.greenchallenge.qrdecoder;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;

public class CpuTimeFilter implements Filter {

	private static final Logger log = Logger.getLogger(CpuTimeFilter.class.getName());
	private QuotaService qs;

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

//		long start = qs.getCpuTimeInMegaCycles();
		chain.doFilter(request, response);
//		long end = qs.getCpuTimeInMegaCycles();
//		double cpuSeconds = qs.convertMegacyclesToCpuSeconds(end - start);
//		log.info("CPU en secondes : " + cpuSeconds +" / CPU en Megacycles : " + (end - start));
	}

	public void init(FilterConfig arg0) throws ServletException {
		qs = QuotaServiceFactory.getQuotaService();
	}

}
