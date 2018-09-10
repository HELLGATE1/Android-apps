package com.vit.doodlz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
public class LineWidthDialogFragment extends DialogFragment {
private ImageView widthImageView;
private SeekBar seekBar;
private MainActivityFragment fragment;

@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    View lineWidthView = getActivity().getLayoutInflater().inflate(R.layout.line_width_dialog, null);
    builder.setView(lineWidthView);
    builder.setTitle(R.string.line_width_dialog);
    widthImageView = lineWidthView.findViewById(R.id.widthIV);
    seekBar = lineWidthView.findViewById(R.id.widthSB);
    final DoodleView doodleView = fragment.getDoodleView();
    seekBar.setOnSeekBarChangeListener(lineWidthChanged);
    seekBar.setProgress(doodleView.getLineWidth());
    builder.setPositiveButton(R.string.set_line_width, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            doodleView.setLineWidth(seekBar.getProgress());
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

private final SeekBar.OnSeekBarChangeListener lineWidthChanged = new SeekBar.OnSeekBarChangeListener() {
    Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Paint p = new Paint();
        p.setColor(fragment.getDoodleView().getDrawingColor());
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(progress);
        bitmap.eraseColor(getResources().getColor(android.R.color.transparent, getContext().getTheme()));
        canvas.drawLine(30, 50, 370, 50, p);
        widthImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
};
}
