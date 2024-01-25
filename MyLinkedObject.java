public class MyLinkedObject {
    private String word;
    private int count;
    private MyLinkedObject next;

    public MyLinkedObject(String word) {
        this.word = word;
        this.count = 1; 
        this.next = null;
    }

    public void setWord(String w) {
        if (w.equals(word)) {
            count++;
        } else {
            if (next == null) {
                this.next = new MyLinkedObject(w);
            } else if (w.compareTo(next.getWord()) < 0) {
                MyLinkedObject newObject = new MyLinkedObject(w);
                newObject.setNext(next);
                setNext(newObject);
            } else {
                next.setWord(w);
            }
        }
    }

    
    
    public void setNext(MyLinkedObject next) {
        this.next = next;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }

    public MyLinkedObject getNext() {
        return next;
    }
}
