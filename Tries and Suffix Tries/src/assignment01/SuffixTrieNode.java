package assignment01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SuffixTrieNode {

    SuffixTrieData data = new SuffixTrieData();
    private boolean isTerminal = false;

    /* I have chosen to use HashMap simply because it is very efficient.
     * HashMap has a complexity of O(1) whereas TreeMap for example has O(log n)
     * Benefit of TreeMap is that the keys are maintained in a sorted order but that is not required
     * when we are implementing a trie structure. */
    protected Map<Character, SuffixTrieNode> map = new HashMap<>();

    public void setTerminal(boolean term) {
        isTerminal = term;
    }

    public boolean getTerminal() {
        return isTerminal;
    }

    public SuffixTrieNode getChild(char label) {
        return map.get(label);
    }

    public ArrayList<String> getstartIndexes() {
        return data.getstartIndexes();
    }

    public void addChild(char label, SuffixTrieNode node) {
        map.put(label, node);
    }

    public void addData(String startIndex) {
        data.addStartIndex(startIndex);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
