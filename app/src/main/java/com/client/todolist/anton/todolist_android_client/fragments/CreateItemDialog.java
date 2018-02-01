package com.client.todolist.anton.todolist_android_client.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.client.todolist.anton.todolist_android_client.R;

public class CreateItemDialog extends DialogFragment {

    private static final String ARG_MODE = "ARG_MODE";
    private static final String ARG_PREFILLED_TEXT = "ARG_PREFILLED_TEXT";
    private static final String ARG_ITEM_NUM = "ARG_ITEM_NUM";

    NoticeDialogListener mListener;
    private EditText itemTextView;
    private EditMode editMode;
    private String prefilledText;
    private Integer itemNum;

    public CreateItemDialog() {
        // Required empty public constructor
    }

    public static CreateItemDialog newInstance(EditMode mode, String prefilledText, Integer itemNum) {
        CreateItemDialog fragment = new CreateItemDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MODE, mode);
        args.putString(ARG_PREFILLED_TEXT, prefilledText);
        args.putInt(ARG_ITEM_NUM, itemNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            editMode = (EditMode) getArguments().get(ARG_MODE);
            prefilledText = getArguments().getString(ARG_PREFILLED_TEXT);
            itemNum = getArguments().getInt(ARG_ITEM_NUM);
        }
    }

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

        itemTextView.setText(prefilledText);

        builder.setView(dialogView)
                .setTitle(editMode == EditMode.EDIT ? getString(R.string.create_item_header) : getString(R.string.edit_item_header))
                .setPositiveButton("Done", (dialog, id) -> {
                    switch (editMode) {
                        case EDIT:
                            mListener.onEditItem(itemNum, itemTextView.getText().toString());
                            break;
                        case CREATE:
                            mListener.onCreateItem(itemTextView.getText().toString());
                            break;
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> CreateItemDialog.this.getDialog().cancel());
        return builder.create();
    }

    public enum EditMode {
        EDIT, CREATE
    }

    public interface NoticeDialogListener {
        void onCreateItem(String itemText);

        void onEditItem(int elementId, String itemText);
    }
}
