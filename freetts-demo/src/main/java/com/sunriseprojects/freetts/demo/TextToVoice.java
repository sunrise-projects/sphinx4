package com.sunriseprojects.freetts.demo;

import java.beans.PropertyVetoException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

public class TextToVoice {
	SynthesizerModeDesc desc;
	Synthesizer synthesizer;
	Voice voice;
	
	
	public TextToVoice() throws Exception {
		System.setProperty("mbrola.base", "C:\\lnx1\\home\\ggon\\git-projects\\mbrola");
		init("mbrola_us1");	
	}
	
	public void invoke(String input) throws Exception {
		doSpeak(input);		
	}
	
	
	public void close() throws Exception {
		terminate();
	}
	
	private void init(String voiceName) throws EngineException, AudioException,
			EngineStateError, PropertyVetoException {
		if (desc == null) {
			System.setProperty("freetts.voices",
					"de.dfki.lt.freetts.en.us.MbrolaVoiceDirectory");
			desc = new SynthesizerModeDesc(Locale.US);
			Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
			synthesizer = Central.createSynthesizer(desc);
			synthesizer.allocate();
			synthesizer.resume();
			
			
			//http://freetts.sourceforge.net/demo/JSAPI/MixedVoices/MixedVoices.java
			
			//adjust speed - normal
			//synthesizer.getSynthesizerProperties().setSpeakingRate(150.0f);
			
			//normal
			synthesizer.getSynthesizerProperties().setSpeakingRate(120.0f);
			
			//slow
			//synthesizer.getSynthesizerProperties().setSpeakingRate(100.0f);
			
			//normal pitch
			synthesizer.getSynthesizerProperties().setPitch(150);
			
			//high pitch
			//synthesizer.getSynthesizerProperties().setPitch(200);
			
			SynthesizerModeDesc smd = (SynthesizerModeDesc) synthesizer
					.getEngineModeDesc();
			Voice[] voices = smd.getVoices();
			Voice voice = null;
			for (int i = 0; i < voices.length; i++) {
				if (voices[i].getName().equals(voiceName)) {
					voice = voices[i];
					break;
				}
			}
			synthesizer.getSynthesizerProperties().setVoice(voice);
		}
	}

	private void terminate() throws EngineException, EngineStateError {
		synthesizer.deallocate();
	}

	public void doSpeak(String speakText) throws EngineException,
			AudioException, IllegalArgumentException, InterruptedException {
		synthesizer.speakPlainText(speakText, null);
		synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("mbrola.base", "C:\\lnx1\\home\\ggon\\git-projects\\mbrola");
		TextToVoice su = new TextToVoice();
		su.init("mbrola_us1");
		su.doSpeak("balance");
		su.doSpeak("weather");
		su.doSpeak("account manager");
		su.terminate();
	}
	
}