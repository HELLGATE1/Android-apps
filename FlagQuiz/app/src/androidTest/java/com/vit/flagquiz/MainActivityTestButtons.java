package com.vit.flagquiz;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class MainActivityTestButtons {
String numberButtons;
ArrayList<Integer> listID_buttons;
@Rule
public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

@Before
public void before() {
    numberButtons = MainActivityFragment.numberButtons;
    listID_buttons = new ArrayList<>();
    switch (numberButtons) {
        case "2":
            listID_buttons.add(R.id.button);
            listID_buttons.add(R.id.button2);
            break;
        case "4":
            listID_buttons.add(R.id.button);
            listID_buttons.add(R.id.button2);
            listID_buttons.add(R.id.button3);
            listID_buttons.add(R.id.button4);
            break;
        case "6":
            listID_buttons.add(R.id.button);
            listID_buttons.add(R.id.button2);
            listID_buttons.add(R.id.button3);
            listID_buttons.add(R.id.button4);
            listID_buttons.add(R.id.button5);
            listID_buttons.add(R.id.button6);
            break;
        case "8":
            listID_buttons.add(R.id.button);
            listID_buttons.add(R.id.button2);
            listID_buttons.add(R.id.button3);
            listID_buttons.add(R.id.button4);
            listID_buttons.add(R.id.button5);
            listID_buttons.add(R.id.button6);
            listID_buttons.add(R.id.button7);
            listID_buttons.add(R.id.button8);
            break;
        default:
            Assert.fail("Неверное количество кнопок");
    }
}

@Test//tc_004
public void check_buttons() {
    switch (numberButtons) {
        case "2":
            onView(withId(R.id.button3)).check(matches(not(isDisplayed())));
            break;
        case "4":
            onView(withId(R.id.button5)).check(matches(not(isDisplayed())));
            break;
        case "6":
            onView(withId(R.id.button7)).check(matches(not(isDisplayed())));
            break;
    }
    for (Integer id : listID_buttons) {
        onView(withId(id)).check(matches(isDisplayed()));
        onView(withId(id)).check(matches(isClickable()));
        onView(withId(id)).check(isCompletelyBelow(withId(R.id.guess_tv)));
    }
}

@Test//tc_006
public void checkCorrect_answer_tv() {
    for (Integer id : listID_buttons) {
        onView(withId(id)).perform(click());
        if (MainActivityFragment.answer_tv.getText() == "Incorrect!" || MainActivityFragment.answer_tv.getText() == "Неверно!") {
            continue;
        } else {
            onView(withId(R.id.answer_tv)).check(matches(isDisplayed()));
            onView(withId(R.id.answer_tv)).check(isCompletelyBelow(withId(R.id.button)));
            break;
        }
    }
}

@Test//tc_007
public void checkIncorrect_answer_tv() {
    for (Integer id : listID_buttons) {
        onView(withId(id)).perform(click());
        if (MainActivityFragment.answer_tv.getText() == MainActivityFragment.pressButText + "!") {
            continue;
        } else {
            onView(withId(R.id.answer_tv)).check(matches(isDisplayed()));
            onView(withId(R.id.answer_tv)).check(matches(withText(R.string.incorrect)));
            onView(withId(R.id.answer_tv)).check(isCompletelyBelow(withId(R.id.button)));
            onView(withId(id)).check(matches(not(isEnabled())));
            break;
        }
    }
}

@Test//tc_008
public void checkEnable_button() {
    for (int count=0;;count++) {
        onView(withId(listID_buttons.get(count))).perform(click());
        if (MainActivityFragment.answer_tv.getText() == MainActivityFragment.pressButText + "!") {
            count--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            continue;
        } else {
            onView(withText(MainActivityFragment.textButWithCorrectAnswer)).perform(click());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onView(withId(listID_buttons.get(count))).check(matches(isEnabled()));
            break;
        }
    }
}

@Test//tc_009
public void checkChanges_question_number_tv() {
    for (Integer id : listID_buttons) {
        onView(withId(id)).perform(click());
        if (MainActivityFragment.answer_tv.getText() == MainActivityFragment.pressButText + "!") {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onView(withId(R.id.question_number_tv)).check(matches(withText(MainActivityFragment.question_number)));
            break;
        }
    }
}

@Test//tc_010
public void checkDialog() {
    for (int count = 0; count < 10; count++) {
        onView(withText(MainActivityFragment.textButWithCorrectAnswer)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    onView(withText(R.string.reset_quiz)).check(matches(isDisplayed()));
    onView(withText(MyDialogFragment.result)).check(matches(isDisplayed()));
}
}
