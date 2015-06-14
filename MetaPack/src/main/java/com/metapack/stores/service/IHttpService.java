package com.metapack.stores.service;

/**
 * @author jcarrera
 *
 * @param <T>
 * @param <I>
 */
public interface IHttpService<T,I> {

	public T getData(I input);
}
