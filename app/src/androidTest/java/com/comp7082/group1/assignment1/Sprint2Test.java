package com.comp7082.group1.assignment1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Sprint2Test {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void imageLocationToast() {
        onView(withId(R.id.getLocation)).perform(click());
    }

    @Test
    public void searchPhotoLocation1() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-05-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etLat)).perform(typeText("22"), closeSoftKeyboard());
        onView(withId(R.id.etLng)).perform(typeText("114"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.getLocation)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
        //onView(withId(R.id.latlong)).check(matches(withText("LAT: 22 LNG: 114")));
    }

    @Test
    public void searchPhotoLocation2() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-05-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etLat)).perform(typeText("49"), closeSoftKeyboard());
        onView(withId(R.id.etLng)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.getLocation)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
        //onView(withId(R.id.latlong)).check(matches(withText("LAT: 49 LNG: 123")));
    }

    @Test
    public void searchPhotoLocation3() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-05-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etLat)).perform(typeText("19"), closeSoftKeyboard());
        onView(withId(R.id.etLng)).perform(typeText("155"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.getLocation)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
       // onView(withId(R.id.latlong)).check(matches(withText("LAT: 19 LNG: 155")));
    }

    //extra photo with different lat and long to be added
    @Test
    public void searchPhotoLocation4() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-05-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etLat)).perform(typeText("49"), closeSoftKeyboard());
        onView(withId(R.id.etLng)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.getLocation)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
        //onView(withId(R.id.latlong)).check(matches(withText("LAT: 49 LNG: 123")));
    }

    //extra photo with different lat and long to be added
    @Test
    public void searchPhotoLocation5() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-05-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etLat)).perform(typeText("19"), closeSoftKeyboard());
        onView(withId(R.id.etLng)).perform(typeText("155"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.getLocation)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
        // onView(withId(R.id.latlong)).check(matches(withText("LAT: 19 LNG: 155")));
    }

}

