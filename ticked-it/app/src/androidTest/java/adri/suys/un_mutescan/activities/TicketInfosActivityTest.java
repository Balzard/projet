package adri.suys.un_mutescan.activities;

import android.content.Intent;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class TicketInfosActivityTest {

    @Rule
    ActivityTestRule<TicketInfosActivity> activityTestRule = new ActivityTestRule<>(TicketInfosActivity.class);

    @Test
    public void barcodeDoesNotExist() throws ParseException {
        init("23", 749997);
        onView(withText(R.string.scan_error_ticket_not_exist)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void eventDoesNotExist() throws ParseException {
        init("cf8495c6a7194d63c62", 43334);
        onView(withText(R.string.scan_error_event_not_exist)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void dateIsNotGood() throws ParseException {
        init("cf8495c6a7194d63c62", 749997);
        onView(withText(R.string.scan_error_incorrect_date)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void ticketDoesNotMatchEvent() throws ParseException {
        init("cf8495c6a7194d63c62", 749999);
        onView(withText(R.string.scan_error_no_match_event_tix)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void ticketWasRefunded() throws ParseException {
        init("cf8495c6a7194d63c62", 749997);
        onView(withText(R.string.scan_error_tix_refund)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void ticketAlreadyScanned() throws ParseException {
        init("ph0c9124a1077f4fa90", 750008);
        onView(withText(R.string.scan_error_already_scanned)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void ticketOK() throws ParseException {
        init("cf8555c8bc80b96f2f3", 750008);
        onView(withText(R.string.ticket_ok_dialog)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    private void init(String barcode, int eventID) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE).parse("2019-02-18 16:00:00");
        Event event = new Event(eventID, "[Ticked-it] Mon anniversaire", 500, 0, 0, 0, date, 0);
        Intent i = new Intent();
        i.putExtra("alert", true);
        i.putExtra("barcode", barcode);
        UnMuteDataHolder.setEvent(event);
        activityTestRule.launchActivity(i);
    }


}