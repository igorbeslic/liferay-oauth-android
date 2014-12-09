package com.liferay.oauth.demo.api;

import org.scribe.builder.api.DefaultApi10a;

/**
 * @author Igor Beslic
 */
public class DemoAPI extends DefaultApi10a {
	public static final String AUTHORIZE_HOST = "10.0.2.2";

	public static final String AUTHORIZE_PORT = "8080";

	public static final String AUTHORIZE_URL =
		"http://".concat(AUTHORIZE_HOST).concat(":").concat(AUTHORIZE_PORT)
			.concat("/c/portal/oauth/authorize?oauth_token=%s");

	@Override
	public String getAccessTokenEndpoint() {
		return "http://".concat(AUTHORIZE_HOST).concat(":")
			.concat(AUTHORIZE_PORT)
			.concat("/c/portal/oauth/access_token");
	}

	@Override
	public String getRequestTokenEndpoint() {
		return "http://".concat(AUTHORIZE_HOST).concat(":")
			.concat(AUTHORIZE_PORT)
			.concat("/c/portal/oauth/request_token");
	}

}
