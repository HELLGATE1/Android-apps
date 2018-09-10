package com.vit.flagquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MainActivityFragment extends Fragment {
private static final String TAG = "FlagQuizActivity";
private static final int FLAGS_IN_QUIZ = 10;
private List<String> allFlagCurrentRegions;
private List<String> currentFlags;//flags which need guess
private Set<String> regions;
private String correctAnswer;
public static int attempt;
private int correctAnswers;
private int count;
private int Rows;
private SecureRandom random;
private Handler handler;
private Animation shake;
private TextView question_number_tv;
private ImageView flag_iv;
private TextView answer_tv;
private ConstraintLayout layout;
private LinearLayout[] linearGroup;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    allFlagCurrentRegions = new ArrayList<>();
    currentFlags = new ArrayList<>();
    random = new SecureRandom();
    handler = new Handler();
    shake = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
    shake.setRepeatCount(3);
    layout = (ConstraintLayout) view.findViewById(R.id.layout);
    question_number_tv = (TextView) view.findViewById(R.id.question_number_tv);
    flag_iv = (ImageView) view.findViewById(R.id.flag_iv);
    linearGroup = new LinearLayout[4];
    linearGroup[0] = (LinearLayout) view.findViewById(R.id.row1_id);
    linearGroup[1] = (LinearLayout) view.findViewById(R.id.row2_id);
    linearGroup[2] = (LinearLayout) view.findViewById(R.id.row3_id);
    linearGroup[3] = (LinearLayout) view.findViewById(R.id.row4_id);
    answer_tv = (TextView) view.findViewById(R.id.answer_tv);
    for (LinearLayout row : linearGroup) {
        for (int column = 0; column < row.getChildCount(); column++) {
            Button button = (Button) row.getChildAt(column);
            button.setOnClickListener(ButListener);
        }
    }
    question_number_tv.setText(getString(R.string.question, 1, FLAGS_IN_QUIZ));
    return view;
}

public void updateVariant(SharedPreferences sharedPreferences) {
    String choices = sharedPreferences.getString(MainActivity.CHOICES, null);
    Rows = Integer.valueOf(choices) / 2;
    for (LinearLayout layout : linearGroup) {
        layout.setVisibility(View.GONE);
    }
    for (int count = 0; count < Rows; count++) {
        linearGroup[count].setVisibility(View.VISIBLE);
    }
}

public void updateRegions(SharedPreferences sharedPreferences) {
    regions = sharedPreferences.getStringSet(MainActivity.REGIONS, null);
}

public void resetQuiz() {
    AssetManager assets = getActivity().getAssets();
    allFlagCurrentRegions.clear();
    try {
        for (String reg : regions) {
            if (reg.contains(" ")) {
                reg = reg.replace(' ', '_');
            }
            String namesFlag[] = assets.list(reg);

            for (String nameflag : namesFlag) {
                allFlagCurrentRegions.add(nameflag.replace(".png", ""));
            }
        }
    } catch (IOException exception) {
        Log.e(TAG, "Error loading image flag", exception);
    }
    correctAnswers = 0;
    attempt = 0;
    currentFlags.clear();
    int flagCounter = 1;
    int numberFlags = allFlagCurrentRegions.size();
    while (flagCounter <= FLAGS_IN_QUIZ) {
        int randomIndex = random.nextInt(numberFlags);

        String flag = allFlagCurrentRegions.get(randomIndex);
        if (!currentFlags.contains(flag)) {
            currentFlags.add(flag);
            flagCounter++;
        }
    }
    loadNextFlag();
}

private void loadNextFlag() {
    String nextFlag = currentFlags.remove(0);
    correctAnswer = nextFlag;
    answer_tv.setText("");
    count = 0;
    question_number_tv.setText(getString(R.string.question, (correctAnswers + 1), FLAGS_IN_QUIZ));
    String region = nextFlag.substring(0, nextFlag.indexOf('-'));
    AssetManager assets = getActivity().getAssets();
    try (InputStream stream = assets.open(region + "/" + nextFlag + ".png")) {
        Drawable flag = Drawable.createFromStream(stream, nextFlag);
        flag_iv.setImageDrawable(flag);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && correctAnswers != 0) {
            animate(false);
        }
    } catch (IOException e) {
        Log.e(TAG, "Error loading" + nextFlag, e);
    }
    Collections.shuffle(allFlagCurrentRegions);
    int correctIndex = allFlagCurrentRegions.indexOf(correctAnswer);
    allFlagCurrentRegions.add(allFlagCurrentRegions.remove(correctIndex));//remove correctIndex element and add it in end
    for (int row = 0; row < Rows; row++) {
        for (int column = 0; column < linearGroup[row].getChildCount(); column++) {
            Button button = (Button) linearGroup[row].getChildAt(column);
            button.setEnabled(true);
            String flagName = allFlagCurrentRegions.get(count);
            count++;
            button.setText(getCountryName(flagName));
        }
    }
    int row = random.nextInt(Rows);
    int column = random.nextInt(2);
    ((Button) linearGroup[row].getChildAt(column)).setText(getCountryName(correctAnswer));
}

private String getCountryName(String str) {
    return str.substring(str.indexOf('-') + 1).replace('_', ' ');
}

private void animate(boolean show) {
    if (correctAnswers == 0)
        return;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        int centerX = (layout.getLeft() + layout.getRight()) / 2;
        int centerY = (layout.getTop() + layout.getBottom()) / 2;
        int radius = (int) (Math.hypot(layout.getWidth(), layout.getHeight()) / 2);
        Animator animator;
        if (show) {
            animator = ViewAnimationUtils.createCircularReveal(layout, centerX, centerY, radius, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadNextFlag();
                }
            });
        } else {
            animator = ViewAnimationUtils.createCircularReveal(layout, centerX, centerY, 0, radius);
        }
        animator.setDuration(500);
        animator.start();
    } else {
        loadNextFlag();
    }
}

private View.OnClickListener ButListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Button pressBut = (Button) v;
        String pressButText = pressBut.getText().toString();
        String answer = getCountryName(correctAnswer);
        attempt++;
        if (pressButText.equals(answer)) {
            correctAnswers++;
            answer_tv.setText(answer + "!");
            answer_tv.setTextColor(getResources().getColor(R.color.correct_answer));
            blockButtons();
            if (correctAnswers == FLAGS_IN_QUIZ) {
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                myDialogFragment.show(getFragmentManager(), "results");
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animate(true);
                    }
                }, 1000);
            }
        } else {
            flag_iv.startAnimation(shake);
            answer_tv.setText(R.string.incorrect);
            answer_tv.setTextColor(getResources().getColor(R.color.incorrect_answer));
            pressBut.setEnabled(false);
        }
    }
};


private void blockButtons() {
    for (int row = 0; row < Rows; row++) {
        for (int column = 0; column < linearGroup[row].getChildCount(); column++) {
            linearGroup[row].getChildAt(column).setEnabled(false);
        }
    }
}
}

