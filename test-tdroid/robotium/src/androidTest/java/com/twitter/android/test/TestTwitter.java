package com.twitter.android.test;

import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


@SuppressWarnings("rawtypes")
public class TestTwitter extends ActivityInstrumentationTestCase2 {
  	private Solo solo;
  	
  	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.twitter.android.StartActivity";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
    }
  	
  	@SuppressWarnings("unchecked")
    public TestTwitter() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}
  
	public void testRun() {
        //Wait for activity: 'com.twitter.android.StartActivity'
		solo.waitForActivity("StartActivity", 2000);
        //Wait for activity: 'com.twitter.android.MainActivity'
		assertTrue("MainActivity is not found!", solo.waitForActivity("MainActivity"));
        //Set default small timeout to 296852 milliseconds
		Timeout.setSmallTimeout(296852);
        //Scroll to TweetHeaderView AdaptiveTweetMediaView TweetHeaderView UserImageView MediaI
		android.widget.ListView listView0 = (android.widget.ListView) solo.getView(android.widget.ListView.class, 0);
		solo.scrollListToLine(listView0, 7);
        //Click on TweetHeaderView AdaptiveTweetMediaView TweetHeaderView UserImageView MediaI
		solo.clickOnView(solo.getView(android.widget.FrameLayout.class, 4));
        //Wait for activity: 'com.twitter.android.RootTweetActivity'
		assertTrue("RootTweetActivity is not found!", solo.waitForActivity("RootTweetActivity"));
        //Press menu back key
		solo.goBack();
	}
}
