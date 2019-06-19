package com.example.mob4;

import android.content.Context;
import android.media.MediaPlayer;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;


public class AudioFragment extends Fragment {

    private Context appContext;
    private MediaPlayer player;
    private Field[] rawFiles;
    private String currentSong;
    private Button bStop,bPlay,bPause, fragChange;
    private Integer pauseTime;
    public AudioFragment() {
        // Required empty public constructor
    }

    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
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
        final View view = inflater.inflate(R.layout.fragment_audio, container, false);
        rawFiles=R.raw.class.getFields();
        List<String> fileNames = new LinkedList();
        for(int i=0; i < rawFiles.length; i++){
            String name = rawFiles[i].getName();
            if(name.split("_")[0].equals("audio")){
                fileNames.add(ParseAudioName(rawFiles[i].getName()));
            }
        }
        Spinner modelList = view.findViewById(R.id.audioFileName);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(appContext, android.R.layout.simple_spinner_dropdown_item, fileNames);
        modelList.setAdapter(spinnerAdapter);

        bStop = view.findViewById(R.id.stopButton);
        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player != null){
                    player.stop();
                    player.release();
                    player = null;
                    currentSong = null;
                    Toast.makeText(appContext, "Stoped", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bPlay = view.findViewById(R.id.playButton);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner list = view.findViewById(R.id.audioFileName);
                String name = list.getSelectedItem().toString();
                if(name != currentSong){
                    if(player != null){
                        player.release();
                        player=null;
                    }
                    for(int i = 0; i <rawFiles.length;i++) {
                        if (ConvertAudioName(name).equals(rawFiles[i].getName())) {
                            try {
                                currentSong = name;
                                Integer id = rawFiles[i].getInt(rawFiles[i]);
                                player = MediaPlayer.create(appContext, id);
                                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        player.stop();
                                        player.release();
                                        player = null;
                                        currentSong = null;
                                    }
                                });
                                player.start();
                                break;
                            } catch (IllegalAccessException e) {
                            }
                        }
                    }
                }
                else{
                    player.seekTo(pauseTime);
                    player.start();
                }
            }
        });

        bPause = view.findViewById(R.id.pauseButton);
        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player!=null){
                    player.pause();
                    pauseTime = player.getCurrentPosition();
                    Toast.makeText(appContext, "Paused", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragChange = view.findViewById(R.id.check_video);
        fragChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(player != null){
                   player.release();
                   player = null;
               }
                VideoFragment videoFragment = new VideoFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, videoFragment);

                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        appContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String ParseAudioName(String name){
        String parsedName = new String();
        String[] splitName = name.split("_");
        for(int i=1;i<splitName.length-1;i++){
            parsedName += splitName[i] + " ";
        }

        parsedName +="- " + splitName[splitName.length-1].replace(splitName[splitName.length-1].charAt(0),Character.toUpperCase(splitName[splitName.length-1].charAt(0)));
        parsedName = parsedName.replace(parsedName.charAt(0),Character.toUpperCase(parsedName.charAt(0)));
        return parsedName;
    }

    private String ConvertAudioName(String name){
        String convertedName = "audio_";

        String[] splitName = name.split(" ");
        for(int i=0;i<splitName.length-2;i++){
            convertedName += splitName[i] + "_";
        }
        String group = splitName[splitName.length-1];
        group = group.replace(group.charAt(0),Character.toLowerCase(group.charAt(0)));
        splitName[splitName.length-1] = group;
        convertedName += splitName[splitName.length-1];
        convertedName = convertedName.replace(convertedName.charAt(6),Character.toLowerCase(convertedName.charAt(6)));
        Log.w("name", convertedName);
        return convertedName;
    }
}
