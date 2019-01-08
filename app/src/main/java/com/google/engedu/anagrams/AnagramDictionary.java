/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static final String TAG = "AnagramActivity";
    private Random random = new Random();
    private List<String> wordList = new ArrayList<>(); // dictionary
    private Set<String> wordSet = new HashSet<>();
    private Map<String,List<String>> lettersToWord = new HashMap<>();
    private Map<Integer, List<String>> sizeToWords= new HashMap<>(); // key: the length of the word
                                                                        // value: words of that length
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordSet.add(word);

            wordList.add(word);

            int wrdLeng = word.length(); // the length of the word we read in
            // if the map does not have this length (key)
            // create a new arraylist with the key
            if (!sizeToWords.containsKey(wrdLeng)) {
                sizeToWords.put(wrdLeng, new ArrayList<String>());
            }
            // put the length(key) and the word into the map
            sizeToWords.get(wrdLeng).add(word);



            String key = sortLetters(word);
            if (!lettersToWord.containsKey(key)) {
                lettersToWord.put(key, new ArrayList<String>());
            }
            lettersToWord.get(key).add(word);



        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word) || word.toLowerCase().contains(base.toLowerCase())) {
            Log.i("Alexis", "it is false");
            return false;
        }

        return true;
    }


    // returns all anagrams of 'target word' in dictionary
    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        int targetLength = targetWord.length();
        String targetWordSorted = sortLetters(targetWord);

        for (String word : wordList) {
            if(word.length() == targetLength && sortLetters(word).equals(targetWordSorted)) {
                result.add(word);
            }
        }
        return result;
    }

    // helper function (call it sortLetters) that
    // takes a String and returns another String with the same letters in alphabetical order
    // (e.g. "post" -> "opst").
    private String sortLetters(String originWord) {
        char temp[] = originWord.toCharArray();

        Arrays.sort(temp);

        return new String (temp);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        char ch = 'a';
        while (ch <= 'z') {
            String newWord = word + ch;
            List<String> anagramList  = getAnagrams(newWord);
            result.addAll(anagramList);

            ch++;
        }

        return result;
    }

    public List<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        // for loop to get anagrams of 2 more letters
        // first for loop for the first letter added
        // second for loop for the second letter added
        for (char c = 'a'; c <= 'z'; c++) {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                String newWord = word + ch + c;
                List<String> anagramList  = getAnagrams(newWord);
                result.addAll(anagramList);
            }
        }

        return result;
    }

    // word <- starter word
    public List<String> getAnagramsOfTwoValidWords(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char ch = 'a'; ch <= 'z'; ch++) {
            String newWord = word + ch;


        }
        return result;
    }

    public String pickGoodStarterWord() {
        //Log.i(TAG, "sizeToWords.get(4)" + sizeToWords.get(4));

        List<String> words = sizeToWords.get(wordLength); // this list holds the words of the same
                                                            // length in the 'sizeToWords' Hashmap

        Random rand = new Random(); // random variable to start at random part in the list 'words'
        int startPoint = rand.nextInt(words.size()); // start point is the random number that runs
                                                            // from 0 to size of that list
        int numAnagrams = 0;
        numAnagrams = getAnagramsWithOneMoreLetter(words.get(startPoint)).size();
        while (numAnagrams < MIN_NUM_ANAGRAMS) {
            startPoint++;
            if (startPoint >= words.size()) {
                startPoint = 0;
            }
            numAnagrams = getAnagramsWithOneMoreLetter(words.get(startPoint)).size();
        }
//        String result = "stop";
//
//        while (startPoint < words.size()) {
//            if (getAnagramsWithOneMoreLetter(words.get(startPoint)).size() > MIN_NUM_ANAGRAMS){
//                result = words.get(startPoint);
//                break;
//               //return words.get(startPoint);
//            }
//            startPoint++;
//            if (startPoint >= words.size()) {
//                startPoint = 0;
//            }
//        }

        // if the wordLength is smaller than the maximum word length
        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++; // increase next amount of letters in the word
        }
        return (words.get(startPoint));
    }
}
