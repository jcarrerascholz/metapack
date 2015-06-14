package com.metapack.stores.request;

import org.jsoup.nodes.Document;

/**
 * @author jcarrera
 *
 */
public class JsoupResponse extends BaseResponse{
	
	private Document htmlDoc;

	public Document getHtmlDoc() {
		return htmlDoc;
	}

	public void setHtmlDoc(Document htmlDoc) {
		this.htmlDoc = htmlDoc;
	}
}
