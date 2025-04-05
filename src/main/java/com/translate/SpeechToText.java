package com.translate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

public class SpeechToText {
    public static String convertAudioToText(String filePath, String languageCode) throws IOException {
       // Read the audio file into memory.
       Path path = Paths.get(filePath);
       byte[] data = Files.readAllBytes(path);
       ByteString audioBytes = ByteString.copyFrom(data);

       // Initialize the SpeechClient.
       try (SpeechClient speechClient = SpeechClient.create()) {
           // Configure the request with the audio encoding, sample rate, and dynamic language code.
           RecognitionConfig config = RecognitionConfig.newBuilder()
                   .setEncoding(AudioEncoding.LINEAR16)
                   .setSampleRateHertz(16000)
                   .setLanguageCode(languageCode)
                   .build();

           // Build the audio request.
           RecognitionAudio audio = RecognitionAudio.newBuilder()
                   .setContent(audioBytes)
                   .build();

           // Perform synchronous speech recognition.
           RecognizeResponse response = speechClient.recognize(config, audio);
           StringBuilder transcript = new StringBuilder();
           for (SpeechRecognitionResult result : response.getResultsList()) {
               // Get the most likely transcription for this portion.
               SpeechRecognitionAlternative alternative = result.getAlternatives(0);
               transcript.append(alternative.getTranscript());
           }
           return transcript.toString();
       }
   }
}
