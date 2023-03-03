package com.tudux.app

import com.sun.speech.freetts.{Voice, VoiceManager}
import com.sun.speech.freetts.audio.{AudioPlayer, SingleFileAudioPlayer}

import java.util.Locale
import javax.speech.Central
import javax.speech.synthesis.{Synthesizer, SynthesizerModeDesc}

object FreeTTS extends App {
  System.setProperty("freetts.voices",
    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")

  Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral")
  val synt: Synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US))

  synt.allocate()
  synt.resume()

  val audioPlayer: AudioPlayer = new SingleFileAudioPlayer(".\\mywav",javax.sound.sampled.AudioFileFormat.Type.WAVE)
  synt.speakPlainText("Blender is a great software for doing all kinds of 3D stuff", null)
  synt.waitEngineState(Synthesizer.QUEUE_EMPTY)
  synt.deallocate()
}

object FreeTTSSave extends App {

  System.setProperty("freetts.voices",
    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")
  Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral")

  val voice: Voice = VoiceManager.getInstance().getVoice("kevin16")

  //get all voices
  val voiceList = VoiceManager.getInstance().getVoices();
  println(voiceList.length)
  println(voice)
  voiceList.foreach(x => println(s"voice ${x.getName}x"))

  voice match {
    case null => println("Error")
    case _ =>
      voice.allocate()
      val someText = "The line consisted of mecha model kits imported from Japan and featured in anime titles such as Super Dimension Fortress Macross (1982), Super Dimension Century Orguss (1983) and Fang of the Sun Dougram (1981). The kits were originally intended to be a marketing tie-in to a similarly named comic book series by DC Comics, which was cancelled after only two issues.At the same time, Harmony Gold licensed the Macross TV series for direct-to-video distribution in 1984, but their merchandising plans were compromised by Revell's prior distribution of the Macross kits. In the end, both parties signed a co-licensing agreement and the Robotech name was adopted for the TV syndication of Macross combined with Super Dimension Cavalry Southern Cross (1984) and Genesis Climber MOSPEADA"
      println(s"Voice rate: ${voice.getRate}")
      println(s"Voice pitch: ${voice.getPitch}")
      println(s"Voice volume: ${voice.getVolume}")
      val audioPlayer: AudioPlayer = new SingleFileAudioPlayer(".//wavesample",javax.sound.sampled.AudioFileFormat.Type.WAVE)
      voice.setAudioPlayer(audioPlayer)
      val status: Boolean = voice.speak(someText)
      println(s"Status: $status")
      audioPlayer.close()
      voice.deallocate()

  }

}
