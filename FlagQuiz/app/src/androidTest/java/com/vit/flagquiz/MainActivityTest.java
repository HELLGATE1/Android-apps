package com.vit.flagquiz;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class MainActivityTest {
@Rule
public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

@Test//tc_001
public void check_iv() {
    onView(withId(R.id.flag_iv)).check(matches(isDisplayed()));
    onView(withId(R.id.flag_iv)).check(isCompletelyAbove(withId(R.id.guess_tv)));
    onView(withId(R.id.flag_iv)).check(isCompletelyBelow(withId(R.id.question_number_tv)));
}

@Test//tc_002
public void check_guess_tv() {
    onView(withId(R.id.guess_tv)).check(matches(isDisplayed()));
    onView(withId(R.id.guess_tv)).check(matches(withText(R.string.guess_country)));
    onView(withId(R.id.guess_tv)).check(isCompletelyAbove(withId(R.id.button2)));
    onView(withId(R.id.guess_tv)).check(isCompletelyBelow(withId(R.id.flag_iv)));
}

@Test//tc_003
public void check_question_number_tv() {
    onView(withId(R.id.question_number_tv)).check(matches(isDisplayed()));
    onView(withId(R.id.question_number_tv)).check(matches(withText(MainActivityFragment.question_number)));
    onView(withId(R.id.question_number_tv)).check(isCompletelyAbove(withId(R.id.flag_iv)));
}

@Test//tc_005
public void checkVisibility_answer_tw(){
    onView(withId(R.id.answer_tv)).check(matches(not(isDisplayed())));
}




}
