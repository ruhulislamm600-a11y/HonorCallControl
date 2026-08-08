package com.example.honorcallcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView statusText;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);

        Button receiveButton = findViewById(R.id.receiveButton);
        Button endButton = findViewById(R.id.endButton);
        Button voiceButton = findViewById(R.id.voiceButton);

        receiveButton.setOnClickListener(v ->
                statusText.setText("কল রিসিভ করার কমান্ড দেওয়া হয়েছে"));

        endButton.setOnClickListener(v ->
                statusText.setText("কল কেটে দেওয়ার কমান্ড দেওয়া হয়েছে"));

        voiceButton.setOnClickListener(v -> startVoiceRecognition());
    }

    private void startVoiceRecognition() {

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            statusText.setText("Voice Recognition পাওয়া যায়নি");
            return;
        }

        speechRecognizer =
                SpeechRecognizer.createSpeechRecognizer(this);

        Intent intent =
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "bn-BD"
        );

        speechRecognizer.setRecognitionListener(
                new android.speech.RecognitionListener() {

                    @Override
                    public void onResults(Bundle results) {

                        ArrayList<String> matches =
                                results.getStringArrayList(
                                        SpeechRecognizer.RESULTS_RECOGNITION
                                );

                        if (matches != null && !matches.isEmpty()) {

                            String text =
                                    matches.get(0).toLowerCase(
                                            Locale.getDefault()
                                    );

                            if (text.contains("কল রিসিভ")
                                    || text.contains("কল ধর")
                                    || text.contains("কল ধরো")) {

                                statusText.setText(
                                        "কল রিসিভ কমান্ড শনাক্ত হয়েছে"
                                );

                            } else if (text.contains("কল কেটে")
                                    || text.contains("কল কাট")
                                    || text.contains("কল শেষ")) {

                                statusText.setText(
                                        "কল কাটার কমান্ড শনাক্ত হয়েছে"
                                );

                            } else {

                                statusText.setText(
                                        "কমান্ড বোঝা যায়নি: " + text
                                );
                            }
                        }
                    }

                    @Override public void onReadyForSpeech(Bundle b) {}
                    @Override public void onBeginningOfSpeech() {}
                    @Override public void onRmsChanged(float v) {}
                    @Override public void onBufferReceived(byte[] b) {}
                    @Override public void onEndOfSpeech() {}
                    @Override public void onError(int i) {}
                    @Override public void onPartialResults(Bundle b) {}
                    @Override public void onEvent(int i, Bundle b) {}
                }
        );

        speechRecognizer.startListening(intent);
        statusText.setText("শুনছি...");
    }

    @Override
    protected void onDestroy() {

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }

        super.onDestroy();
    }
}
