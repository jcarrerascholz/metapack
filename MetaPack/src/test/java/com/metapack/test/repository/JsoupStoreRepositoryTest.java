package com.metapack.test.repository;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.metapack.stores.helper.ServiceResponseStatus;
import com.metapack.stores.model.Store;
import com.metapack.stores.repository.JsoupStoreRepository;
import com.metapack.stores.request.JsoupResponse;
import com.metapack.stores.request.StoreResponse;
import com.metapack.stores.service.IHttpService;

/**
 * @author jcarrera
 *
 */
public class JsoupStoreRepositoryTest {

	private static final String TEST_LOCATIONS_HTML = "/test-locations.html";
	private static final String SERVICE_URL = "https://example.com/locations";
	Mockery jsoupHttpServiceContext = new Mockery();
	IHttpService<JsoupResponse, String> jsoupHttpService;

	@Before
	@SuppressWarnings("unchecked")
	public void doBeforeEachTest() {
		jsoupHttpService = jsoupHttpServiceContext.mock(IHttpService.class);
	}

	@Test
	public void testGetDataStatusOK() {
		JsoupResponse jsoupResponse = this.getJsoupResponse(
				ServiceResponseStatus.OK, null, null);
		StoreResponse response = getJsoupRepositoryWithContextExpectations(
				jsoupResponse).getData();
		Assert.assertEquals(response.getStatus(), ServiceResponseStatus.OK);
	}

	@Test
	public void testGetDataStatusError() {
		JsoupResponse jsoupResponse = this.getJsoupResponse(
				ServiceResponseStatus.ERROR, null, null);
		StoreResponse response = getJsoupRepositoryWithContextExpectations(
				jsoupResponse).getData();
		Assert.assertEquals(response.getStatus(), ServiceResponseStatus.ERROR);
	}

	@Test
	public void testGetDataErrorMessage() {
		JsoupResponse jsoupResponse = this.getJsoupResponse(null, "Test", null);
		StoreResponse response = getJsoupRepositoryWithContextExpectations(
				jsoupResponse).getData();
		Assert.assertEquals(response.getErrorMessage(), "Test");
	}

	@Test
	public void testGetDataDoc() throws IOException {
		String url = getClass().getResource(TEST_LOCATIONS_HTML).getFile();
		File file = new File(url);
		Document htmlDoc = Jsoup.parse(file, null);
		JsoupResponse jsoupResponse = this.getJsoupResponse(
				ServiceResponseStatus.OK, null, htmlDoc);
		StoreResponse response = getJsoupRepositoryWithContextExpectations(
				jsoupResponse).getData();
		Assert.assertEquals(response.getStatus(), ServiceResponseStatus.OK);
		Assert.assertNotNull(response.getStores());
		Assert.assertNotNull(response.getStores().get(0));
		Store store = response.getStores().get(0);
		Assert.assertEquals(store.getPostCode(), "AB11 5PS");
		Assert.assertEquals(store.getCityName(), "Aberdeen");
		Assert.assertEquals(store.getAddress(),
				"LSU 1A Union Square, Guild Street");
		Assert.assertEquals(store.getLongitude(), "-2.101030");
		Assert.assertEquals(store.getLatitude(), "57.144470");
	}

	@Test
	public void testGetDataStatusOKNoDocument() {
		JsoupResponse jsoupResponse = this.getJsoupResponse(
				ServiceResponseStatus.OK, null, null);
		StoreResponse response = getJsoupRepositoryWithContextExpectations(
				jsoupResponse).getData();
		Assert.assertNotNull(response);
	}

	// Start testing service cache.
	@Test
	public void testGetDataEmptyServiceResponse() {
		JsoupResponse jsoupResponse = this.getJsoupResponse(null, null, null);
		StoreResponse response = getJsoupRepositoryWithContextExpectations(
				jsoupResponse).getData();
		Assert.assertNotNull(response);
	}

	@Test
	public void testGetDataNullServiceResponseWithPreviousNullResponse() {
		JsoupResponse jsoupResponse = this.getJsoupResponse(
				ServiceResponseStatus.ERROR, null, null);
		JsoupStoreRepository jsoupStoreRepository = new JsoupStoreRepository();
		jsoupStoreRepository.setStoreServiceUrl(SERVICE_URL);
		jsoupHttpServiceContext.checking(new Expectations() {
			{
				exactly(2).of(jsoupHttpService).getData(with(SERVICE_URL));
				will(returnValue(jsoupResponse));
			}
		});
		jsoupStoreRepository.setJsoupHTTPService(jsoupHttpService);
		StoreResponse firstResponse = jsoupStoreRepository.getData();
		StoreResponse secondResponse = jsoupStoreRepository.getData();
		Date firstLastUpdated = firstResponse.getLastUpdated();
		Date secondLastUpdated = secondResponse.getLastUpdated();

		Assert.assertNotSame(firstLastUpdated.getTime(),
				secondLastUpdated.getTime());
	}

	@Test
	public void testGetDataServiceCachedValidResponse() {
		JsoupResponse jsoupOkResponse = this.getJsoupResponse(
				ServiceResponseStatus.OK, null, null);
		JsoupResponse jsoupErrorResponse = this.getJsoupResponse(
				ServiceResponseStatus.ERROR, null, null);
		JsoupStoreRepository jsoupStoreRepository = new JsoupStoreRepository();
		jsoupStoreRepository.setStoreServiceUrl(SERVICE_URL);
		jsoupHttpServiceContext.checking(new Expectations() {
			{
				oneOf(jsoupHttpService).getData(with(SERVICE_URL));
				will(returnValue(jsoupOkResponse));
				oneOf(jsoupHttpService).getData(with(SERVICE_URL));
				will(returnValue(jsoupErrorResponse));
			}
		});
		jsoupStoreRepository.setJsoupHTTPService(jsoupHttpService);
		StoreResponse firstResponse = jsoupStoreRepository.getData();
		jsoupStoreRepository.getDataFromService();
		StoreResponse secondResponse = jsoupStoreRepository.getData();

		Assert.assertEquals(firstResponse.getStatus(), ServiceResponseStatus.OK);
		Assert.assertEquals(secondResponse.getStatus(),
				ServiceResponseStatus.OK);
	}

	private JsoupResponse getJsoupResponse(ServiceResponseStatus status,
			String errorMessage, Document htmlDoc) {
		JsoupResponse jsoupResponse = new JsoupResponse();
		jsoupResponse.setStatus(status);
		jsoupResponse.setErrorMessage(errorMessage);
		jsoupResponse.setHtmlDoc(htmlDoc);
		return jsoupResponse;
	}

	private JsoupStoreRepository getJsoupRepositoryWithContextExpectations(
			JsoupResponse jsoupResponse) {
		JsoupStoreRepository jsoupStoreRepository = new JsoupStoreRepository();
		jsoupStoreRepository.setStoreServiceUrl(SERVICE_URL);
		jsoupHttpServiceContext.checking(new Expectations() {
			{
				oneOf(jsoupHttpService).getData(with(SERVICE_URL));
				will(returnValue(jsoupResponse));
			}
		});
		jsoupStoreRepository.setJsoupHTTPService(jsoupHttpService);
		return jsoupStoreRepository;
	}
}
