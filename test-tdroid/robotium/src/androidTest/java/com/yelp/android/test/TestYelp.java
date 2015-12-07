package com.yelp.android.test;

import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


@SuppressWarnings("rawtypes")
public class TestYelp extends ActivityInstrumentationTestCase2 {
  	private Solo solo;
  	
  	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.yelp.android.ui.activities.RootActivity";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
    }
  	
  	@SuppressWarnings("unchecked")
    public TestYelp() throws ClassNotFoundException {
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
        //Wait for activity: 'com.yelp.android.ui.activities.RootActivity'
		solo.waitForActivity("RootActivity", 2000);
        //Set default small timeout to 36571 milliseconds
		Timeout.setSmallTimeout(36571);
        //Click on Nearby
		solo.clickOnView(solo.getView("nearby"));
        //Wait for activity: 'com.yelp.android.ui.activities.nearby.ActivityNearby'
		assertTrue("ActivityNearby is not found!", solo.waitForActivity("ActivityNearby"));
        //Press menu back key
		solo.goBack();
        //Click on About Me
		solo.clickOnView(solo.getView("aboutme"));
        //Wait for activity: 'com.yelp.android.ui.activities.profile.ActivityUserProfile'
		assertTrue("ActivityUserProfile is not found!", solo.waitForActivity("ActivityUserProfile"));
        //Press menu back key
		solo.goBack();
        //Click on Bookmarks
		solo.clickOnView(solo.getView("bookmarks"));
        //Wait for activity: 'com.yelp.android.ui.activities.ActivityBookmarks'
		assertTrue("ActivityBookmarks is not found!", solo.waitForActivity("ActivityBookmarks"));
        //Press menu back key
		solo.goBack();
        //Click on Monocle
		solo.clickOnView(solo.getView("monocle"));
        //Rotate the screen
		solo.setActivityOrientation(Solo.LANDSCAPE);
        //Wait for activity: 'com.yelp.android.ui.activities.ActivityHome'
		assertTrue("ActivityHome is not found!", solo.waitForActivity("ActivityHome"));
        //Rotate the screen
		solo.setActivityOrientation(Solo.PORTRAIT);
        //Click on Check-Ins
		solo.clickOnView(solo.getView("check_ins"));
        //Wait for activity: 'com.yelp.android.ui.activities.friendcheckins.NearbyCheckIns'
		assertTrue("NearbyCheckIns is not found!", solo.waitForActivity("NearbyCheckIns"));
        //Press menu back key
		solo.goBack();
        //Click on Friends
		solo.clickOnView(solo.getView("friends"));
        //Wait for activity: 'com.yelp.android.ui.activities.friends.ActivityFriendList'
		assertTrue("ActivityFriendList is not found!", solo.waitForActivity("ActivityFriendList"));
        //Press menu back key
		solo.goBack();
        //Click on Talk
		solo.clickOnView(solo.getView("talk"));
        //Wait for activity: 'com.yelp.android.ui.activities.talk.AllTalkTab'
		assertTrue("AllTalkTab is not found!", solo.waitForActivity("AllTalkTab"));
        //Press menu back key
		solo.goBack();
        //Click on Recents
		solo.clickOnView(solo.getView("recents"));
        //Wait for activity: 'com.yelp.android.ui.activities.ActivityRecents'
		assertTrue("ActivityRecents is not found!", solo.waitForActivity("ActivityRecents"));
        //Press menu back key
		solo.goBack();
        //Click on Deals
		solo.clickOnView(solo.getView("deals"));
        //Wait for activity: 'com.yelp.android.ui.activities.deals.ActivityDealsLanding'
		assertTrue("ActivityDealsLanding is not found!", solo.waitForActivity("ActivityDealsLanding"));
        //Press menu back key
		solo.goBack();
	}
}
