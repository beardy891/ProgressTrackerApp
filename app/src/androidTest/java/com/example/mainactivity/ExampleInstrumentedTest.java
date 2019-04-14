package com.example.mainactivity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String POMODORO_TIME_ZERO_TEST = "00:00";

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
    public void aboutPomodoroMenuActionTest(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText("About Pomodoro")).perform(click());
                // TODO: 14.04.2019 how to check if new activity was launched
    }

}
