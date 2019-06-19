package com.example.lab2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class MyDialog extends DialogFragment {

    private TextView mActionOk, brand, model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_dialog, container,false);
        mActionOk = view.findViewById(R.id.action_ok);
        brand = view.findViewById(R.id.brand);
        brand.setText(getArguments().getString("brand"));
        model = view.findViewById(R.id.model);
        model.setText(getArguments().getString("model"));

      mActionOk.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v) {
              getDialog().dismiss();
          }
      });

      return view;
    }
}