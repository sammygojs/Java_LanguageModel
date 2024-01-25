import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyHashTable {
    private int size;
    private MyHashFunction hashFunction;
    private LinkedList<MyLinkedObject>[] table;

    public MyHashTable(int size, MyHashFunction hashFunction) {
        this.size = size;
        this.hashFunction = hashFunction;
        this.table = new LinkedList[size];

        for (int i = 0; i < size; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public MyHashFunction getHashFunction() {
        return hashFunction;
    }

    public int getTotalWords() {
        int totalCount = 0;

        for (LinkedList<MyLinkedObject> linkedList : table) {
            if(linkedList.size()!=0){
                MyLinkedObject linkedObject = linkedList.getFirst();
                while(linkedObject!=null){
                    totalCount += linkedObject.getCount();
                    linkedObject = linkedObject.getNext();
                }
            }
        }

        return totalCount;
    }

    public int getTotalDifferentWords() {
        int totalDifferentWords = 0;

        for (LinkedList<MyLinkedObject> linkedList : table) {
            if(linkedList.size()!=0){
                MyLinkedObject linkedObject = linkedList.getFirst();
                while(linkedObject!=null){
                    totalDifferentWords++;
                    linkedObject = linkedObject.getNext();
                }
            }
        }

        return totalDifferentWords;
    }

    public LinkedList<MyLinkedObject>[] getTable() {
        return table;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            table[i].clear();
        }
    }

    public void insert(String word) {
        int index = hashFunction.hash(word);
        if(table[index].size() == 0){
            table[index].add(new MyLinkedObject(word));
        }
        else{
            table[index].getFirst().setWord(word);
        }
    }


    public ArrayList<String> displayAllWords() {
        ArrayList<String> list = new ArrayList<>();

        for (LinkedList<MyLinkedObject> linkedList : table){
            if(linkedList.size()!=0){
                MyLinkedObject mylinkedObject = linkedList.getFirst();
                while(mylinkedObject!=null){
                    list.add(mylinkedObject.getWord());
                    mylinkedObject = mylinkedObject.getNext();
                }
            }
        }
        

        return list;
    }

    public ArrayList<String> displayBigrams() {
        ArrayList<String> list = new ArrayList<>();

        for (LinkedList<MyLinkedObject> linkedList : table){
            if(linkedList.size()!=0){
                MyLinkedObject mylinkedObject = linkedList.getFirst();
                while(mylinkedObject!=null){
                    list.add(mylinkedObject.getWord());
                    
                    mylinkedObject = mylinkedObject.getNext();
                }
            }
        }
        

        return list;
    }

    interface MyHashFunction {
        int hash(String word);
    }

    public int getCount(String word) {
        int index = hashFunction.hash(word);
        if(table[index].size() != 0){
            MyLinkedObject mylinkedObject = table[index].getFirst();
            while(mylinkedObject!=null){
            if (mylinkedObject.getWord().equals(word)) {
                return mylinkedObject.getCount();
                }
            mylinkedObject = mylinkedObject.getNext();
            }
        }
        return 0;
    }

    public void setCountZero(String word) {
        int index = hashFunction.hash(word);
        if(table[index].size() != 0){
            MyLinkedObject mylinkedObject = table[index].getFirst();
            while(mylinkedObject!=null){
            if (mylinkedObject.getWord().equals(word)) {
                mylinkedObject.setCount(0);
                }
            mylinkedObject = mylinkedObject.getNext();
            }
        }
    }

    public int[] getListLengths(){
        int[] arr = new int[10];
        int itr=0;
        for (LinkedList<MyLinkedObject> linkedList : table) {
            int count=0;
            if(linkedList.size()!=0){
                MyLinkedObject myLinkedObject = linkedList.getFirst();
                while(myLinkedObject!=null){
                    count++;
                    myLinkedObject = myLinkedObject.getNext();
                }
            }
            arr[itr++]=count;
        }
        return arr;
    }


    public double getAverageListLength() {
        List<Integer> listLengths = new ArrayList<>();
        for (LinkedList<MyLinkedObject> linkedList : table) {
            int count=0;
            if(linkedList.size()!=0){
                MyLinkedObject myLinkedObject = linkedList.getFirst();
                while(myLinkedObject!=null){
                    count++;
                    myLinkedObject = myLinkedObject.getNext();
                }
            }
            listLengths.add(count);
        }

        return calculateAverage(listLengths);
    }

    public double getStandardDeviationListLength() {
        List<Integer> listLengths = new ArrayList<>();
        for (LinkedList<MyLinkedObject> linkedList : table) {
            int count=0;
            if(linkedList.size()!=0){
                MyLinkedObject myLinkedObject = linkedList.getFirst();
                while(myLinkedObject!=null){
                    count++;
                    myLinkedObject = myLinkedObject.getNext();
                }
            }
            listLengths.add(count);
        }

        return calculateStandardDeviation(listLengths);
    }

    private double calculateAverage(List<Integer> values) {
        if (values.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (int value : values) {
            sum += value;
        }

        return (double) sum / values.size();
    }

    private double calculateStandardDeviation(List<Integer> values) {
        if (values.isEmpty()) {
            return 0.0;
        }

        double mean = calculateAverage(values);
        double sumSquaredDifferences = 0.0;

        for (int value : values) {
            double difference = value - mean;
            sumSquaredDifferences += difference * difference;
        }

        double variance = sumSquaredDifferences / values.size();
        return Math.sqrt(variance);
    }
}


