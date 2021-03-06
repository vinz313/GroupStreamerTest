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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ch.epfl.unison.Const.PrefKeys;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.GroupsActivity;

import com.jayway.android.robotium.solo.Solo;

public class GroupsActivityTest extends ActivityInstrumentationTestCase2<GroupsActivity> {
    
    private static final String TAG = "GroupsActivityTest";
	
	HttpClient mockClient;
	
	private MockResponses mockResponses;
	
	private final String email = "test@test.test";
	private final String password = "pw";
	private final String nickname = "nick";
	private final String newGroupName = "newGroup";
	private boolean newGroupCreated = false;
	private final Long uid = Long.valueOf(0);

	public GroupsActivityTest() {
		super(GroupsActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		mockClient = mock(HttpClient.class);
		
		mockResponses = new MockResponses();
		newGroupCreated = false;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getInstrumentation().getTargetContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PrefKeys.EMAIL, email);
        editor.putString(PrefKeys.PASSWORD, password);
        editor.putString(PrefKeys.NICKNAME, nickname);
        editor.putLong(PrefKeys.UID, uid);
        editor.putBoolean(PrefKeys.HELPDIALOG, false);
        editor.commit();
	}
	
	public void testGroupsToDisplay() throws UnsupportedEncodingException {
		//Creating the mock server:
				
				try {
					when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(mockResponses.groupsGETSuccessAfterCreation);
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
	
	public void testGroupsSuggestionSuccess() throws UnsupportedEncodingException {
	    try {
            when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(
                    new Answer<HttpResponse>() {

                        @Override
                        public HttpResponse answer(InvocationOnMock invocation) throws Throwable {
                            HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];

                            if (input.getURI().getPath() == null
                                    || (input.getURI().getPath().length() == 0)
                                    || input.getURI().getPath().equals("/"))
                            {
                                return mockResponses.loginGETSuccess;
                            }
                            if (input.getURI().getPath().contains("suggestion")) {
                                Log.d("TAG", "answering to suggestion request.");
                                return mockResponses.groupsGETSuggestion;
                            } else {
                                return mockResponses.groupsGETSuccess;
                            }
                        }
                    });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getInstrumentation().getTargetContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PrefKeys.GROUP_SUGGESTION, true);

        HttpClientFactory.setInstance(mockClient);
        
        Solo solo = new Solo(getInstrumentation(), getActivity());
        
        assertTrue(solo.waitForText("user1"));
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        solo.finishOpenedActivities();
	}
	
	public void testGroupsFailEmptyEntity() throws UnsupportedEncodingException {
		//Creating the mock server:
				

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

		mockClient = mock(HttpClient.class);
		HttpResponse groupsBad = new BasicHttpResponse(new ProtocolVersion(
				"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "NO!");

		try {
			when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(groupsBad);
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
	
	public void testMenu() throws ClientProtocolException, IOException {
	    mockClient = mock(HttpClient.class);
	    
	    when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(mockResponses.groupsGETSuccess, mockResponses.PUTNicknameSuccess, mockResponses.groupsGETSuccess);
	    
	    HttpClientFactory.setInstance(mockClient);
	    
	    Solo solo = new Solo(getInstrumentation(), getActivity());
	    
	    solo.sendKey(Solo.MENU);
	    solo.clickOnText("Settings");
	    
	    assertTrue(solo.waitForText("UUID"));
	    
	    solo.clickOnText("Nickname");
	    
	    solo.enterText(0, "shiggy");
	    
	    solo.clickOnText("OK");
	    
	    solo.goBack();
	    
	    assertTrue(solo.waitForActivity("GroupsActivity"));
	    
	    solo.finishOpenedActivities();
	}

	
	public void testCreateGroupsAndDisplay() throws ClientProtocolException, IOException{
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
							return mockResponses.loginGETSuccess;
						}
						else if (input.getURI().getPath().contains("libentries"))
						{
							return mockResponses.responseForLibentriesPUT;
						}
						else if (input.getURI().getPath().contains("groups"))
						{
							if(!newGroupCreated) {
								Log.d("testlog", "va repondre sans newGroup");
								return mockResponses.groupsGETSuccess;								
							}else {
								Log.d("testlog", "va repondre avec newGroup");
								return mockResponses.groupsGETSuccessAfterCreation;
							}
						}
						else
						{
							return new BasicHttpResponse(new ProtocolVersion(
									"HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "Bad request, die! Asked : " + input.getURI().getPath()); // alternatively, you could throw an exception
						}
					}
				}
				);
		
		HttpClientFactory.setInstance(mockClient);
		
		Solo solo = new Solo(getInstrumentation(), getActivity());
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		solo.clickOnText("Create group");
		solo.waitForDialogToOpen(1000);
		solo.enterText(0, newGroupName);
		newGroupCreated = true; //this needs to be done before we click on OK.
		solo.clickOnText("OK");

		assertTrue(solo.waitForText(newGroupName, 0, 10000, true));
		
		solo.finishOpenedActivities();
		
	}
	
	public void testJoinProtectedGroup() throws ClientProtocolException, IOException {
	    mockClient = mock(HttpClient.class);
        
        when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(new Answer<HttpResponse>() {

            @Override
            public HttpResponse answer(InvocationOnMock invocation) throws Throwable {
                HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];
                String path = input.getURI().getPath();
                
                if (path.endsWith("groups")) {
                    return mockResponses.groupsGETSuccess;
                } else if (path.endsWith("2")) {
                    Log.d(TAG, "received the following params: " + input.getParams().toString());
                    return mockResponses.mainGETGroupsNotDJSuccess;
                } else if (path.endsWith("group")) {
                    return mockResponses.PUTPasswordSuccess;
                }
                return null;
            }
        });
        HttpClientFactory.setInstance(mockClient);
        
        Solo solo = new Solo(getInstrumentation(), getActivity());
        
        solo.clickOnText("test3");
        
        assertTrue(solo.waitForText("Group Password"));
        
        solo.enterText(0, "0000");
        
        solo.clickOnText("OK");
        
        assertTrue(solo.waitForText("cool group"));
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
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
