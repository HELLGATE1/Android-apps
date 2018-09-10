package com.vit.flagquiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class MyDialogFragment extends DialogFragment {

@NonNull
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    MainActivityFragment fragment = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.quizFragment);
    builder.setCancelable(false);//click beyond alertDialog or on Back do nothing
    builder.setMessage(getString(R.string.results, fragment.attempt, (1000 / (double) fragment.attempt)));
    builder.setPositiveButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            fragment.resetQuiz();
        }
    });
    return builder.create();
}
}
