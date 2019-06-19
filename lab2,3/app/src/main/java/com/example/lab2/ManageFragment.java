package com.example.lab2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ManageFragment extends Fragment {
    private Context appContext;
    private Button backButton, saveButton, addButton;
    private RadioGroup radioGroup;
    private HashMap<String, String[]> modelDictionary = new HashMap();
    private HashMap<String, String[]> updatedDictionary = new HashMap();
    private boolean changesMade;
    private boolean changesSaved;
    private OnFragmentInteractionListener mListener;

    public ManageFragment() {
        // Required empty public constructor
    }

    public static ManageFragment newInstance(HashMap map) {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        args.putSerializable("dictionary", map);
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
        final View view = inflater.inflate(R.layout.manage_fragment, container, false);
        changesMade = false;
        changesSaved = false;
        modelDictionary = (HashMap)getArguments().getSerializable("dictionary");
        updatedDictionary = (HashMap)getArguments().getSerializable("dictionary");
        radioGroup = view.findViewById(R.id.manage_radioGroup);
        Set<String> keys = modelDictionary.keySet();
        String[] brands = keys.toArray(new String[0]);
        for(int i=0;i<brands.length;i++){
            RadioButton rb = new RadioButton(appContext);
            rb.setText(brands[i]);
            rb.setId(i);
            radioGroup.addView(rb);
        }
        radioGroup.check(0);

        addButton = view.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = view.findViewById(R.id.add_type);
                RadioGroup manageRadioGroup = view.findViewById(R.id.manage_radioGroup);
                String brand = ((RadioButton)view.findViewById(manageRadioGroup.getCheckedRadioButtonId())).getText().toString();
                String newType = text.getText().toString();
                if(newType.length() == 0){
                    Toast toast = Toast.makeText(appContext, "Enter new type name",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,0,200);
                    toast.show();
                }
                else if(Arrays.asList(updatedDictionary.get(brand)).contains(newType)){
                    Toast toast = Toast.makeText(appContext, "This model already exists for selected brand",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,0,200);
                    toast.show();
                }
                else{
                    if(!changesMade){
                        changesMade = true;
                    }
                    List types = new LinkedList(Arrays.asList(updatedDictionary.get(brand)));
                    types.add(newType);
                    String[] typesArray = new String[types.size()];
                    types.toArray(typesArray);
                    updatedDictionary.put(brand,typesArray);
                    text.setText("");
                    Toast toast = Toast.makeText(appContext, "Option has been added",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,0,200);
                    toast.show();
                }

                modelDictionary = updatedDictionary;
            }
        });

        saveButton = view.findViewById(R.id.save_changes);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changesMade){
                    Toast toast = Toast.makeText(appContext, "No changes have been made",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,0,200);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(appContext, "Changes saved", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 200);
                    toast.show();
                    try{
                        modelDictionary = updatedDictionary;
                        File file = new File(appContext.getFilesDir(), "data");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
                        oos.writeObject(updatedDictionary);
                        oos.close();
                    }
                    catch (Exception e){
                        Log.w("exception", e.getMessage());
                    }


                    modelDictionary = updatedDictionary;
                }
            }
        });

        backButton = view.findViewById(R.id.view_fragment_navigation);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFragment viewFragment = new ViewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, viewFragment);
                transaction.commit();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
