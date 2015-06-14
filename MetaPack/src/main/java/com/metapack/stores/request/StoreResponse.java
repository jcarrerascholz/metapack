package com.metapack.stores.request;

import java.util.Date;
import java.util.List;

import com.metapack.stores.model.Store;

/**
 * @author jcarrera
 *
 */
public class StoreResponse extends BaseResponse {

	private List<Store> stores;
	private Date lastUpdated;
	
	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> result) {
		this.stores = result;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
