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

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.unison.Const.PrefKeys;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.GroupsActivity;

import com.jayway.android.robotium.solo.Solo;

public class GroupsActivityTest extends ActivityInstrumentationTestCase2<GroupsActivity> {
	
	private MockResponses mockResponses;
	private HashMap<String, HttpResponse> responseMap;
	
	private final String email = "test@test.test";
	private final String password = "pw";
	private final String nickname = "nick";
	private final Long uid = Long.valueOf(0);

	public GroupsActivityTest() {
		super(GroupsActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		mockResponses = new MockResponses();
		responseMap = mockResponses.responseMap;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getInstrumentation().getTargetContext());
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PrefKeys.EMAIL, email);
        editor.putString(PrefKeys.PASSWORD, password);
        editor.putString(PrefKeys.NICKNAME, nickname);
        editor.putLong(PrefKeys.UID, uid != null ? uid : -1);
        editor.putBoolean(PrefKeys.HELPDIALOG, false);
        editor.commit();
	}
	
	public void testGroupsToDisplay() throws UnsupportedEncodingException {
		//Creating the mock server:
				HttpClient mockClient = mock(HttpClient.class);

				

				try {
					when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(responseMap.get(mockResponses.GROUPS_SUCC_KEY));
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				HttpClientFactory.setInstance(mockClient);
				
				Solo solo = new Solo(getInstrumentation(), getActivity());
				
				assertTrue(solo.waitForText("test1"));
				
				solo.finishOpenedActivities();
	}
	
	public void testGroupsFailEmptyEntity() throws UnsupportedEncodingException {
		//Creating the mock server:
				HttpClient mockClient = mock(HttpClient.class);

				HttpResponse groupsSuccess = new BasicHttpResponse(new ProtocolVersion(
						"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
//				groupsSuccess.setEntity(new StringEntity(mockResponses.groupsGETResponseContent
//						.toString()));

				try {
					when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(groupsSuccess);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				HttpClientFactory.setInstance(mockClient);
				
				Solo solo = new Solo(getInstrumentation(), getActivity());
				
				assertTrue(solo.waitForText("Unable to load the list of groups."));
				
				solo.finishOpenedActivities();
	}
	
	public void testGroupsFailMalformedEntity() throws UnsupportedEncodingException {
		//Creating the mock server:
				HttpClient mockClient = mock(HttpClient.class);

				HttpResponse groupsSuccess = new BasicHttpResponse(new ProtocolVersion(
						"HTTP", 1, 1), HttpStatus.SC_OK, "OK");
				groupsSuccess.setEntity(new StringEntity("malformed json"));

				try {
					when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(groupsSuccess);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				HttpClientFactory.setInstance(mockClient);
				
				Solo solo = new Solo(getInstrumentation(), getActivity());
				
				assertTrue(solo.waitForText("Unable to load the list of groups."));
				
				solo.finishOpenedActivities();
	}
	
	public void testGroupsFailBadReq() throws UnsupportedEncodingException {
		//Creating the mock server:
				HttpClient mockClient = mock(HttpClient.class);

				HttpResponse groupsSuccess = new BasicHttpResponse(new ProtocolVersion(
						"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "NO!");

				try {
					when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(groupsSuccess);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				HttpClientFactory.setInstance(mockClient);
				
				Solo solo = new Solo(getInstrumentation(), getActivity());
				
				assertTrue(solo.waitForText("Unable to load the list of groups."));
				
				solo.finishOpenedActivities();
	}
	@Override
	protected void tearDown() {
//		UnisonAPI.DEBUG = false;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}
}
