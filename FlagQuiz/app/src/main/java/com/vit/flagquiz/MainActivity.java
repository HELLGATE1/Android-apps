package com.vit.flagquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
public final static String CHOICES = "number_answer";
public final static String REGIONS = "region";
private static boolean phoneDevice = true;
private static boolean changes = true;


@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);//create the object SharedPreference with file preferences
    PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
        phoneDevice = false;
    }
    if (phoneDevice) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}//app only in portrait orientation

protected void onStart() {
    super.onStart();
    if (changes) {
        MainActivityFragment quizFragment = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.quizFragment);
        quizFragment.updateVariant(PreferenceManager.getDefaultSharedPreferences(this));
        quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));
        quizFragment.resetQuiz();
        changes = false;
    }
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    int orientation = getResources().getConfiguration().orientation;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } else {
        return false;
    }
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
    return super.onOptionsItemSelected(item);
}

private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        changes = true;
        MainActivityFragment quizFragment = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.quizFragment);
        if (key.equals(CHOICES)) {
            quizFragment.updateVariant(sharedPreferences);
            quizFragment.resetQuiz();
            changes = false;
        } else if (key.equals(REGIONS)) {
            Set<String> regions = sharedPreferences.getStringSet(REGIONS, null);
            if (regions != null && regions.size() > 0) {
                quizFragment.updateRegions(sharedPreferences);
                quizFragment.resetQuiz();
                changes = false;
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                regions.add(getString(R.string.default_region));
                editor.putStringSet(REGIONS, regions);
                editor.apply();
                Toast.makeText(MainActivity.this, R.string.default_region_message, Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT);
    }
};
}
