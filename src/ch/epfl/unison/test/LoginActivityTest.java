package ch.epfl.unison.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ch.epfl.unison.R;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.api.UnisonAPI;
import ch.epfl.unison.ui.LoginActivity;

import com.google.gson.JsonNull;
import com.jayway.android.robotium.solo.Solo;

public class LoginActivityTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {

	public LoginActivityTest() {
		super(LoginActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		UnisonAPI.DEBUG = true;
		super.setUp();
	}

	public void testLoginSuccess() throws JSONException,
			UnsupportedEncodingException {
		String loginResponseContent = "{" +
				"\"gid\": null," +
				"\"nickname\": \"user0\"," +
				"\"uid\": 0" +
				"}";
		
		String putResponseContent = "{" +
				"\"success\": true" +
				"}";
		
		String groupsResponseContent = "{" +
				"\"groups\": [" +
				"{" +
				"\"nb_users\": 0, " +
				"\"gid\": 0, " +
				"\"name\": \"test0\", " +
				"\"distance\": null" +
				"}, " +
				"{" +
				"\"nb_users\": 1, " +
				"\"gid\": 1, " +
				"\"name\": \"test1\", " +
				"\"distance\": null" +
				"}" +
				"]" +
				"}";
		
//		JSONObject loginResponseContent = new JSONObject().put("gid", JsonNull.INSTANCE)
//				.put("nickname", "user0").put("uid", "0");

//		JSONObject putResponseContent = new JSONObject().put("success", true);

		
//		JSONArray groupsResponseContent = new JSONArray().put(new JSONObject()
//				.put("nb_users", 0).put("gid", 0).put("name", "test1")
//				.put("distance", JsonNull.INSTANCE))
//				.put(new JSONObject()
//				.put("nb_users", 1).put("gid", 1).put("name", "test2")
//				.put("distance", JsonNull.INSTANCE));

		HttpClient mockClient = mock(HttpClient.class);

		HttpResponse loginSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		loginSuccess
				.setEntity(new StringEntity(loginResponseContent));

		HttpResponse putSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		putSuccess.setEntity(new StringEntity(putResponseContent));
		
		HttpResponse groupsSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		groupsSuccess.setEntity(new StringEntity(groupsResponseContent));

		try {
			when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(
					loginSuccess, putSuccess, groupsSuccess);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpClientFactory.setInstance(mockClient);

		Solo solo = new Solo(getInstrumentation(), getActivity());

		Button loginBtn = (Button) solo.getView(R.id.loginBtn);

		solo.enterText(0, "theUser");
		solo.enterText(1, "thePassWord");

		assertTrue(loginBtn.isEnabled());
		solo.clickOnText("Log In");

		solo.waitForText("Welcome");

		solo.finishOpenedActivities();
	}

	@Override
	protected void tearDown() {
		UnisonAPI.DEBUG = false;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}
}
