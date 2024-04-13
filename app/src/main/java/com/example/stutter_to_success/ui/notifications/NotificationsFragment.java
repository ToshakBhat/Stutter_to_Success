package com.example.stutter_to_success.ui.notifications;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.stutter_to_success.ExpertActivity;
import com.example.stutter_to_success.GeminiPro;
import com.example.stutter_to_success.R;
import com.example.stutter_to_success.ResponseCallback;
import com.example.stutter_to_success.databinding.FragmentNotificationsBinding;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

public class NotificationsFragment extends Fragment {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    int randomNumber;
    public String[] targets = {"Finance","Apple","Honesty","Lion","Mango","Hackathon","Sunglasses","College","Trust","Tea"};
    private FragmentNotificationsBinding binding;
    TextView target_textView;
    ProgressBar progress;
    Button record;
    Button play;
    TextToSpeech t1;
    Button expert;
    Button send_to_expert;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) {
            Toast.makeText(getContext(), "Error requesting audio", Toast.LENGTH_LONG).show();
        }
    }



    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        progress.setVisibility(View.VISIBLE);


        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        progress.setVisibility(View.GONE);
        expert.setVisibility(View.VISIBLE);
        recorder.release();
        recorder = null;
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        target_textView = root.findViewById(R.id.target_textview);
        progress = root.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        expert = root.findViewById(R.id.expert);
        LinearLayout tgt = root.findViewById(R.id.tgt);
        send_to_expert = root.findViewById(R.id.send_to_expert);
        Random random = new Random();

        // Generate a random integer within a specified range
        randomNumber = getRandomNumberInRange(1, 10); // Example: Generate a random number between 1 and 100



        System.out.println("Random number: " + randomNumber);

        t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });



        get_response();

        fileName = requireActivity().getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        LinearLayout ll = new LinearLayout(getContext());


        Button record = new RecordButton(getContext());
        LinearLayout.LayoutParams recordParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        recordParams.setMargins(90, 0, 0, 0); // Set left margin to 20dp
        //record.setBackground(R.color.blue_500);
        ll.addView(record, recordParams);

        Button play = new PlayButton(getContext());
        //play.setBackground(R.color.blue_500);
        LinearLayout.LayoutParams playParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        playParams.setMargins(90, 0, 0, 0); // Set right margin to 20dp
        ll.addView(play, playParams);
        tgt.addView(ll);

        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(target_textView.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            }
        });

        send_to_expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ExpertActivity.class);
                startActivity(i);
            }
        });


        return root;





    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        // Calculate the range
        int range = max - min + 1;

        // Generate a random number within the range
        return min + (int) (Math.random() * range);
    }


    public void get_response(){
        GeminiPro model = new GeminiPro();

        model.getResponse("Generate a short random paragraph on the keyword "+targets[randomNumber]+" for an individual.", new ResponseCallback() {
            @Override
            public void onResponse(String response) {
                target_textView.setText(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}