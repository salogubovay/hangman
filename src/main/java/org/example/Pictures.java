package org.example;

public class Pictures {
    /*
        Hangman ASCII art from https://gist.github.com/chrishorton/8510732aa9a80a03c829b09f12e20d9c
     */
    private static String[] stages = {
        "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
    };

    public static int getStageSize() {
        return stages.length;
    }

    public static String getStage(int num) {
        if (num < 0 || num > stages.length) {
            return "Incorrect stage number.";
        } else {
            return stages[num];
        }
    }
}
