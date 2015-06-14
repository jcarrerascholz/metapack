package com.metapack.stores.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metapack.stores.model.Store;
import com.metapack.stores.repository.IRepository;
import com.metapack.stores.request.StoreResponse;

/**
 * @author jcarrera
 *
 */
@Service
public class StoreService implements IService<StoreResponse> {

	@Autowired
	private IRepository<StoreResponse> storeDataRepository;

	/*
	 * @return @StoreResponse with all results.
	 */
	@Override
	public StoreResponse getAll() {
		return storeDataRepository.getData();
	}

	/*
	 * @return @StoreResponse filtered by store name.
	 * 
	 * @name String filter.
	 */
	@Override
	public StoreResponse findByName(String name) {
		StoreResponse result = this.getAll();
		if (name != null && !"".equals(name) && result != null
				&& result.getStores() != null) {
			result = filterResult(name, result);
		}
		return result;
	}

	/**
	 * @param name
	 * @param result
	 * @return @StoreResponse Filter stores by store name using lambda
	 *         expression.
	 */
	private StoreResponse filterResult(String name, StoreResponse result) {
		StoreResponse filteredStoreResponse = new StoreResponse();
		BeanUtils.copyProperties(result, filteredStoreResponse);
		Stream<Store> stream = result.getStores().stream();
		List<Store> stores = stream.filter(
				item -> item.getCityName().toLowerCase()
						.contains(name.toLowerCase())).collect(
				Collectors.toList());
		filteredStoreResponse.setStores(stores);
		return filteredStoreResponse;
	}

	public IRepository<StoreResponse> getStoreDataRepository() {
		return storeDataRepository;
	}

	public void setStoreDataRepository(
			IRepository<StoreResponse> storeDataRepository) {
		this.storeDataRepository = storeDataRepository;
	}

}
