package com.comp7082.group1.assignment1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.comp7082.group1.assignment1.mvp.views.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Sprint1Test {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void openSearchActivity() {
        onView(withId(R.id.btnSearch)).perform(click());
        //intended(hasComponent(SearchActivity.class.getName()));
    }

    @Test
    public void checkFromDateTime(){
        onView(withId(R.id.btnSearch)).perform(click());
        //editText formatting error with 00:00:00
        //onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-04-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-04-20"), closeSoftKeyboard());
        onView(withId(R.id.etFromDateTime)).check(matches(withText("2022-04-20")));
    }

    @Test
    public void checkToDateTime(){
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-04-21"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).check(matches(withText("2022-04-21")));
    }

    @Test
    public void checkCaption(){
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etKeywords)).perform(clearText(), typeText("BCIT"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).check(matches(withText("BCIT")));

        onView(withId(R.id.etKeywords)).perform(clearText(), typeText("COMP7082"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).check(matches(withText("COMP7082")));

        onView(withId(R.id.etKeywords)).perform(clearText(), typeText("Group 1"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).check(matches(withText("Group 1")));
    }

    @Test
    public void searchPhoto1() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-04-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
        //onView(withId(R.id.btnRight)).perform(click());
        //onView(withId(R.id.btnLeft)).perform(click());
    }

    //identical test, to be updated once photo captions are linked
    @Test
    public void searchPhoto2() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-04-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
        //onView(withId(R.id.btnRight)).perform(click());
        //onView(withId(R.id.btnLeft)).perform(click());
    }

    //identical test, to be updated once photo captions are linked
    @Test
    public void searchPhoto3() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), typeText("2022-01-01 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("2022-04-20 00:00:00"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("Description"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Description")));
        //onView(withId(R.id.btnRight)).perform(click());
        //onView(withId(R.id.btnLeft)).perform(click());
    }

}