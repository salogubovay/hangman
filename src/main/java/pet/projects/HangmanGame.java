package pet.projects;

import java.io.*;
import java.util.*;

public class HangmanGame {
    private static final String DICTIONARY_FILE = "/dictionary.txt";
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static List<String> stages;
    private static List<String> dictionary;
    private static int maxErrors;

    public static void main(String[] args) {
        initDictionary();
        initStages();
        startGameLoop();
        SCANNER.close();
    }

    private static void startGameLoop() {
        while (true) {
            System.out.println("<< Хотите сыграть в игру? (да / нет) >>");
            String input = SCANNER.nextLine().toLowerCase();
            switch (input) {
                case "да":
                    startRoundLoop();
                    break;
                case "нет":
                    return;
                default:
                    System.out.println("Некорректный ввод: ожидалось 'да' или 'нет'");
            }
        }
    }

    private static void startRoundLoop() {
        int errorCount = 0;
        String word = dictionary.get(RANDOM.nextInt(dictionary.size()));
        Set<Character> notGuessedLetters = createLettersSet(word);
        Set<Character> guessedLetters = new HashSet<>();

        while (errorCount < maxErrors && notGuessedLetters.size() > 0) {
            printCurrentState(errorCount);
            printMaskedWord(word, notGuessedLetters);
            errorCount += readLetter(notGuessedLetters, guessedLetters);
        }
        printCurrentState(errorCount);
        printAnswer(errorCount, word);
    }

    private static Set<Character> createLettersSet(String word) {
        Set<Character> set = new HashSet<>();
        for (char c : word.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    private static int readLetter(Set<Character> notGuessedLetters, Set<Character> guessedLetters) {
        String input = SCANNER.nextLine().toLowerCase();

        if (!isValidInput(input)) {
            System.out.println("Некорректный ввод (введите маленькую русскую букву)");
            return 0;
        }

        char c = input.charAt(0);

        if (guessedLetters.contains(c)) {
            System.out.println("Буква '" + c + "' уже была.");
            return 0;
        }

        guessedLetters.add(c);
        if (notGuessedLetters.contains(c)) {
            notGuessedLetters.remove(c);
            return 0;
        } else {
            return 1;
        }
    }

    private static void printCurrentState(int errorCount) {
        System.out.println("*---------------------*");
        System.out.format("error count = %d\n", errorCount);
        System.out.println(stages.get(errorCount));
    }

    private static void printMaskedWord(String word, Set<Character> notGuessedLetters) {
        String currentWord = createMaskedWord(word, notGuessedLetters);
        System.out.format("%s: ", currentWord);
    }

    private static void printAnswer(int errorCount, String word) {
        System.out.println("Загаданное слово = " + word);
        if (errorCount < maxErrors) {
            System.out.println("Вы ПОБЕДИЛИ :)");
        } else {
            System.out.println("Вы ПРОИГРАЛИ :(");
        }
    }

    private static void initDictionary() {
        dictionary = new ArrayList<>();
        try (InputStream inputStream = Objects.requireNonNull(HangmanGame.class.getResourceAsStream(DICTIONARY_FILE));
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String word;
            while ((word = br.readLine()) != null) {
                dictionary.add(word);
            }
        } catch (Exception e) {
            System.out.println("Ошибка чтения словаря: " + e.getMessage());
        }
    }

    private static boolean isValidInput(String input) {
        return input.length() == 1 && ALPHABET.indexOf(input.charAt(0)) > -1;
    }

    private static String createMaskedWord(String word, Set<Character> notGuessedLetters) {
        StringBuilder output = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (notGuessedLetters.contains(c)) {
                output.append("#");
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

    private static void initStages() {
        /*
            Hangman ASCII art from https://gist.github.com/chrishorton/8510732aa9a80a03c829b09f12e20d9c
        */
        String[] stagesArr = new String[]{
                "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
                "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
        };
        stages = new ArrayList<>();
        Collections.addAll(stages, stagesArr);
        maxErrors = stages.size() - 1;
    }
}
