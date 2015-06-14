package com.metapack.test.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.metapack.stores.helper.ServiceResponseStatus;
import com.metapack.stores.model.Store;
import com.metapack.stores.repository.IRepository;
import com.metapack.stores.request.StoreResponse;
import com.metapack.stores.service.StoreService;

/**
 * @author jcarrera
 *
 */
public class StoreServiceTest {
	Mockery storeRepositoryContext = new Mockery();
	IRepository<StoreResponse> storeRepository;

	@Before
	@SuppressWarnings("unchecked")
	public void doBeforeEachTest() {
		storeRepository = storeRepositoryContext.mock(IRepository.class);
	}

	@Test
	public void testGetAllStatusResponse() {
		StoreService storeService = setStoreResponseWithExpectations(
				ServiceResponseStatus.OK, null, null, null);
		StoreResponse response = storeService.getAll();
		Assert.assertEquals(response.getStatus(), ServiceResponseStatus.OK);
	}

	@Test
	public void testGetAllMessageResponse() {
		StoreService storeService = setStoreResponseWithExpectations(null,
				"Test", null, null);
		StoreResponse response = storeService.getAll();
		Assert.assertEquals(response.getErrorMessage(), "Test");
	}

	@Test
	public void testGetAllLastUpdatedResponse() {
		Date date = new Date();
		StoreService storeService = setStoreResponseWithExpectations(null,
				null, date, null);
		StoreResponse response = storeService.getAll();
		Assert.assertEquals(response.getLastUpdated(), date);
	}

	@Test
	public void testGetAllStoresResponse() {
		String[] names = { "Aberdeen", "Peterhead" };
		List<Store> stores = getStores(names);
		StoreService storeService = setStoreResponseWithExpectations(null,
				null, null, stores);
		StoreResponse response = storeService.getAll();
		Assert.assertEquals(response.getStores().size(), 2);
		Assert.assertEquals(response.getStores().get(0).getCityName(), "Aberdeen");
		Assert.assertEquals(response.getStores().get(1).getCityName(), "Peterhead");
	}

	@Test
	public void testGetAllNullResponse() {
		StoreService storeService = new StoreService();
		storeRepositoryContext.checking(new Expectations() {
			{
				oneOf(storeRepository).getData();
				will(returnValue(null));
			}
		});
		storeService.setStoreDataRepository(storeRepository);
		StoreResponse response = storeService.getAll();
		Assert.assertNull(response);
		;
	}
	
	@Test
	public void testFindByNameFilteredResponseWithValidArgument() {
		String[] names = { "Aberdeen", "Peterhead" };
		List<Store> stores = getStores(names);
		StoreService storeService = setStoreResponseWithExpectations(null,
				null, null, stores);
		StoreResponse response = storeService.findByName("Aberdeen");
		Assert.assertEquals(response.getStores().size(), 1);
		Assert.assertEquals(response.getStores().get(0).getCityName(), "Aberdeen");
	}
	
	@Test
	public void testFindByNameFilteredResponseStartingWithArgument() {
		String[] names = { "Aberdeen", "Peterhead" };
		List<Store> stores = getStores(names);
		StoreService storeService = setStoreResponseWithExpectations(null,
				null, null, stores);
		StoreResponse response = storeService.findByName("Aber");
		Assert.assertEquals(response.getStores().size(), 1);
		Assert.assertEquals(response.getStores().get(0).getCityName(), "Aberdeen");
	}
	
	@Test
	public void testFindByNameFilteredResponseEndinggWithArgument() {
		String[] names = { "Aberdeen", "Peterhead" };
		List<Store> stores = getStores(names);
		StoreService storeService = setStoreResponseWithExpectations(null,
				null, null, stores);
		StoreResponse response = storeService.findByName("deen");
		Assert.assertEquals(response.getStores().size(), 1);
		Assert.assertEquals(response.getStores().get(0).getCityName(), "Aberdeen");
	}
	
	@Test
	public void testFindByNameFilteredResponseWithNullArgument() {
		String[] names = { "Aberdeen", "Peterhead" };
		List<Store> stores = getStores(names);
		StoreService storeService = setStoreResponseWithExpectations(null,
				null, null, stores);
		StoreResponse response = storeService.findByName(null);
		Assert.assertEquals(response.getStores().size(), 2);
		Assert.assertEquals(response.getStores().get(0).getCityName(), "Aberdeen");
		Assert.assertEquals(response.getStores().get(1).getCityName(), "Peterhead");
	}

	private StoreService setStoreResponseWithExpectations(
			ServiceResponseStatus status, String message, Date lastUpdated,
			List<Store> stores) {
		StoreService storeService = new StoreService();
		StoreResponse response = new StoreResponse();
		response.setStatus(status);
		response.setErrorMessage(message);
		response.setLastUpdated(lastUpdated);
		response.setStores(stores);
		storeRepositoryContext.checking(new Expectations() {
			{
				oneOf(storeRepository).getData();
				will(returnValue(response));
			}
		});
		storeService.setStoreDataRepository(storeRepository);
		return storeService;
	}

	private List<Store> getStores(String[] names) {
		List<Store> stores = new ArrayList<Store>();
		for (String name : names) {
			stores.add(getStore(name));
		}
		return stores;
	}

	private Store getStore(String name) {
		Store store = new Store();
		store.setCityName(name);
		return store;
	}

}
