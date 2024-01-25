public class MyLanguageModel {
    public static void main(String[] args) {
        MyHashTable myHashTable = new MyHashTable(10, new UnicodeSumHash(10));

        MyHashTable bigramHashTable = new MyHashTable(10, new UnicodeSumHash(10));

        MyHashTable trigramHashTable = new MyHashTable(10, new UnicodeSumHash(10));

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VocabularyListGUI(myHashTable, bigramHashTable, trigramHashTable);
            }
        });
    }
}
