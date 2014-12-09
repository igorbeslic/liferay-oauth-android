package com.liferay.oauth.demo.task;

import android.app.Activity;

import android.os.AsyncTask;
import com.liferay.oauth.demo.LCSActivity;
import com.liferay.oauth.demo.OAuthData;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author Igor Beslic
 */
public class AccessTokenTask extends AsyncTask<OAuthService, Void, String> {

	public AccessTokenTask(OAuthData oAuthData, Activity parentActivity) {

		_oAuthData = oAuthData;

		_parentActivity = parentActivity;
	}

	@Override
	protected String doInBackground(OAuthService... params) {

		Verifier verifier = new Verifier(_oAuthData.oauthVerifier);

		_oAuthData.accessToken = params[0].getAccessToken(
			_oAuthData.requestToken, verifier);

		_oAuthData.accessTokenString = _oAuthData.accessToken.getToken();
		_oAuthData.accessTokenSecret = _oAuthData.accessToken.getSecret();

		return _oAuthData.accessToken.getToken();
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);

		if (s != null) {
			((LCSActivity)_parentActivity).doStorePreferences();
		}
	}

	private OAuthData _oAuthData;

	private Activity _parentActivity;
}
