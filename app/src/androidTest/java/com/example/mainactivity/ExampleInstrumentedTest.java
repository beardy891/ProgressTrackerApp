package com.example.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.Instrumentation.*;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String POMODORO_TIME_ZERO_TEST = "00:00";
    private static final String EXPECTED_URL = "https://en.wikipedia.org/wiki/Pomodoro_Technique";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.mainactivity", appContext.getPackageName());
    }

    @Test
    public void isTimerDisplayed(){
        onView(withId(R.id.bt_stat_stop_pomodoro)).perform(click());
        onView(withId(R.id.chrono_pomodoro_timer)).check(matches(isDisplayed()));
    }

    @Test
    public void resetTimeMenuActionTest(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText("Reset total time")).perform(click());
        onView(withId(R.id.tv_time_total))
                .check(matches(isDisplayed()))
                .check(matches(withText(POMODORO_TIME_ZERO_TEST)));
    }

    @Test
    public void clickAboutPomodoroMenuAction_OpensBrowser(){
        Intents.init();

        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_VIEW),
                hasData(EXPECTED_URL));
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, null);

        intending(expectedIntent).respondWith(result);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("About Pomodoro")).perform(click());

        intended(expectedIntent);
        Intents.release();
    }

}
