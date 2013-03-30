package ch.epfl.unison.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ch.epfl.unison.Const;
import ch.epfl.unison.Const.PrefKeys;
import ch.epfl.unison.R;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.api.JsonStruct;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.MainActivity;

import com.jayway.android.robotium.solo.Solo;

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
	
	private boolean isDJ = false;
	private int skippedSongsNumber = 0;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		mockResponses = new MockResponses();
		
		JsonStruct.Group group = new JsonStruct.Group();
		group.nbUsers = 0;
		group.gid = gid;
		group.distance = null;
		group.name = gname;

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
				Const.Strings.NAME, gname).putExtra(Const.Strings.GROUP, group);
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
							return mockResponses.mainGETGroupsNotDJSuccess;
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
                            return mockResponses.mainGETGroupsNotDJSuccess;
                        }
                    }
                });
        
        HttpClientFactory.setInstance(mockClient);
        
        getActivity().startActivity(intent);
        Solo solo = new Solo(getInstrumentation());
        
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
	
	//We can make this test more precise by looking at the entity of the request with path "current"
	//and see whether the song that is being played is indeed the one we expected.
	//CAUTION: order is important in the Answer implementation
	//CAUTION: This test depends on your music library. You have to customize this test and the responses in
	//         MockResponses.java
	
	//TODO - Handle POST requests to groupstreamer.com/libentries/0/batch
	//     - Make different cases when a track is being played so that it is shown when we fetch the group info
	//       from the server. ( GET request to /groups/0 )
	//     - Test the seekbar (Robotium can wait for a log. It might be useful).

//	public void testPlayer() throws ClientProtocolException, IOException, InterruptedException {
//	        
//		 HttpClient mockClient = mock(HttpClient.class);
//		 
//		    when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(
//	                new Answer<HttpResponse>() {
//	                    @Override
//	                    public HttpResponse answer(final InvocationOnMock invocation)
//	                            throws Throwable {
//	                        HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];
//	                        String requestPath = input.getURI().getPath();
//	                        if (requestPath == null) {
//	                        	return new BasicHttpResponse(new ProtocolVersion(
//										"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "Bad request, die! Request path was null ");
//	                        }
//	                        
//	                        if (requestPath.endsWith("groups")) {
//	                            return mockResponses.groupsGETSuccess;
//	                        } else if (requestPath.endsWith("master")){
//	                        	isDJ = true;
//	                            return mockResponses.PUTMasterSuccess;
//	                        } else if (requestPath.endsWith("playlist")) {
//	                        	return mockResponses.GETPlaylistSuccess;
//	                        } else if (requestPath.contains("tracks")){
//	                        	switch (skippedSongsNumber) {
//	                        	case 0: return mockResponses.GET1st2ndTrack;
//	                        	case 1: return mockResponses.GET2nd3rdTrack;
//	                        	case 2: return mockResponses.GET3rdTrack;
//	                        	case 3: return  mockResponses.GETNoMoreTracks;
//	                        	default:
//	                        		return new BasicHttpResponse(new ProtocolVersion(
//											"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "Bad request, die! Problem with number of skipped tracks.");
//	                        	}
//	                        }  else if (requestPath.endsWith("current")) {
//	                        	return mockResponses.PUTCurrentSuccess;
//	                        } else if (requestPath.contains("groups/")){
//	                            if (isDJ) {
//	                            	return mockResponses.mainGETGroupsDJSuccess;
//	                            } else {
//	                            	return mockResponses.mainGETGroupsNotDJSuccess;
//	                            }
//	                        } else {
//	                        	return new BasicHttpResponse(new ProtocolVersion(
//										"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "Bad request, die! Asked : " + input.getURI().getPath());
//	                        }
//	                    }
//	                });
//	        
//	        HttpClientFactory.setInstance(mockClient);
//	        
//	        getActivity().startActivity(intent);
//	        Solo solo = new Solo(getInstrumentation());
//	        
//	        assertTrue(solo.waitForActivity("MainActivity"));
//	        Button playButton = (Button) solo.getView(R.id.musicToggleBtn);
//	        Button nextButton = (Button) solo.getView(R.id.musicNextBtn);
//	        Button prevButton = (Button) solo.getView(R.id.musicPrevBtn);
//	        
//	        
//	        solo.clickOnText("Become the DJ");
//	        
//	        //let the servercom finish
//	        Thread.sleep(3000);
//	        
//	        
//	        solo.clickOnView(playButton);
//
//	        //Enjoy the music :)
//	        Thread.sleep(3000);
//	        
//	        assertTrue(solo.waitForText("Rihanna"));
//	        
//	        skippedSongsNumber ++;
//	        solo.clickOnView(nextButton);
//	        
//	        Thread.sleep(3000);
//	        
//	        assertTrue(solo.waitForText("Bruno"));
//	        
//	        Thread.sleep(2000);
//	        
//	        skippedSongsNumber ++;
//	        solo.clickOnView(nextButton);
//	        
//	        Thread.sleep(6000);
//	        
//	        //click twice because we want to hear the previous song.
//	        solo.clickOnView(prevButton);
//	        solo.clickOnView(prevButton);
//	        
//	        Thread.sleep(2000);
//	        
//	        
//	        //If you hear that, then the pause button is working :)
//	        solo.clickOnView(playButton);
//	        Thread.sleep(500);
//	        solo.clickOnView(playButton);
//	        Thread.sleep(500);
//	        solo.clickOnView(playButton);
//	        Thread.sleep(500);
//	        solo.clickOnView(playButton);
//	        Thread.sleep(500);
//	        solo.clickOnView(playButton);
//	        Thread.sleep(500);
//	        
//	        solo.finishOpenedActivities();
//	        
//	}
	
	

	@Override
	protected void tearDown() {
		isDJ = false;
		skippedSongsNumber = 0;
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getInstrumentation().getTargetContext());
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

}
