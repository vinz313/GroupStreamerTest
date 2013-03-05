package ch.epfl.unison.test;

import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.unison.ui.LoginActivity;

import com.jayway.android.robotium.solo.Solo;

public class RobotiumTest extends ActivityInstrumentationTestCase2<LoginActivity>{

	public RobotiumTest() {
		super(LoginActivity.class);
	}

	public void testRobotium(){
		Solo solo = new Solo(getInstrumentation(), getActivity());
		
		//Assumes you had already logged in so that it is now done automatically.
		solo.waitForText("test1");
		solo.clickOnText("test1");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
