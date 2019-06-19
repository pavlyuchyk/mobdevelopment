package com.example.lab2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class dataView_activity extends AppCompatActivity {
    private HashMap<String, String[]> modelDictionary = new HashMap();
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_view_activity);

        File file = new File(getFilesDir(), "data");
        if(file.exists()){
            modelDictionary = readData(file);
        }
        if(modelDictionary.size()!= 0){
            Set<String> keys = modelDictionary.keySet();
            String[] brands = keys.toArray(new String[0]);
            TextView text =  findViewById(R.id.text_data);
            text.setText("Stored data:");
            for(int i=0;i<brands.length;i++){
                text.append(System.getProperty("line.separator"));
                text.append("Brand: " + brands[i]);
                List types = new LinkedList(Arrays.asList(modelDictionary.get(brands[i])));
                String[] modelsArray = new String[types.size()];
                types.toArray(modelsArray);
                for(int j=0;j<modelsArray.length;j++){
                    text.append(System.getProperty("line.separator"));
                    text.append("Model " + (j+1) + ": " + modelsArray[j]);
                }
                text.append(System.getProperty("line.separator"));
            }
        }
        else{
            TextView text =  findViewById(R.id.text_data);
            text.append("No data saved in storage");
        }

        backButton = findViewById(R.id.back_to_main);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
}
