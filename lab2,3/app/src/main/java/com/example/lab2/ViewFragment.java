package com.example.lab2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class ViewFragment extends Fragment {
    private Context appContext;
    private Button openDialog, changeFragment, checkData;
    private RadioGroup mRadioGroup;
    public HashMap<String, String[]> modelDictionary = new HashMap();
    public String[] modelArrayApple = new String[]{"IPhone X", "IPhone 8", "IPhone SE", "IPhone 6S"};
    public String[] modelArraySamsung = new String[]{"Galaxy S10", "Galaxy A80", "Galaxy M10"};
    public String[] modelArrayNokia = new String[]{"Nokia 3310", "Nokia E71", "Nokia 7650"};
    public String[] brandArray = new String[]{"Apple", "Samsung", "Nokia"};

    private OnFragmentInteractionListener mListener;

    public ViewFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ViewFragment newInstance(String param1, String param2) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.view_fragment, container, false);
        File file = new File(appContext.getFilesDir(), "data");
        if(file.exists()){
            modelDictionary = readData(file);
        }
        else{
            modelDictionary.put("Apple", modelArrayApple);
            modelDictionary.put("Samsung", modelArraySamsung);
            modelDictionary.put("Nokia", modelArrayNokia);
        }

        mRadioGroup = view.findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadio = view.findViewById(checkedId);
                String[] models = modelDictionary.get(checkedRadio.getText()) != null? modelDictionary.get(checkedRadio.getText()):new String[]{"Empty"};
                Spinner modelList = view.findViewById(R.id.type);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, models);
                modelList.setAdapter(spinnerAdapter);
            }
        });
        for(int i=0;i<brandArray.length;i++){
            RadioButton rb = new RadioButton(appContext);
            rb.setText(brandArray[i]);
            rb.setId(i);
            mRadioGroup.addView(rb);
        }
        mRadioGroup.check(0);

        openDialog = view.findViewById(R.id.open_dialog);
        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                String model = ((Spinner)view.findViewById(R.id.type)).getSelectedItem().toString();
                data.putString("model",model);
                RadioButton rb = view.findViewById(mRadioGroup.getCheckedRadioButtonId());
                String brand = rb.getText().toString();
                data.putString("brand",brand);
                MyDialog dialog = new MyDialog();
                dialog.setArguments(data);
                dialog.show(getFragmentManager(), "Dialog");
            }
        });

        changeFragment = view.findViewById(R.id.manage_fragment_navigation);
        changeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("dictionary", modelDictionary);

                ManageFragment manageFragment = new ManageFragment();

                manageFragment.setArguments(data);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, manageFragment);

                transaction.commit();
            }
        });

        checkData = view.findViewById(R.id.check_data);
        checkData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SHOW_DATA");
                appContext.startActivity(intent);
            }
        });

        return view;
    }

    private HashMap readData(File file){
        HashMap data = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
             data = (HashMap) ois.readObject();
            ois.close();
            return data;
        }
        catch (Exception e){
            Log.w("read error", e.getMessage());
        }

        return data;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        appContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
