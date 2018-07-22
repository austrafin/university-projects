package assignment01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Trie {

    private final TrieNode root = new TrieNode();
    private int freq;

    /**
     * Inserts a string into the trie and returns the last node that was
     * inserted.
     *
     * @param str The string to insert into the trie
     * @param data	The data associated with the string
     * @return The last node that was inserted into the trie
     */
    public TrieNode insert(String str, TrieData data) {
        char characters[] = str.toCharArray();
        TrieNode currentNode;
        currentNode = root;
        for (int i = 0; i < characters.length; ++i) {

            if (currentNode.getChild(characters[i]) != null) {
                currentNode = currentNode.getChild(characters[i]);
            }
            else {
                currentNode.addChild(characters[i], new TrieNode());
                currentNode = currentNode.getChild(characters[i]);

            }
            if (i == characters.length - 1) {
                currentNode.addData(data);
                currentNode.setTerminal(true);
            }
        }
        return currentNode;
    }

    /**
     * Search for a particular prefix in the trie, and return the final node in
     * the path from root to the end of the string, i.e. the node corresponding
     * to the final character. getNode() differs from get() in that getNode()
     * searches for any prefix starting from the root, and returns the node
     * corresponding to the final character of the prefix, whereas get() will
     * search for a whole word only and will return null if it finds the pattern
     * in the trie, but not as a whole word. A "whole word" is a path in the
     * trie that has an ending node that is a terminal node.
     *
     * @param str The string to search for
     * @return the final node in the path from root to the end of the prefix, or
     * null if prefix is not found
     */
    public TrieNode getNode(String str) {
        char characters[] = str.toCharArray();
        TrieNode currentNode;
        currentNode = root;

        for (int i = 0; i < characters.length; ++i) {
            currentNode = currentNode.getChild(characters[i]);
            if (currentNode == null) {
                return null; // string does not exist
            }
        }
        return currentNode; // string exists
    }

    /**
     * Searches for a word in the trie, and returns the final node in the search
     * sequence from the root, i.e. the node corresponding to the final
     * character in the word.
     *
     * getNode() differs from get() in that getNode() searches for any prefix
     * starting from the root, and returns the node corresponding to the final
     * character of the prefix, whereas get() will search for a whole word only
     * and will return null if it finds the pattern in the trie, but not as a
     * whole word. A "whole word" is a path in the trie that has an ending node
     * that is a terminal node.
     *
     * @param str The word to search for
     * @return The node corresponding to the final character in the word, or
     * null if word is not found
     */
    public TrieNode get(String str) {
        TrieNode node;
        node = getNode(str);

        if (node != null && node.getTerminal()) {
            return node; // prefix exists
        }
        else {
            return null; // prefix does not exist
        }
    }

    /**
     * Helper function for getAlphabeticalListWithPrefix()
     *
     * @param list the list of words that match the prefix
     * @param node current node to inspect
     * @param word starts as the prefix characters but are gradually added to it
     * with each recursion.
     */
    public void findWords(List<String> list, TrieNode node, String word) {
        Set<Character> keys;

        if (node != null) {
            keys = node.getKeys();
            Character keysArr[] = new Character[keys.size()];

            /**
             * if a terminal node encountered, add the associated string to the
             * word list.
             */
            if (node.getTerminal() && !list.contains(word)) {
                list.add(word);
            }

            keysArr = keys.toArray(keysArr);

            /**
             * Add a character to the word and move to the next node (a new
             * recursion)
             */
            for (int i = 0; i < keysArr.length; ++i) {
                StringBuilder newWord = new StringBuilder(word);
                newWord.append(keysArr[i]);
                findWords(list, node.getChild(keysArr[i]), newWord.toString());
            }
        }
    }

    /**
     * Retrieve from the trie an alphabetically sorted list of all words
     * beginning with a particular prefix.
     *
     * @param prefix The prefix with which all words start.
     * @return The list of words beginning with the prefix, or an empty list if
     * the prefix was not found.
     */
    public List<String> getAlphabeticalListWithPrefix(String prefix) {
        /**
         * Using LinkedList because it allows a constant time adding of
         * elements compared to ArrayList which requires shifting existing
         * elements over by one each time a new element is added.
         */
        List<String> found = new LinkedList();
        findWords(found, getNode(prefix), prefix);
        Collections.sort(found);
        return found;
    }

    /**
     * Helper method for getMostFrequentWordWithPrefix()
     *
     * @param s with each recursion we append a character to this as we traverse
     * down to each child node.
     * @param node child node to be processed
     * @param wordPrefix prefix of all the valid words in the set, for example
     * valid words for prefix "ap" would be "apple", "application", "appear"...
     */
    public void findMostFrequentWord(StringBuilder s, TrieNode node, String wordPrefix) {
        Set<Character> keys;

        if (node != null) {
            keys = node.getKeys();
            Character keysArr[] = new Character[keys.size()];

            /**
             * When a terminal node is encountered, that means a valid word has
             * been found. The frequency of this word is checked and if it is
             * more than the current max, we record this as the new max. The
             * word is reset to the original prefix.
             */
            if (node.getTerminal()) {
                if (node.data.getFrequency() > freq) {
                    s.delete(0, s.length()).append(wordPrefix);
                    freq = node.data.getFrequency();
                }
            }

            keysArr = keys.toArray(keysArr);

            /**
             * Append the node's character to the word. Move to the child node
             * and do a new recursion.
             */
            for (int i = 0; i < keysArr.length; ++i) {
                StringBuilder newWord = new StringBuilder(wordPrefix);
                newWord.append(keysArr[i]);
                findMostFrequentWord(s, node.getChild(keysArr[i]), newWord.toString());
            }
        }
    }

    /**
     * NOTE: TO BE IMPLEMENTED IN ASSIGNMENT 1 Finds the most frequently
     * occurring word represented in the trie (according to the dictionary file)
     * that begins with the provided prefix.
     *
     * @param prefix The prefix to search for
     * @return The most frequent word that starts with prefix
     */
    public String getMostFrequentWordWithPrefix(String prefix) {
        StringBuilder found = new StringBuilder("");
        freq = 0;
        findMostFrequentWord(found, getNode(prefix), prefix);
        freq = 0;
        String word = found.toString();

        if ("".equals(word)) {
            return "null";
        }
        else {
            return prefix + word.substring(prefix.length());
        }
    }

    /**
     * NOTE: TO BE IMPLEMENTED IN ASSIGNMENT 1 Reads in a dictionary from file
     * and places all words into the trie.
     *
     * @param fileName the file to read from
     * @return the trie containing all the words
     */
    public static Trie readInDictionary(String fileName) {
        Scanner fileScanner;
        Trie t = new Trie();

        try {
            // use a FileInputStream to ensure correct reading end-of-file
            fileScanner = new Scanner(new FileInputStream(fileName));

            while (fileScanner.hasNextLine()) {
                String nextLine = fileScanner.nextLine();
                String[] keys = nextLine.split("\\s+");
                t.insert(keys[1], new TrieData(Integer.parseInt(keys[2]), Integer.parseInt(keys[0])));
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return t;
    }
}
