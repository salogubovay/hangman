package org.example;
import java.util.*;
import java.io.*;

public class Game {
    private static final String START_MESSAGE = "Do you want to play / exit? (p / e): ";
    private List<String> dictionary = new ArrayList<>();
    private static final String DICTIONARY_FILE = "src/main/resources/dictionary.txt";
    private final int MAX_ERRORS = Pictures.getStageSize() - 1;
    private Random rnd = new Random(17);
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    public void start() {
        boolean continueGame = true;
        initDictionary();
        try (Scanner sc = new Scanner(System.in)) {
            while (continueGame) {
                System.out.println(START_MESSAGE);
                String ans = sc.nextLine();
                switch (ans) {
                    case "p":
                        play(sc);
                        break;
                    case "e":
                        continueGame = false;
                        break;
                    default:
                        System.out.println("Incorrect input: 'p' or 'e' expected");
                }
            }
        }
    }

    private void play(Scanner sc) {
        int errorCount = 0;
        String word = dictionary.get(rnd.nextInt(dictionary.size()));
        Set<Character> notGuessedLetters = new HashSet<>();
        Set<Character> guessedLetters = new HashSet<>();
        for (char c : word.toCharArray()) {
            notGuessedLetters.add(c);
        }

        while (errorCount < MAX_ERRORS && notGuessedLetters.size() > 0) {
            String currentWord = createMaskedWord(word, notGuessedLetters);
            printStatus(errorCount);
            System.out.print(currentWord + ": ");
            String input = sc.nextLine();
            if (isValidInput(input)) {
                char c = input.charAt(0);
                if (guessedLetters.contains(c)) {
                    continue;
                }
                guessedLetters.add(c);
                if (notGuessedLetters.contains(c)) {
                    notGuessedLetters.remove(c);
                } else {
                    errorCount++;
                }
            } else {
                System.out.println("input is invalid, try something else");
            }
        }

        printStatus(errorCount);
        printAnswer(errorCount, word);
    }

    private boolean isValidInput(String input) {
        return input.length() == 1 && ALPHABET.indexOf(input.charAt(0)) > -1;
    }

    private void printStatus(int errorCount) {
        StringBuilder output = new StringBuilder();
        output.append("error count = " + errorCount + "\n");
        output.append(Pictures.getStage(errorCount));
        System.out.println(output.toString());
    }

    private void printAnswer(int errorCount, String word) {
        System.out.println("Correct word = " + word);
        if ( errorCount == MAX_ERRORS) {
            System.out.println("You LOST!");
        } else {
            System.out.println("You WON!");
        }
    }

    private String createMaskedWord(String word, Set<Character> notGuessedLetters) {
        StringBuilder output = new StringBuilder();
        for(char c : word.toCharArray()) {
            if (notGuessedLetters.contains(c)) {
                output.append("#");
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

    private void initDictionary() {
        try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_FILE))) {
            String word;
            while((word = br.readLine())!=null) {
                dictionary.add(word);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
