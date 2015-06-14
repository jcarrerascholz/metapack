package com.metapack.stores.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.metapack.stores.helper.ServiceResponseStatus;
import com.metapack.stores.request.JsoupResponse;

/**
 * @author jcarrera
 *
 */
@Service
public class JsoupHttpService implements IHttpService<JsoupResponse, String> {

	private static final Logger log = LoggerFactory
			.getLogger(StoreService.class);

	/* 
	 * @return @JsoupResponse from HTML URL.
	 * @param HTML URL.
	 */
	@Override
	public JsoupResponse getData(String url) {
		JsoupResponse response = new JsoupResponse();
		try {
			Document htmlDoc = Jsoup.connect(url).get();
			response.setHtmlDoc(htmlDoc);
			response.setStatus(ServiceResponseStatus.OK);
		} catch (IOException e) {
			response.setStatus(ServiceResponseStatus.ERROR);
			response.setErrorMessage("Error while trying to connect to " + url + " Message: " + e.getMessage());
			log.error("JsoupHttpService.getData "
					+ "Error while trying to connect to " + url, e.getMessage());
		}
		return response;
	}

}
