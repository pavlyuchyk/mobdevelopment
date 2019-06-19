package com.example.mob4;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.VideoView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class VideoFragment extends Fragment {
    private Context appContext;
    private Button fragChange, bPlay,bPause,bStop;
    private Field[] rawFiles;
    private VideoView videoPlayer;
    String currentVideo;
    Integer pauseTime;
    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
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
        final View view = inflater.inflate(R.layout.fragment_video, container, false);
        rawFiles=R.raw.class.getFields();
        List<String> fileNames = new LinkedList();
        for(int i=0; i < rawFiles.length; i++){
            String name = rawFiles[i].getName();
            if(name.split("_")[0].equals("video")){
                fileNames.add(ParseVideoName(rawFiles[i].getName()));
            }
        }
        Spinner modelList = view.findViewById(R.id.videoFileName);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(appContext, android.R.layout.simple_spinner_dropdown_item, fileNames);
        modelList.setAdapter(spinnerAdapter);
        fragChange = view.findViewById(R.id.check_audio);
        fragChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.stopPlayback();
                videoPlayer=null;

                AudioFragment audioFragment = new AudioFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, audioFragment);

                transaction.commit();
            }
        });

        bPlay = view.findViewById(R.id.playButton);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner list = view.findViewById(R.id.videoFileName);
                String name = list.getSelectedItem().toString();
                if(name != currentVideo){
                    for(int i = 0; i <rawFiles.length;i++) {
                        String fileName = rawFiles[i].getName();
                        Log.w("comparison",Boolean.toString(ConvertVideoName(name).equals(rawFiles[i].getName())));
                        if(fileName.split("_")[0].equals("video") && ConvertVideoName(name).equals(fileName)){
                            try{
                                currentVideo = name;
                                Integer id = rawFiles[i].getInt(rawFiles[i]);
                                Uri videoUri= Uri.parse( "android.resource://" + appContext.getPackageName() + "/" + id);
                                videoPlayer =  view.findViewById(R.id.videoPlayer);
                                videoPlayer.setVideoURI(videoUri);
                                videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        videoPlayer.stopPlayback();
                                        videoPlayer.resume();
                                    }
                                });

                                videoPlayer.start();
                                break;
                            }
                            catch(IllegalAccessException e){
                            }
                        }
                    }
                }
                else{
                    videoPlayer.seekTo(pauseTime);
                    videoPlayer.start();
                }
            }
        });

        bPause = view.findViewById(R.id.pauseButton);
        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoPlayer!=null){
                    videoPlayer.pause();
                    pauseTime = videoPlayer.getCurrentPosition();
                    Toast.makeText(appContext, "Paused", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bStop = view.findViewById(R.id.stopButton);
        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoPlayer != null){
                    videoPlayer.stopPlayback();
                    videoPlayer.resume();
                    currentVideo = null;
                    Toast.makeText(appContext, "Stoped", Toast.LENGTH_SHORT).show();
                }
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

    private String ParseVideoName(String name){
        String parsedName = new String();
        String[] splitName = name.split("_");
        for(int i=1;i<splitName.length;i++){
            parsedName += splitName[i] + " ";
        }

        return parsedName;
    }

    private String ConvertVideoName(String name){
        String convertedName = "video_";

        String[] splitName = name.split(" ");
        for(int i=0;i<splitName.length-1;i++){
            convertedName += splitName[i] + "_";
        }
        String group = splitName[splitName.length-1];
        group = group.replace(group.charAt(0),Character.toLowerCase(group.charAt(0)));
        splitName[splitName.length-1] = group;
        convertedName += splitName[splitName.length-1];
        Log.w("name", convertedName);
        return convertedName;
    }
}
