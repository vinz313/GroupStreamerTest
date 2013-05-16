
package ch.epfl.unison.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Pair;

import ch.epfl.unison.Const;
import ch.epfl.unison.Const.PrefKeys;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.api.JsonStruct;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.GroupsHistoryActivity;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jayway.android.robotium.solo.Solo;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupsHistoryActivityTest extends
        ActivityInstrumentationTestCase2<GroupsHistoryActivity> {

    private MockResponses mockResponses;

    private final String email = "test@test.test";
    private final String password = "pw";
    private final String nickname = "nick";
    private final Long uid = Long.valueOf(0);
    
    private boolean notFoundSent = false;
    private boolean getGroupsSent = false;

    public GroupsHistoryActivityTest() {
        super(GroupsHistoryActivity.class);
    }

    protected void setUp() throws Exception {
        mockResponses = new MockResponses();
        
        notFoundSent = false;

        Type groupHistoryMapType = new
                TypeToken<Map<Long, Pair<JsonStruct.Group, Date>>>() {
                }.getType();

        JsonStruct.Group group0 = new JsonStruct.Group();
        group0.nbUsers = 0;
        group0.gid = (long) 0;
        group0.distance = null;
        group0.name = "group0";

        JsonStruct.Group group1 = new JsonStruct.Group();
        group1.nbUsers = 1;
        group1.gid = (long) 1;
        group1.distance = null;
        group1.name = "group1";

        JsonStruct.Group group2 = new JsonStruct.Group();
        group2.nbUsers = 2;
        group2.gid = (long) 2;
        group2.distance = null;
        group2.name = "group2";

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getInstrumentation()
                        .getTargetContext());
        SharedPreferences.Editor editor = prefs.edit();

        HashMap<Long, Pair<JsonStruct.Group, Date>> history = new HashMap<Long, Pair<JsonStruct.Group, Date>>();

        history.put(Long.valueOf(group0.gid), new Pair<JsonStruct.Group, Date>(group0, new Date()));
        history.put(Long.valueOf(group1.gid), new Pair<JsonStruct.Group, Date>(group1, new Date()));
        history.put(Long.valueOf(group2.gid), new Pair<JsonStruct.Group, Date>(group2, new Date()));

        String value = new GsonBuilder().create().toJson(history, groupHistoryMapType);
        editor.putString(Const.PrefKeys.HISTORY, value);

        editor.putString(PrefKeys.EMAIL, email);
        editor.putString(PrefKeys.PASSWORD, password);
        editor.putString(PrefKeys.NICKNAME, nickname);
        editor.putLong(PrefKeys.UID, uid);
        editor.putBoolean(PrefKeys.HELPDIALOG, false);

        editor.commit();
    }

    public void testDisplayGroups() {
        Solo solo = new Solo(getInstrumentation(), getActivity());

        assertTrue(solo.searchText("group0"));
        assertTrue(solo.searchText("group1"));
        assertTrue(solo.searchText("group2"));

        solo.finishOpenedActivities();
    }

    public void testDeleteHistory() {
        Solo solo = new Solo(getInstrumentation(), getActivity());

        solo.clickOnText("Delete History");

        assertFalse(solo.searchText("group0"));
        assertFalse(solo.searchText("group1"));
        assertFalse(solo.searchText("group2"));

        solo.finishOpenedActivities();
    }

    public void testJoinGroup() throws ClientProtocolException, IOException {
        HttpClient mockClient = mock(HttpClient.class);

        when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(
                new Answer<HttpResponse>() {

                    @Override
                    public HttpResponse answer(InvocationOnMock invocation) throws Throwable {
                        HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];
                        String path = input.getURI().getPath();

                        if (path.endsWith("groups")) {
                            return mockResponses.groupsGETSuccess;
                        } else if (path.endsWith("2")) {
                            return mockResponses.mainGETGroupsNotDJSuccess;
                        } else if (path.endsWith("group")) {
                            return mockResponses.PUTPasswordSuccess;
                        }
                        return null;
                    }
                });

        HttpClientFactory.setInstance(mockClient);

        Solo solo = new Solo(getInstrumentation(), getActivity());

        solo.clickOnText("group2");

        assertTrue(solo.waitForText("cool group"));
        
        
        solo.finishOpenedActivities();
    }

    public void testGroupNotFoundRecreate() throws ClientProtocolException, IOException {
        HttpClient mockClient = mock(HttpClient.class);
        
        when(mockClient.execute((HttpUriRequest) anyObject())).thenAnswer(
                new Answer<HttpResponse>() {

                    @Override
                    public HttpResponse answer(InvocationOnMock invocation) throws Throwable {
                        HttpUriRequest input = (HttpUriRequest) invocation.getArguments()[0];
                        String path = input.getURI().getPath();

                        if (path.endsWith("groups")) {
//                            if (!getGroupsSent) {
//                                getGroupsSent = true;
//                                return mockResponses.groupsGETSuccess;
//                            } else {
                                return mockResponses.singleGroupAfterCreation;
//                            }
                        } else if (path.endsWith("2")) {
                            return mockResponses.mainGETGroupsNotDJSuccess;
                        } else if (path.endsWith("group")) {
                            if (!notFoundSent) {
                                notFoundSent = true;
                                return mockResponses.historyGroupNotFound;
                            } else {
                                return mockResponses.PUTPasswordSuccess;
                            }
                            
                        }
                        return null;
                    }
                });

        HttpClientFactory.setInstance(mockClient);

        Solo solo = new Solo(getInstrumentation(), getActivity());

        solo.clickOnText("group2");
        
        assertTrue(solo.waitForText("Recreate group"));
        
        solo.clickOnText("Recreate group");
        
        assertTrue(solo.waitForText("newGroup"));

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

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
