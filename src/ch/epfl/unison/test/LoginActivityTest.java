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

		JSONObject loginResponseContent = new JSONObject()
				.put("gid", JSONObject.NULL).put("nickname", "user0")
				.put("uid", "0");

		JSONObject putResponseContent = new JSONObject().put("success", true);

		JSONObject groupsResponseContent = new JSONObject().put(
				"groups",
				new JSONArray().put(
						new JSONObject().put("nb_users", 0).put("gid", 0)
								.put("name", "test1")
								.put("distance", JSONObject.NULL)).put(
						new JSONObject().put("nb_users", 1).put("gid", 1)
								.put("name", "test2")
								.put("distance", JSONObject.NULL)));

		HttpClient mockClient = mock(HttpClient.class);

		HttpResponse loginSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		loginSuccess
				.setEntity(new StringEntity(loginResponseContent.toString()));

		HttpResponse putSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		putSuccess.setEntity(new StringEntity(putResponseContent.toString()));

		HttpResponse groupsSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		groupsSuccess.setEntity(new StringEntity(groupsResponseContent
				.toString()));

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

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
