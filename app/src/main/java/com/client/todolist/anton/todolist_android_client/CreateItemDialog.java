package com.client.todolist.anton.todolist_android_client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CreateItemDialog extends DialogFragment{

    private EditText itemTextView;

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String itemText);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.create_item_dialog_layout, null);
        itemTextView = dialogView.findViewById(R.id.newItemEditText);

        builder.setView(dialogView)
                .setTitle("Add new item")
                .setPositiveButton("Create", (dialog, id) -> mListener.onDialogPositiveClick(CreateItemDialog.this,
                        itemTextView.getText().toString()))
                .setNegativeButton("Cancel", (dialog, id) -> CreateItemDialog.this.getDialog().cancel());
        return builder.create();
    }
}
