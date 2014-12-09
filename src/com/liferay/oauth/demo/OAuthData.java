package com.liferay.oauth.demo;

import org.scribe.model.Token;

/**
 * @author Igor Beslic
 * */
public class OAuthData {

	protected static final String CONSUMER_KEY =
		"15cfe73d-3db4-4cfa-a1b0-65d8d77d3307";

	protected static final String CONSUMER_SECRET =
		"5b94432f6317c6792ecf60155d317138";

	public String accessTokenString;

	public String accessTokenSecret;

	public Token accessToken;

	public Token requestToken;

	public String oauthAuthorizeUrl;

	public String oauthToken;

	public String oauthVerifier;

	public boolean valid() {
		return (accessTokenString != null) && (accessTokenSecret != null);
	}
}
