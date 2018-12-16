package com.trackme.trackmeapplication.automatedsos;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.trackme.trackmeapplication.account.login.UserLoginActivity;
import com.trackme.trackmeapplication.automatedsos.exception.NoPermissionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Spy;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class SOSServiceHelperImplTest {

    @RunWith(AndroidJUnit4.class)
    public static class TestWithPermissionGranted {
        @Rule
        public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        @Rule
        public ServiceTestRule mServiceTestRule = new ServiceTestRule();

        @Rule
        public IntentsTestRule<UserLoginActivity> mActivity = new IntentsTestRule<>(UserLoginActivity.class);

        @Spy
        private SOSAndroidService androidService;

        private SOSServiceHelperImpl helper;

        @Before
        public void setUp() throws Exception {
            intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

            Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), SOSAndroidService.class);
            IBinder binder = mServiceTestRule.bindService(serviceIntent);
            androidService = ((SOSAndroidService.LocalBinder) binder).getService();
            mServiceTestRule.startService(serviceIntent);

            helper = new SOSServiceHelperImpl(androidService);
        }

        @After
        public void tearDown() throws Exception {
            androidService = null;
            helper = null;
        }

        @Test
        public void makeEmergencyCall() throws Exception {
            helper.makeEmergencyCall();
            intended(hasAction(Intent.ACTION_CALL), times(1));

            assertTrue(helper.hasRecentEmergencyCall());
        }
    }

    public static class TestWithoutPermissionGranted{

        @Rule
        public ServiceTestRule mServiceTestRule = new ServiceTestRule();

        @Rule
        public IntentsTestRule<UserLoginActivity> mActivity = new IntentsTestRule<>(UserLoginActivity.class);

        @Spy
        private SOSAndroidService androidService;

        private SOSServiceHelperImpl helper;

        @Before
        public void setUp() throws Exception {
            revokePermissions();

            intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

            Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), SOSAndroidService.class);
            IBinder binder = mServiceTestRule.bindService(serviceIntent);
            androidService = ((SOSAndroidService.LocalBinder) binder).getService();
            mServiceTestRule.startService(serviceIntent);

            helper = new SOSServiceHelperImpl(androidService);
        }

        private void revokePermissions() {
            // In M+, trying to call a number will trigger a runtime dialog.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm revoke " + InstrumentationRegistry.getTargetContext().getPackageName()
                                + " android.permission.CALL_PHONE");
            }
        }

        @After
        public void tearDown() throws Exception {
            androidService = null;
            helper = null;
        }

        @Test(expected = NoPermissionException.class)
        public void makeEmergencyCall() throws Exception {
            helper.makeEmergencyCall();
        }
    }
}