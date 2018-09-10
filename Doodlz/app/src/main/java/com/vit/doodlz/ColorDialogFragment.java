package com.vit.doodlz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
public class ColorDialogFragment extends DialogFragment {
private SeekBar alphaSeekBar;
private SeekBar redSeekBar;
private SeekBar greenSeekBar;
private SeekBar blueSeekBar;
private View colorView;
private int color;
private MainActivityFragment fragment;

@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    View colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.color_dialog, null);
    builder.setView(colorDialogView);
    builder.setTitle(R.string.color_dialog);
    alphaSeekBar = colorDialogView.findViewById(R.id.alphaSB);
    redSeekBar = colorDialogView.findViewById(R.id.redSB);
    greenSeekBar = colorDialogView.findViewById(R.id.greenSB);
    blueSeekBar = colorDialogView.findViewById(R.id.blueSB);
    colorView = colorDialogView.findViewById(R.id.colorView);
    alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
    redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
    greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
    blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);
    final DoodleView doodleView = fragment.getDoodleView();
    color = doodleView.getDrawingColor();
    alphaSeekBar.setProgress(Color.alpha(color));
    redSeekBar.setProgress(Color.red(color));
    greenSeekBar.setProgress(Color.green(color));
    blueSeekBar.setProgress(Color.blue(color));
    colorView.setBackgroundColor(color);
    builder.setPositiveButton(R.string.set_color, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            doodleView.setDrawingColor(color);
        }
    });
    return builder.create();
}

@Override
public void onAttach(Context context) {
    super.onAttach(context);
    fragment = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.doodleFragment);
    if (fragment != null)
        fragment.setDialogOnScreen(true);
}

@Override
public void onDetach() {
    super.onDetach();
    if (fragment != null)
        fragment.setDialogOnScreen(false);
}

private final SeekBar.OnSeekBarChangeListener colorChangedListener = new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            color = Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(), greenSeekBar.getProgress(), blueSeekBar.getProgress());
            colorView.setBackgroundColor(color);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }
};
}
