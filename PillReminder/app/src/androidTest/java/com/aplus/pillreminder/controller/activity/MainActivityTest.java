package com.aplus.pillreminder.controller.activity;


import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.aplus.pillreminder.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addPillTest() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.actvName)).perform(replaceText("test pill"), closeSoftKeyboard());
        onView(withId(R.id.etDescribe)).perform(replaceText("test describe"), closeSoftKeyboard());
        onView(withId(R.id.etQuantity)).perform(replaceText("10") ,closeSoftKeyboard());
        onView(withId(R.id.etDose)).perform(replaceText("5") ,closeSoftKeyboard());
        onView(withId(R.id.action_confirm)).perform(click());
        SystemClock.sleep(3000);

    }
}
