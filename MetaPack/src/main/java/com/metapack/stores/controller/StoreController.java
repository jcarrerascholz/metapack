package com.metapack.stores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.metapack.stores.helper.ServiceResponseStatus;
import com.metapack.stores.request.StoreResponse;
import com.metapack.stores.service.IService;

/**
 * @author jcarrera
 *
 */
@Controller
public class StoreController {

	@Autowired
	private IService<StoreResponse> storeService;
	private static final String STORES_PATH = "/stores";

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = STORES_PATH, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StoreResponse list(
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			Model model) {
		return storeService.findByName(name);
	}

	@RequestMapping(value = STORES_PATH)
	public String listWithView(
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			Model model) {
		StoreResponse result = storeService.findByName(name);
		model.addAttribute("response", result);
		return result.getStatus() == ServiceResponseStatus.OK ? "store_list" : "service_error";
	}

}
