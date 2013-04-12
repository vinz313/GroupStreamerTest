package ch.epfl.unison.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.unison.Const;
import ch.epfl.unison.Const.PrefKeys;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.MainActivity;
import ch.epfl.unison.R;

import com.jayway.android.robotium.solo.Solo;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MockResponses mockResponses;

	private final String email = "test@test.test";
	private final String password = "pw";
	private final String nickname = "nick";
	private final String gname = "cool group";
	private final Long gid = Long.valueOf(0);
	private final Long uid = Long.valueOf(0);

	private Intent intent;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		mockResponses = new MockResponses();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getInstrumentation()
						.getTargetContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PrefKeys.EMAIL, email);
		editor.putString(PrefKeys.PASSWORD, password);
		editor.putString(PrefKeys.NICKNAME, nickname);
		editor.putLong(PrefKeys.UID, uid != null ? uid : -1);
		editor.putBoolean(PrefKeys.HELPDIALOG, false);
		editor.commit();

		intent = new Intent(getInstrumentation().getTargetContext(),
				MainActivity.class).putExtra(Const.Strings.GID, gid).putExtra(
				Const.Strings.NAME, gname);
	}

	public void testTabsNav() throws ClientProtocolException, IOException,
			InterruptedException {
		HttpClient mockClient = mock(HttpClient.class);

		when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(
				new Answer<HttpResponse>() {
					@Override
					public HttpResponse answer(final InvocationOnMock invocation)
							throws Throwable {
						HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];
						
						if (input.getURI().getPath() == null || input.getURI().getPath().endsWith("groups")) {
							return mockResponses.groupsGETSuccess;
						} else {
							return mockResponses.mainGETGroupsSuccess;
						}
					}
				});

		HttpClientFactory.setInstance(mockClient);
		getActivity().startActivity(intent);
		Solo solo = new Solo(getInstrumentation());

		assertTrue(solo.waitForActivity("MainActivity"));

		solo.clickOnText(solo.getString(R.string.fragment_title_stats));

		solo.clickOnText(solo.getString(R.string.fragment_title_player));

		solo.finishOpenedActivities();
	}
	
	public void testMenu() throws ClientProtocolException, IOException {
	    HttpClient mockClient = mock(HttpClient.class);
        
	    when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(
                new Answer<HttpResponse>() {
                    @Override
                    public HttpResponse answer(final InvocationOnMock invocation)
                            throws Throwable {
                        HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];
                        
                        if (input.getURI().getPath() == null || input.getURI().getPath().endsWith("groups")) {
                            return mockResponses.groupsGETSuccess;
                        } else if (input.getURI().getPath().endsWith("nickname")){
                            return mockResponses.PUTNicknameSuccess;
                        } else {
                            return mockResponses.mainGETGroupsSuccess;
                        }
                    }
                });
        
        HttpClientFactory.setInstance(mockClient);
        
        getActivity().startActivity(intent);
        Solo solo = new Solo(getInstrumentation());
        
        solo.waitForActivity("MainActivity");
        
        solo.sendKey(Solo.MENU);
        solo.clickOnText("Settings");
        
        assertTrue(solo.waitForText("UUID"));
        
        solo.clickOnText("Nickname");
        
        solo.enterText(0, "shiggy");
        
        solo.clickOnText("OK");
        
        solo.goBack();
        
        assertTrue(solo.waitForActivity("MainActivity"));
        
        solo.finishOpenedActivities();
    }

	@Override
	protected void tearDown() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

}
