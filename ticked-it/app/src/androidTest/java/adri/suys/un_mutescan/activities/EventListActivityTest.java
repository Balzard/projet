package adri.suys.un_mutescan.activities;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.model.User;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class EventListActivityTest {

    @Rule
    public ActivityTestRule<EventListActivity> activityTestRule = new ActivityTestRule<>(EventListActivity.class);

    @Test
    public void initOK() {
        String eventName = "MES EVENEMENTS";
        onView(withText(eventName)).check(matches(anything()));
    }

    @Test
    public void initEvents(){
        //initEvent(16, "Barema en concert");
        initEvent(1, "Fred & The Healers au Spacium");
        initEvent(4, "Christian Olivier (Têtes Raides) & Piwi Leman au Spacium");
        //initEvent(9, "Mon event Fidelity");
    }

    @BeforeClass
    public static void initGoodActivity() {
        User user = new User(412, "adrien.suys@gmail.com", "$2y$13$fYRZ4t.zUDkm6yVzFKxeR.QXV83liep6m4JjgSh2s.MyG2SJwrYim", "");
        UnMuteDataHolder.setUser(user);
    }

    private void initEvent(int pos, String eventName){
        androidx.test.espresso.idling.CountingIdlingResource resource = activityTestRule.getActivity().getPresenter().getCountingIdlingResource();
        IdlingRegistry.getInstance().register(resource);
        onView(withId(R.id.event_recycler_view))
                .perform(RecyclerViewActions.
                        actionOnItemAtPosition(pos, MyViewAction.clickChildViewWithId(R.id.event_info_stat)
                        )
                );
        onData(withText(eventName)).check(matches(anything()));
    }

    public static class MyViewAction{
        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }
    }


}