package edu.cmu.sphinx.demo.grammar;

/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 * 
 * See the file "license.terms" for information on usage and
 * redistribution of this file in the Sphinx package, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 * 
 * i have just modified the actual HelloWorld.java file by adding few lines of code.
 * make sure you have a look on the grammar file that i have uploaded.
 * all the best.
 *
 */


import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import com.sunriseprojects.freetts.demo.TextToVoice;

/**
 * A simple HelloWorld demo showing a simple speech application 
 * built using Sphinx-4. This application uses the Sphinx-4 endpointer,
 * which automatically segments incoming audio into utterances and silences.
 */
public class GrammarDemo {

    /**
     * Main method for running the HelloWorld demo.
     */
	static int i=1;
	static String resultText;
	
    private static final String ACOUSTIC_MODEL =
            "resource:/edu/cmu/sphinx/models/en-us/en-us";
        private static final String DICTIONARY_PATH =
            "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
        private static final String GRAMMAR_PATH =
            "resource:/edu/cmu/sphinx/demo/grammar/";

    public static void main(String[] args) throws Exception {
		
    	TextToVoice voice = new TextToVoice();
    	
    	
    	
    	try {

            System.out.println("Loading...");

            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath(ACOUSTIC_MODEL);
            configuration.setDictionaryPath(DICTIONARY_PATH);
            configuration.setGrammarPath(GRAMMAR_PATH);
            configuration.setUseGrammar(true);
            
            configuration.setGrammarName("sample");
            LiveSpeechRecognizer recognizer =
                    new LiveSpeechRecognizer(configuration);
            
  
            recognizer.startRecognition(true);
  
            while (true) 
            {
            	
            	
		    System.out.println("Start speaking. Press Ctrl-C to quit.\n");

   
		    String result = recognizer.getResult().getHypothesis();
		    
		    if (result != null)
		    {
		    	
		    		System.out.println("Enter your choise"+ "\n");
			 resultText = result; //result.getBestFinalResultNoFiller();
			System.out.println("You said: " + resultText + "\n");
			
			voice.invoke(resultText);
		    }
			
            }
      
            
        } catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } 

    	
    	voice.close();
    }
}