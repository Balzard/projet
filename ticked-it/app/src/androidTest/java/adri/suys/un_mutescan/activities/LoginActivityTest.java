package adri.suys.un_mutescan.activities;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import adri.suys.un_mutescan.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void onClickWrongCredential(){
        onView(withId(R.id.input_username)).perform(typeText("adrien.suys@gmail.com"));
        onView(withId(R.id.input_password)).perform(typeText("ucjn"));
        onView(withText(R.string.user_wrong_credentials)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickNoPermission(){
        onView(withId(R.id.input_username)).perform(typeText("cedricbentz@yahoo.fr"));
        onView(withId(R.id.input_password)).perform(typeText("ucjn"));
        onView(withText(R.string.user_cant_access)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickNotFound(){
        onView(withId(R.id.input_username)).perform(typeText("zob@xx.com"));
        onView(withId(R.id.input_password)).perform(typeText("ucjn"));
        onView(withText(R.string.user_not_found)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickNoLogin(){
        onView(withText(R.string.user_no_login)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClickOK(){
        onView(withId(R.id.input_username)).perform(typeText("adrien.suys@gmail.com"));
        onView(withId(R.id.input_password)).perform(typeText("YCkd3z365"));
        onView(withText(R.string.user_logged)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

}