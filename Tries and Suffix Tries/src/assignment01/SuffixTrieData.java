package assignment01;

import java.util.ArrayList;

public class SuffixTrieData {

    private ArrayList<String> startIndexes = new ArrayList<String>();

    public void addStartIndex(String startIndex) {
        startIndexes.add(startIndex);
    }
    
    public ArrayList<String> getstartIndexes() {
        return startIndexes;
    }

    @Override
    public String toString() {
        return startIndexes.toString();
    }
}
