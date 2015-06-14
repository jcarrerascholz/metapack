package com.metapack.stores.request;

import com.metapack.stores.helper.ServiceResponseStatus;

/**
 * @author jcarrera
 *
 */
public class BaseResponse {

	private String errorMessage;
	private ServiceResponseStatus status;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ServiceResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceResponseStatus status) {
		this.status = status;
	}

}
