package com.vit.doodlz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
public class EraseImageDialogFragment extends DialogFragment {
private MainActivityFragment fragment;

@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(R.string.message_erase);
    builder.setPositiveButton(R.string.erase, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            fragment.getDoodleView().clear();
        }
    });
    builder.setNegativeButton(R.string.cancell, null);
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
}
