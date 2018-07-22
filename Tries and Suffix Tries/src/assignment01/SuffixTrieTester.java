/**
 * For documentation of the testing regime, refer to 'COMP3712 Assignment 1 Task 2 Testing.pdf'
 */
package assignment01;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author syrj0001
 */
public class SuffixTrieTester {
    
    public static void main(String[] args) {
        // GUI version
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SuffixTrieGUI frame = new SuffixTrieGUI();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Suffix Trie Search");
            }
        });
        
        /* // Comment out for the command line version
        String fileName = "Frank.txt";
       
        SuffixTrie st = SuffixTrie.readInFromFile(fileName);
        String[] ss = {"hideous", "the only", "onster", ", the", "ngeuhhh"};
        for (String s : ss) {
            SuffixTrieNode sn = st.get(s);
            System.out.println("[" + s + "]: " + sn);
        }*/
    }
}
