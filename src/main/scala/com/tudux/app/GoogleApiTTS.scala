package com.tudux.app

import com.google.cloud.texttospeech.v1.AudioConfig
import com.google.cloud.texttospeech.v1.AudioEncoding
import com.google.cloud.texttospeech.v1.SsmlVoiceGender
import com.google.cloud.texttospeech.v1.SynthesisInput
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse
import com.google.cloud.texttospeech.v1.TextToSpeechClient
import com.google.cloud.texttospeech.v1.VoiceSelectionParams
import com.google.protobuf.ByteString
import java.io.FileOutputStream
import java.io.OutputStream
// Imports the Google Cloud client library


object GoogleApiTTS extends App {

  val  textToSpeechClient: TextToSpeechClient = TextToSpeechClient.create()
  val input = SynthesisInput.newBuilder.setText("Hello, World!").build

  // Build the voice request, select the language code ("en-US") and the ssml voice gender
  // ("neutral")
   val voice: VoiceSelectionParams =
    VoiceSelectionParams.newBuilder()
      .setLanguageCode("en-US")
      .setSsmlGender(SsmlVoiceGender.NEUTRAL)
      .build();

  val audioConfig: AudioConfig =
    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

  // Perform the text-to-speech request on the text input with the selected voice parameters and
  // audio file type
  val response: SynthesizeSpeechResponse =
    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

  // Get the audio contents from the response
  val audioContents: ByteString = response.getAudioContent();

   val out: OutputStream = new FileOutputStream("output.mp3")
  out.write(audioContents.toByteArray)
  println("Audio content written to file \"output.mp3\"")

}
