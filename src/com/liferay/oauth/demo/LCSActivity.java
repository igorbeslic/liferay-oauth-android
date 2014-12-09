package com.liferay.oauth.demo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.liferay.oauth.demo.api.DemoAPI;
import com.liferay.oauth.demo.task.AccessDLTask;
import com.liferay.oauth.demo.task.AccessTokenTask;
import com.liferay.oauth.demo.task.RequestTokenTask;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class LCSActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		doRestorePreferences();

		doSetupBaseUI();

		doSetupExtendedUI();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Uri uri = getIntent().getData();

		if (uri == null) {
			return;
		}

		getOAuthData().oauthToken = uri.getQueryParameter("oauth_token");
		getOAuthData().oauthVerifier = uri.getQueryParameter("oauth_verifier");

		if (getOAuthData().accessTokenString != null) {
			inflateOAuthData();
		}
	}

	private void inflateOAuthData() {
		if (getOAuthData().accessTokenString != null) {

			TextView textViewHelper =
				(TextView)findViewById(R.id.oauthTokenBox);

			if (getOAuthData().oauthToken != null) {
				textViewHelper.setText(getOAuthData().oauthToken);
			}

			textViewHelper = (TextView)findViewById(R.id.oauthVerifierBox);

			textViewHelper.setText(getOAuthData().oauthVerifier);

			Button portalDataButton =
				(Button)findViewById(R.id.portalDataButton);

			portalDataButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String command =
						"http://10.0.2.2:8080/api/jsonws/" +
							"dlapp/get-folders/repositoryId/10180" +
							"/parentFolderId/0";

						new AccessDLTask(
							getOAuthData(), getOAuthService(),
							LCSActivity.this)
							.execute(command);
					}
				});

			Button clearPreferencesButton = (Button)findViewById(
				R.id.clearPreferencesButton);

			clearPreferencesButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						doClearPreferences();

						doSetupBaseUI();
					}
				});
		}
	}

	public void doClearPreferences() {
		SharedPreferences preferences = getSharedPreferences(
			PREFERENCES_NAME, 0);

		final SharedPreferences.Editor editor =  preferences.edit();

		editor.remove("access_token");
		editor.remove("access_token_secret");

		editor.commit();
	}

	private void doRestorePreferences() {
		SharedPreferences preferences = getSharedPreferences(
			PREFERENCES_NAME, 0);

		OAuthData oAuthData = getOAuthData();

		oAuthData.accessTokenString = preferences.getString(
			"access_token", null);
		oAuthData.accessTokenSecret = preferences.getString(
			"access_token_secret", null);

		if (oAuthData.valid()) {
			oAuthData.accessToken = new Token(
			oAuthData.accessTokenString, oAuthData.accessTokenSecret);
		}
	}

	public void doStorePreferences() {
		SharedPreferences preferences = getSharedPreferences(
			PREFERENCES_NAME, 0);

		final SharedPreferences.Editor editor =  preferences.edit();

		OAuthData oAuthData = getOAuthData();

		if (oAuthData.valid()) {
			editor.putString("access_token", oAuthData.accessTokenString);
			editor.putString(
				"access_token_secret", oAuthData.accessTokenSecret);

			editor.commit();

			doSetupBaseUI();
			doSetupExtendedUI();
			inflateOAuthData();
		}
	}



	private void doSetupBaseUI() {
		Button requestTokenButton = (Button)findViewById(R.id.startButton);

		Button authorizeTokenButton = (Button)findViewById(R.id.completeButton);

		if (getOAuthData().valid()) {
			requestTokenButton.setEnabled(false);
			authorizeTokenButton.setEnabled(false);
		}
		else {
			requestTokenButton.setEnabled(true);
			requestTokenButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						new RequestTokenTask(getOAuthData(), LCSActivity.this)
							.execute(getOAuthService());
					}
				});

			authorizeTokenButton.setEnabled(true);
			authorizeTokenButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						new AccessTokenTask(getOAuthData(), LCSActivity.this)
							.execute(getOAuthService());
					}
				});
		}
	}

	private void doSetupExtendedUI() {
		if (getOAuthData().accessTokenString != null) {
			ViewGroup lcsActivityLayout = (ViewGroup)findViewById(
				R.id.mainLayout);

			getLayoutInflater().inflate(
				R.layout.verify_url_form, lcsActivityLayout, true);

			inflateOAuthData();
		}
	}

	private static OAuthData getOAuthData() {
		if (_oauthData == null) {
			_oauthData = new OAuthData();
		}

		return _oauthData;
	}

	private static OAuthService getOAuthService() {
		if (_oAuthService == null) {
			_oAuthService = new ServiceBuilder()
				.provider(DemoAPI.class)
				.apiKey(OAuthData.CONSUMER_KEY)
				.apiSecret(OAuthData.CONSUMER_SECRET)
				.build();
		}

		return _oAuthService;
	}

	private static final String PREFERENCES_NAME = "lcs-preferences";

	private static OAuthData _oauthData;

	private static OAuthService _oAuthService;

}
