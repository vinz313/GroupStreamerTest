
package ch.epfl.unison.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.unison.R;
import ch.epfl.unison.api.HttpClientFactory;
import ch.epfl.unison.mockUtils.MockResponses;
import ch.epfl.unison.ui.SignupActivity;

import com.jayway.android.robotium.solo.Solo;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    public void testInvalidEmail() throws UnsupportedEncodingException {
        HttpClient mockClient = mock(HttpClient.class);

        try {
            when(mockClient.execute((HttpUriRequest) anyObject())).thenReturn(mockResponses.signupPOSTInvalidEmail);
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
        
        solo.clickOnCheckBox(0);

        solo.clickOnButton(1);

        assertTrue(solo.waitForText(getInstrumentation().getTargetContext().getString(R.string.signup_form_email_invalid)));
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
        
        HttpClientFactory.setInstance(mockClient);

        Solo solo = new Solo(getInstrumentation(), getActivity());

        solo.enterText(0, "test@test.test");
        solo.enterText(1, "thePassWord");
        solo.enterText(2, "thePassWord");
        
        solo.clickOnCheckBox(0);

        solo.clickOnButton(1);

        assertTrue(solo.waitForActivity("GroupsActivity"));
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
