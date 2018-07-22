package assignment01;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrieNode {

    protected TrieData data = null;
    private boolean terminal = false;
    private int numChildren = 0;

    /* I have chosen to use HashMap simply because it is very efficient.
     * HashMap has a complexity of O(1) whereas TreeMap for example has O(log n)
     * Benefit of TreeMap is that the keys are maintained in a sorted order but that is not required
     * when we are implementing a trie structure. */
    private Map<Character, TrieNode> map = new HashMap();

    /**
     * Lookup a child node of the current node that is associated with a
     * particular character label.
     *
     * @param label The label to search for
     * @return The child node associated with the provided label
     */
    public TrieNode getChild(char label) {
        return map.get(label);
    }

    public Set<Character> getKeys() {
        return map.keySet();
    }

    /**
     * Add a child node to the current node, and associate it with the provided
     * label.
     *
     * @param label The character label to associate the new child node with
     * @param node The new child node that is to be attached to the current node
     */
    public void addChild(char label, TrieNode node) {
        map.put(label, node);
        ++numChildren;
    }

    public void setTerminal(boolean term) {
        terminal = term;
    }

    public boolean getTerminal() {
        return terminal;
    }

    /**
     * Add a new data object to the node.
     *
     * @param dataObject The data object to be added to the node.
     */
    public void addData(TrieData dataObject) {
        data = dataObject;
    }

    /**
     * The toString() method for the TrieNode that outputs in the format
     * TrieNode; isTerminal=[true|false], data={toString of data},
     * #children={number of children} for example, TrieNode; isTerminal=true,
     * data=3, #children=1
     */
    @Override
    public String toString() {
        String dataString = new String();
        if (data == null) {
            dataString = "null";
        }
        else {
            dataString = data.toString();
        }
        return "TrieNode: isTerminal=" + Boolean.toString(terminal)
                + ", data=" + dataString
                + ", #children=" + numChildren;
    }
}
