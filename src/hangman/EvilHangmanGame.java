package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private Set<String> words = new HashSet<String>();
    private String masterKey = "";
    private int masterWordLength = 0;
    private Map<String, Set<String>> map = new HashMap<String, Set<String>>();
    private SortedSet<Character> guessedLetters = new TreeSet<Character>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        if (dictionary == null || dictionary.length() == 0 || !dictionary.exists()) {
            throw new EmptyDictionaryException("Dictionary is empty.");
        }
        if (wordLength < 2) {
            throw new EmptyDictionaryException("Dictionary is empty.");
        }

        words.clear();
        masterKey = "";
        masterWordLength = wordLength;

        Scanner scanner = new Scanner(dictionary);
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine().toString().toLowerCase();
            if (word.length() == wordLength) {
                words.add(word);
            }
        }
        if (words.isEmpty()){
            throw new EmptyDictionaryException("Dictionary is empty.");
        }

        for (int i = 0; i < wordLength; i++) {
            masterKey += '-';
        }

        map.put(masterKey, words);

    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        String guessCast = String.valueOf(guess);
        guessCast = guessCast.toLowerCase();
        guess = guessCast.charAt(0);
        String currentKey = masterKey;
        Map<String, Set<String>> currentMap = new HashMap<String, Set<String>>();
        if (guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException("You already used that letter");
        } else {
            guessedLetters.add(guess);
        }

        for (String s : words) {
            Set<String> currentWords = new HashSet<>();
            currentKey = masterKey;
            for (int i = 0; i < s.length(); i++) {
                if (i == s.length() - 1) {
                    if (s.charAt(i) == guess && masterKey.charAt(i) == '-') {
                        currentKey = currentKey.substring(0, i) + (guess);
                    }
                } else {
                    if (s.charAt(i) == guess && masterKey.charAt(i) == '-') {
                        currentKey = currentKey.substring(0, i) + (guess) + (currentKey.substring(i + 1));
                    }
                }
            }

            if (currentMap.containsKey(currentKey)) {
                currentMap.get(currentKey).add(s);
            } else {
                Set<String> currentSet = new HashSet<>();
                currentSet.add(s);
                currentMap.put(currentKey, currentSet);
            }

        }

        int currentCount = 0;
        currentKey = currentMap.keySet().iterator().next();
        for (String keys : currentMap.keySet()) {

            if (currentMap.get(keys).size() > currentCount) {
                currentCount = currentMap.get(keys).size();
                currentKey = keys;
            }
                else if (currentMap.get(keys).size() == currentCount) {
                    if (currentMap.get(keys).size() > currentCount) {
                        currentCount = currentMap.get(keys).size();
                        currentKey = keys;
                    } else if (currentMap.get(keys).size() == currentCount) {
                        int keyGuessCount = 0;
                        int keyLetterCount = 0;
                        int countGuessCount = 0;
                        int countLetterCount = 0;
                        for (int i = 0; i < keys.length(); i++) {
                            if (keys.charAt(i) == guess) {
                                keyGuessCount += 1;
                                keyLetterCount += 1;
                            } else if (keys.charAt(i) != '-') {
                                keyLetterCount += 1;
                            }
                            if (currentKey.charAt(i) == guess) {
                                countGuessCount += 1;
                                countLetterCount += 1;
                            } else if (currentKey.charAt(i) != '-') {
                                countLetterCount += 1;
                            }
                        }
                        if (keyGuessCount == 0) {
                            currentCount = currentMap.get(keys).size();
                            currentKey = keys;
                        } else if (keyLetterCount < countLetterCount) {
                            currentCount = currentMap.get(keys).size();
                            currentKey = keys;
                        } else if (keyLetterCount == countLetterCount) {
                            for (int j = keys.length() - 1; j >= 0; j--) {

                                if (keys.charAt(j) == guess && currentKey.charAt(j) != guess) {
                                    currentCount = currentMap.get(keys).size();
                                    currentKey = keys;
                                    break;
                                } else if (keys.charAt(j) != guess && currentKey.charAt(j) == guess) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }



        map = currentMap;
        words = map.get(currentKey);
        masterKey = currentKey;
        return words;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getWord() {
        return masterKey;
    }

    public String getFinishedWord() {
        return words.iterator().next();
    }

    public int guessedLetterCount(String word, char letter) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                count += 1;
            }
        }
        return count;
    }

    public Boolean containsDash(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == '-') {
                return true;
            }
        }
        return false;
    }
}