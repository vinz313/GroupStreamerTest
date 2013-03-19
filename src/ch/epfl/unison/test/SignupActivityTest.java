
package ch.epfl.unison.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.SignupActivity;

public class SignupActivityTest extends
        ActivityInstrumentationTestCase2<SignupActivity> {

    private MockResponses mockResponses;

    public SignupActivityTest() {
        super(SignupActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        mockResponses = new MockResponses();
    }

    public void testInvalidEmail() {
        HttpClient mockClient = mock(HttpClient.class);

        HttpResponse badReq = new BasicHttpResponse(new ProtocolVersion(
                "HTTP", 1, 1), HttpStatus.SC_BAD_REQUEST, "invalid email");

        try {
            when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(badReq);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpClientFactory.setInstance(mockClient);

        Solo solo = new Solo(getInstrumentation(), getActivity());

        solo.enterText(0, "notAnEmail");
        solo.enterText(1, "thePassWord");
        solo.enterText(2, "thePassWord");

        solo.clickOnText("Sign Up");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        solo.finishOpenedActivities();
    }

    public void testSignupSuccess() {
        HttpClient mockClient = mock(HttpClient.class);

        try {
            when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(
                    mockResponses.signupPOSTSuccess);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Solo solo = new Solo(getInstrumentation(), getActivity());

        solo.enterText(0, "test@test.test");
        solo.enterText(1, "thePassWord");
        solo.enterText(2, "thePassWord");

        solo.clickOnText("Sign Up");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        solo.finishOpenedActivities();
    }

    @Override
    protected void tearDown() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getInstrumentation().getTargetContext());
        Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

}
