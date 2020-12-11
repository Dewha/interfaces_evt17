package com.example.eswallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SumDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog,null);
        builder.setView(view)
                .setTitle(R.string.sum)
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("OK", (dialog, which) -> {

                });
        TextInputLayout ti_sum;
        ti_sum = view.findViewById(R.id.tv_sum);
        return builder.create();
    }
}
