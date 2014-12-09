package com.liferay.oauth.demo.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.liferay.oauth.demo.OAuthData;
import com.liferay.oauth.demo.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Igor Beslic
 */
public class AccessDLTask extends AsyncTask<String, Void, String> {

	public AccessDLTask(
			OAuthData oAuthData, OAuthService oAuthService,
			Activity parentActivity) {

		_oAuthData = oAuthData;

		_oAuthService = oAuthService;

		_parentActivity = parentActivity;
	}

	@Override
	protected String doInBackground(String... params) {
		String commandURL = params[0];

		String result;

		try {
			OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST, commandURL);

			_oAuthService.signRequest(_oAuthData.accessToken, oAuthRequest);

			Response response = oAuthRequest.send();

			result = response.getBody();
		}
		catch (Exception e) {
			result = e.getMessage();
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(
			_parentActivity.getApplicationContext());

		JSONParser jsonParser = new JSONParser();

		StringBuffer sb = new StringBuffer();

		sb.append("Request made on: ");
		sb.append(dateFormat.format(new Date())).append("\n");

		try{
			JSONArray jsonArray = (JSONArray)jsonParser.parse(result);

			int size = jsonArray.size();

			sb.append("Total folders: ");
			sb.append(String.valueOf(size)).append("\n");
			sb.append("Folder list: ").append("\n");

			for (int idx = 0; idx < size; idx++) {
				JSONObject jsonObject = (JSONObject)jsonArray.get(idx);
				sb.append(
					jsonObject.get("name"))
						.append(" - ")
						.append(jsonObject.get("description"))
						.append("\n");
			}
		}
		catch (ParseException pe) {
			pe.printStackTrace();

			sb.append("RECEIVED: ").append(result).append("\n");
			sb.append("ERROR   : ").append(pe.getMessage());
		}

		TextView portalDataText = (TextView)_parentActivity.findViewById(
			R.id.portalDataText);

		portalDataText.setText(sb.toString());
	}

	private OAuthData _oAuthData;

	private OAuthService _oAuthService;

	private Activity _parentActivity;
}
