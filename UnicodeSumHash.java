public class UnicodeSumHash extends MyHashFunction implements MyHashTable.MyHashFunction {
    public UnicodeSumHash(int tableSize) {
        super(tableSize);
    }

    @Override
    public int hash(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Input word cannot be null or empty");
        }
        int unicodeSum = 0;
        for (char character : word.toCharArray()) {
            unicodeSum += (int) character;
        }
        return unicodeSum % hashTableSize;
    }
}
