package com.metapack.stores.helper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.metapack.stores.model.Store;

/**
 * @author jcarrera
 *
 */
public class JsoupRepositoryHelper {
	
	private static final int POST_CODE = 1;
	private static final int CITY_NAME = 2;
	private static final int ADDRESS = 3;
	private static final int LONGITUDE = 4;
	private static final int LATITUDE = 5;
	private static final String HTML_TD = "td";
	private static final String HTML_TR = "tr";
	private static final String HTML_TABLE = "table";
	
	/**
	 * @param Jsoup HTML doc
	 * @return An @ArrayList of @Store
	 */
	public static List<Store> extractStoresFromHTMLDocument(Document doc) {
		List<Store> stores = null;
		if (doc != null) {
			stores = new ArrayList<Store>();
			for (Element table : doc.select(HTML_TABLE)) {
				for (Element row : table.select(HTML_TR)) {
					stores.add(extractStore(row.select(HTML_TD)));
				}
			}
		}
		return stores;
	}
	
	/**
	 * @param Jsoup HTML tds
	 * @return @Store
	 */
	public static Store extractStore(Elements tds) {
		Store store = new Store();
		store.setPostCode(tds.get(POST_CODE).text());
		store.setCityName(tds.get(CITY_NAME).text());
		store.setAddress(tds.get(ADDRESS).text());
		store.setLongitude(tds.get(LONGITUDE).text());
		store.setLatitude(tds.get(LATITUDE).text());
		return store;
	}
}
