package com.metapack.stores.repository;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.metapack.stores.helper.JsoupRepositoryHelper;
import com.metapack.stores.helper.ServiceResponseStatus;
import com.metapack.stores.request.JsoupResponse;
import com.metapack.stores.request.StoreResponse;
import com.metapack.stores.service.IHttpService;

/**
 * @author jcarrera
 *
 */
@Repository
@EnableAsync
@EnableScheduling
public class JsoupStoreRepository implements IRepository<StoreResponse> {

	@Autowired
	private IHttpService<JsoupResponse, String> jsoupHTTPService;
	private StoreResponse lastResponse = null;
	private static final int HTTP_SERVICE_TIME_RATE = 1800000;
	@Value("${metapack.service.url}")
	private String storeServiceUrl;

	/*
	 * Return @StoreResponse from cache or HTTP service.
	 */
	@Override
	public StoreResponse getData() {
		if (this.getResponse() == null
				|| this.getResponse().getStatus() == ServiceResponseStatus.ERROR)
			this.getDataFromService();
		return this.getResponse();
	}

	/**
	 * Calls to HTTP service each 30 Minutes and store response if response
	 * status is OK.
	 */
	@Scheduled(fixedRate = HTTP_SERVICE_TIME_RATE)
	public void getDataFromService() {
		JsoupResponse response = jsoupHTTPService.getData(storeServiceUrl);
		if (this.getResponse() == null
				|| this.getResponse().getStatus() == ServiceResponseStatus.ERROR
				|| response.getStatus() == ServiceResponseStatus.OK) {
			StoreResponse storeResponse = transformResponse(response);
			this.setResponse(storeResponse);
		}
	}

	private StoreResponse transformResponse(JsoupResponse response) {
		StoreResponse storeResponse = new StoreResponse();
		storeResponse.setStatus(response.getStatus());
		storeResponse.setErrorMessage(response.getErrorMessage());
		storeResponse.setStores(JsoupRepositoryHelper
				.extractStoresFromHTMLDocument(response.getHtmlDoc()));
		storeResponse.setLastUpdated(Calendar.getInstance().getTime());
		return storeResponse;
	}

	private synchronized StoreResponse getResponse() {
		return lastResponse;
	}

	private synchronized void setResponse(StoreResponse response) {
		this.lastResponse = response;
	}

	public IHttpService<JsoupResponse, String> getJsoupHTTPService() {
		return jsoupHTTPService;
	}

	public void setJsoupHTTPService(
			IHttpService<JsoupResponse, String> jsoupHTTPService) {
		this.jsoupHTTPService = jsoupHTTPService;
	}

	public String getStoreServiceUrl() {
		return storeServiceUrl;
	}

	public void setStoreServiceUrl(String storeServiceUrl) {
		this.storeServiceUrl = storeServiceUrl;
	}

}
