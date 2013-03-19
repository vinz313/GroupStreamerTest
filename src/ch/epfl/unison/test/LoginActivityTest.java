package ch.epfl.unison.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ch.epfl.unison.Const;
import ch.epfl.unison.R;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.LoginActivity;

import com.jayway.android.robotium.solo.Solo;

public class LoginActivityTest extends
ActivityInstrumentationTestCase2<LoginActivity> {

	private MockResponses mockResponses;
	private HashMap<String, HttpResponse> responseMap;
	
	
	public LoginActivityTest() {
		super(LoginActivity.class);
	}

	@Override
	public void setUp() throws Exception {
//		UnisonAPI.DEBUG = true;
		mockResponses = new MockResponses();
		responseMap = mockResponses.responseMap;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getInstrumentation().getTargetContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Const.PrefKeys.HELPDIALOG, false);
        editor.commit();
	}

	public void testLoginSuccess() throws JSONException,
			UnsupportedEncodingException {

		//Creating the mock server:
		HttpClient mockClient = mock(HttpClient.class);

		

		try {
//			when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(
//					loginSuccess);
			
// Replaced by this multi purpose mock which can even handle communication we don't want to test (automatic comm).
			when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(
					new Answer<HttpResponse>()
					{
						@Override
						public HttpResponse answer(final InvocationOnMock invocation) throws Throwable
						{
							HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];
							
							//This complex check is for the login comm, the path should always be "/" but I'm not sure and it might change
							if (input.getURI().getPath() == null || input.getURI().getPath().isEmpty() || input.getURI().getPath().equals("/"))
							{
								return responseMap.get(mockResponses.LOGIN_KEY);
							}
							else if (input.getURI().getPath().contains("libentries"))
							{
								return responseMap.get(mockResponses.LIBENTRIES_KEY);
							}
							else if (input.getURI().getPath().contains("groups"))
							{
								return responseMap.get(mockResponses.GROUPS_SUCC_KEY);
							}
							else
							{
								return new BasicHttpResponse(new ProtocolVersion(
										"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "Bad request, die! Asked : " + input.getURI().getPath()); // alternatively, you could throw an exception
							}
						}
					}
					);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpClientFactory.setInstance(mockClient);
		
		//Mock done

		//Launch test
		Solo solo = new Solo(getInstrumentation(), getActivity());

		Button loginBtn = (Button) solo.getView(R.id.loginBtn);

		solo.enterText(0, "theUser");
		solo.enterText(1, "thePassWord");

		assertTrue(loginBtn.isEnabled());
		solo.clickOnText("Log In");

		assertTrue(solo.waitForActivity("GroupsActivity"));

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		solo.finishOpenedActivities();
		
		
	}
	
	public void testLoginFailureBadReq() {
		HttpClient mockClient = mock(HttpClient.class);
		
		HttpResponse badReq = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "Bad request, die!");
		
		try {
			when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(badReq);
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
		
		assertTrue(solo.waitForText("Unable to log in. Are you connected ?"));
		
		solo.finishOpenedActivities();
	}
	
	public void testLoginFailureEmptyEntity() {
		HttpClient mockClient = mock(HttpClient.class);
		
		HttpResponse badReq = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		
		try {
			when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(badReq);
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
		
		assertTrue(solo.waitForText("Unable to log in. Are you connected ?"));
		
		solo.finishOpenedActivities();
	}
	
	public void testLoginFailureBadEntity() throws UnsupportedEncodingException {
		HttpClient mockClient = mock(HttpClient.class);
		
		HttpResponse loginSuccess = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
		loginSuccess
				.setEntity(new StringEntity("malformed json"));
		
		try {
			when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(loginSuccess);
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
		
		assertTrue(solo.waitForText("Unable to log in. Are you connected ?"));
		
		solo.finishOpenedActivities();
	}

	@Override
	public void tearDown() {
//		UnisonAPI.DEBUG = false;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getInstrumentation().getTargetContext());
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}
}
