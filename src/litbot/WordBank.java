package litbot;

import java.util.Random;

/**
 *
 * @author adam
 */
public class WordBank {

    /*
     * Dictionary array that stores word lists organized by letter.
     * The list of words starting with 'A' is at index 0 etc...
     */
    Word[] dictionary = new Word[26];
    Word previousWord;
    boolean debugging = false;

    /**
     *
     * Adds a word to the relevant section WordBank if it does not already
     * exist. Relevant section is determined by the first letter of the word.
     *
     * @author Adam Al-Jumaily
     * @param text (String)
     */
    public void addWord(String text) {
        Word w;
        char firstLetter = text.toUpperCase().charAt(0);
        int index = (int) firstLetter - 65;
        if (!wordExists(firstLetter, text)) {

            w = new Word(text);
            w.nextWord = dictionary[index];
            dictionary[index] = w;

            if (previousWord != null) {
                addWordToBefore(w, previousWord);
                addWordToAfter(previousWord, w);
            }
            if (debugging) {
                System.out.printf("Added word %s to index %d\n", text, index);
            }
        } else {
            if (debugging) {
                System.out.printf("Word %s already exists, manipulating\n", text);
            }
            w = getWord(index, text);
            if (previousWord != null) {
                addWordToBefore(w, previousWord);
                addWordToAfter(previousWord, w);
            }
        }
        previousWord = w;
    }

    /**
     * Uses the words in the wordbank to write out a section.
     *
     *
     * @param length
     */
    public void write(int length) {
        Word w = dictionary[0];
        int count = 0;
        while ((w != null) && (count <= length)) {
            System.out.printf("%s ", w.text);
            w = w.getNext();
            count++;
        }
        System.out.print(".");
    }

    /**
     *
     * Returns a word that has been deemed existant.
     *
     * @param index
     * @param text
     * @return Word specified by text.
     */
    private Word getWord(int index, String text) {
        Word curWord = dictionary[index];

        while (curWord != null) {
            if (curWord.text.equalsIgnoreCase(text)) {
                break;
            } else {
                curWord = curWord.nextWord;
            }
        }

        if (debugging) {
            System.out.printf("Found + returned word %s\n", curWord.text);
        }
        return curWord;
    }

    /**
     *
     * Checks to see if a word already exists in the WordBank, ignores case.
     *
     *
     * @author Adam Al-Jumaily
     * @param firstLetter (char)
     * @param text (String)
     * @return false if the word is not in the bank.
     * @return true if the word is already in the bank.
     */
    private boolean wordExists(char firstLetter, String text) {
        boolean exists = false;

        int index = (int) firstLetter - 65;
        Word curWord = dictionary[index];
        while (curWord != null) {
            if (curWord.text.equalsIgnoreCase(text)) {
                if (debugging) {
                    System.out.printf("The word '%s' exists at index %d\n",
                            text, index);
                }
                exists = true;
                break;
            }
            curWord = curWord.nextWord;
        }

        return exists;
    }

    /**
     *
     * Attempts to add a word to the 'after' array of another word if it does
     * not currently exist in the array.
     *
     * @param curWord The word whose 'after' array we are adding to.
     * @param followingWord
     */
    private void addWordToAfter(Word curWord, Word followingWord) {
        if (!existsInArray(curWord, followingWord, 'a')) {
            curWord.addAfter(followingWord);
        }
    }

    /**
     *
     * Attempts to add a word to the 'before' array of another word if it does
     * not currently exist in the array.
     *
     * @param curWord
     * @param prevWord
     */
    private void addWordToBefore(Word curWord, Word prevWord) {
        if (!existsInArray(curWord, prevWord, 'b')) {
            curWord.addBefore(prevWord);
        }
    }

    /**
     *
     * Checks to see if the word to be added to the before or after array
     * already exists.
     *
     * @param curWord
     * @param wordToAdd
     * @param arrayType 'a' for after, 'b' for before
     * @return false if it doesn't exist
     * @return true if exists.
     */
    private boolean existsInArray(Word curWord, Word wordToAdd, char arrayType) {
        boolean exists = false;

        switch (arrayType) {
            case 'a':
                if (curWord.afterIndex == 0) {
                    break;
                } else {
                    for (int i = 0; i < curWord.afterIndex; i++) {
                        if (curWord.after[i].text.equalsIgnoreCase(wordToAdd.text)) {
                            exists = true;
                            break;
                        }
                    }
                }
                break;
            case 'b':
                if (curWord.beforeIndex == 0) {
                    break;
                } else {
                    for (int i = 0; i < curWord.beforeIndex; i++) {
                        if (curWord.before[i].text.equalsIgnoreCase(wordToAdd.text)) {
                            exists = true;
                            break;
                        }
                    }
                }
                break;
        }
        return exists;
    }
}

class Word {

    boolean debugging = false;

    Word nextWord;
    Random rand = new Random();
    Word[] after = new Word[10];
    int afterIndex = 0;
    Word[] before = new Word[10];
    int beforeIndex = 0;
    final String text;

    /*
     * 'regular' for normal words.
     * 'first' for first word.
     * 'last' for last word.
     */
    final String type;

    public Word(String s) {
        this(s, "regular");
    }

    public Word(String s, String t) {
        this.text = s;
        this.type = t;
    }

    public void addBefore(Word w) {
        if (beforeIndex == (before.length - 1)) {
            resizeBefore();
        }
        before[beforeIndex] = w;
        beforeIndex++;
        if (debugging) {
            System.out.printf("index of word '%s' before is %d\n", text, beforeIndex);
        }

    }

    public void addAfter(Word w) {
        if (afterIndex == (after.length - 1)) {
            resizeAfter();
        }
        after[afterIndex] = w;
        afterIndex++;
        if (debugging) {
            System.out.printf("index of word '%s' after is %d\n", text, afterIndex);
        }
    }

    public Word getNext() {
        Word next;
        if (afterIndex == 0) {
            next = null;
        } else {
            int chosenWord = rand.nextInt(afterIndex);
            next = after[chosenWord];
        }
        return next;
    }

    private void resizeAfter() {
        int nextLength = this.after.length * 2;
        Word[] newAfter = new Word[nextLength];
        for (int i = 0; i < this.after.length; i++) {
            newAfter[i] = this.after[i];
        }
        this.after = newAfter;
    }

    private void resizeBefore() {
        int nextLength = this.before.length * 2;
        Word[] newBefore = new Word[nextLength];
        for (int i = 0; i < this.before.length; i++) {
            newBefore[i] = this.before[i];
        }
        this.before = newBefore;
    }

}
