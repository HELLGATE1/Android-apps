package com.vit.doodlz;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivityFragment extends Fragment {
private DoodleView doodleView;
private float acceleration;
private float currentAcceleration;
private float lastAcceleration;
private boolean DialogOnScreen = false;
private boolean firstRequest = true;
private SensorManager sensorManager;
private Sensor sensorAccel;
private static final int ACCELERATION_THRESHOLD = 4;
private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    setHasOptionsMenu(true);//fragment can set item in menu
    sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
    sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    doodleView = view.findViewById(R.id.doodleView);
    acceleration = 0.00f;
    currentAcceleration = SensorManager.GRAVITY_EARTH;
    lastAcceleration = SensorManager.GRAVITY_EARTH;
    return view;
}

@Override
public void onResume() {
    super.onResume();
    sensorManager.registerListener(sensorEventListener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
}

@Override
public void onPause() {
    super.onPause();
    sensorManager.unregisterListener(sensorEventListener, sensorAccel);
}

private final SensorEventListener sensorEventListener = new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!DialogOnScreen) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            lastAcceleration = currentAcceleration;
            currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);
            if (currentAcceleration - lastAcceleration > ACCELERATION_THRESHOLD) {
                EraseImageDialogFragment dialogFragment = new EraseImageDialogFragment();
                dialogFragment.show(getFragmentManager(), "eraseDialog");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
};

@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.doodle_fragment_menu, menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.color:
            ColorDialogFragment colorDialogFragment = new ColorDialogFragment();
            colorDialogFragment.show(getFragmentManager(), "color dialog");
            return true;
        case R.id.line_width:
            LineWidthDialogFragment widthDialogFragment = new LineWidthDialogFragment();
            widthDialogFragment.show(getFragmentManager(), "line width dialog");
            return true;
        case R.id.delete:
            EraseImageDialogFragment dialogFragment = new EraseImageDialogFragment();
            dialogFragment.show(getFragmentManager(), "eraseDialog");
            return true;
        case R.id.save:
            saveImage();
            return true;
        case R.id.print:
            doodleView.printImage();
            return true;
    }
    return super.onOptionsItemSelected(item);
}

private void saveImage() {
    if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//check if the app has the permission
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.permission);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_IMAGE_PERMISSION_REQUEST_CODE);
                }
            });
            builder.create().show();
        } else {
            if (firstRequest) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_IMAGE_PERMISSION_REQUEST_CODE);
                firstRequest = false;
            } else {
                Snackbar snackbar = Snackbar.make(doodleView, R.string.snackbarPermission, Snackbar.LENGTH_LONG);
                snackbar.setDuration(8000);
                snackbar.show();
            }
        }
    } else {
        doodleView.saveImage();
    }
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == SAVE_IMAGE_PERMISSION_REQUEST_CODE && grantResults.length == 1) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doodleView.saveImage();
        }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
}

public DoodleView getDoodleView() {
    return doodleView;
}

public void setDialogOnScreen(boolean visible) {
    DialogOnScreen = visible;
}
}
