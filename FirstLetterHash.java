public class FirstLetterHash extends MyHashFunction implements MyHashTable.MyHashFunction {
    public FirstLetterHash(int size) {
        super(size);
    }

    @Override
    public int hash(String word) {
        if (word.isEmpty()) {
            return 0;
        }
        return word.charAt(0) % hashTableSize;
    }
}