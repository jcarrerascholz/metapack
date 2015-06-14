package com.metapack.stores.service;


/**
 * @author jcarrera
 *
 * @param <T>
 */
public interface IService<T> {

	public T getAll();
	public T findByName(String Name);
}
