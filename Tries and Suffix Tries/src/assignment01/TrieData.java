package assignment01;

public class TrieData {

    private int frequency = -1;
    private int rank = 0;

    public TrieData(int frequency, int rank) {
        this.frequency = frequency;
        this.rank = rank;
    }

    public TrieData(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "frequency=" + String.valueOf(frequency) + ", rank=" + String.valueOf(rank);
    }
}
