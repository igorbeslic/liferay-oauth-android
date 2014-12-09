package com.liferay.oauth.demo.task;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import com.liferay.oauth.demo.OAuthData;
import com.liferay.oauth.demo.api.DemoAPI;
import org.scribe.oauth.OAuthService;

/**
 * @author Igor Beslic
 */
public class RequestTokenTask extends AsyncTask<OAuthService, Void, String> {

	public RequestTokenTask(OAuthData oAuthData, Activity parentActivity) {
		_oAuthData = oAuthData;
		_parentActivity = parentActivity;
	}

	@Override
	protected String doInBackground(OAuthService... params) {
		_oAuthData.requestToken = params[0].getRequestToken();

		return DemoAPI.AUTHORIZE_URL.replace(
			"%s", _oAuthData.requestToken.getToken());
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);

		_oAuthData.oauthAuthorizeUrl = s;

		_parentActivity.startActivity(new Intent(
			"android.intent.action.VIEW", Uri.parse(s)));
	}

	private OAuthData _oAuthData;
	private Activity _parentActivity;

}
